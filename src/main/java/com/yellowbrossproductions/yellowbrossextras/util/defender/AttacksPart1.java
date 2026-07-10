package com.yellowbrossproductions.yellowbrossextras.util.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGunEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.*;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.ChainsawEntity;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class AttacksPart1 {
    public static void tickPhase1Attacks(DefenderEntity defender) {
        LivingEntity target = defender.tryToFindTarget();
        int ticks = defender.attackTicks;
        int ticks2 = defender.attackTicks2;

        if (defender.attackType == defender.attack_saws) {
            if (ticks == 7) {
                defender.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.8F);
                defender.makeExplodeParticles();
                defender.setWeaponToShow(1);
            }
            if (ticks == 15) {
                defender.level.playSound(null, defender, YESoundEvents.ENTITY_DEFENDER_SAW_START.get(), defender.getSoundSource(), 2.0F, 1.0F);
            }
            if (ticks >= 22 && ticks <= 82) {
                LivingEntity t = null;
                if (defender.getTarget() != null && ((defender.getTarget() instanceof Player) || ((defender.getTarget().getBlockStateOn() != Blocks.AIR.defaultBlockState()) || defender.getTarget().isPassenger()))) {
                    t = defender.getTarget();
                } else {
                    List<Mob> list = defender.level.getEntitiesOfClass(Mob.class, defender.getBoundingBox().inflate(40.0D), p -> {
                        return p instanceof Enemy && EntityUtil.canHurtThisMob(p, defender) && ((p.getBlockStateOn() != Blocks.AIR.defaultBlockState())) && defender.isInAttackSight(p);
                    });
                    if (!list.isEmpty()) {
                        t = list.get(0);
                    }
                }
                if (t != null && (!(t instanceof Player) || (((ticks - 22) % 30 == 0 && (defender.distanceTo(t) > 10.0D)) || ticks == 22))) {
                    float power = (float) 5.5F;
                    defender.setCharge(t.position().subtract(defender.position()).normalize().scale(power).scale(0.2d));
                }
                if (defender.chargeX != 0 || defender.chargeZ != 0) defender.setDeltaMovement(defender.chargeX, defender.getDeltaMovement().y, defender.chargeZ);
                if (defender.horizontalCollision) {
                    defender.setDeltaMovement(defender.getDeltaMovement().add(0.0D, 0.15D, 0.0D));
                }
                defender.makeSawParticles1();
                for (Entity entity : defender.level.getEntities(defender, defender.getBoundingBox().inflate(15.0F))) {
                    if (EntityUtil.canHurtThisMob(entity, defender) && entity instanceof LivingEntity && entity.isAlive()) {
                        double x = defender.getX() - entity.getX();
                        double y = defender.getY() - entity.getY();
                        double z = defender.getZ() - entity.getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        if ((defender.distanceToSqr(entity) < 9.0D && !(entity instanceof Player)) || (defender.distanceToSqr(entity) < 4.5D)) {
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_SAW.get(), 1.0F, 1.0F);
                            entity.hurt(DamageSource.mobAttack(defender).bypassArmor(), 10.0F);
                            entity.hurtMarked = true;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 0.4D, (-y / d * 0.4D) + 0.2D, -z / d * 0.4D));
                            defender.makeSawParticles2(entity);
                        }
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_sword) {
            if (ticks == 11) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_WHOOSH.get(), 2.0F, 1.0F);
            }
            if (ticks == 12 && !defender.level.isClientSide) {
                float healing = 0.0F;

                defender.makeSpinParticles();
                for (LivingEntity entity : defender.level.getEntitiesOfClass(LivingEntity.class, defender.getBoundingBox().inflate(15.0F))) {
                    if (EntityUtil.canHurtThisMob(entity, defender) && entity.isAlive() && entity != defender) {
                        double x = defender.getX() - entity.getX();
                        double y = defender.getY() - entity.getY();
                        double z = defender.getZ() - entity.getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        if (defender.distanceTo(entity) < 3.0D) {
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch());
                            entity.hurt(DamageSource.mobAttack(defender), ((float) defender.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) * EntityUtil.multiplyToScrewArmor((LivingEntity) entity, 0.5f));
                            entity.hurtMarked = true;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 2.5D, (-y / d * 0.4D) + 0.5D, -z / d * 2.5D));
                        }

                        healing += 2.0F;
                    }
                }

                defender.heal(Math.min(healing, 20.0F));
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_axes) {
            if (ticks == (24 * defender.throwTimes)) {
                defender.setAnimationState("none");
                defender.setAnimationState("axes");
                defender.throwTimes += 1;
            }
            if ((ticks == 6 + (24 * (defender.throwTimes - 1))) || (ticks == 18 + (24 * (defender.throwTimes - 1)))) {
                defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                if (target != null && !defender.level.isClientSide) {
                    DefenderAxeEntity axe = new DefenderAxeEntity(defender.level, defender, target.getBoundingBox().getCenter().subtract(defender.position().add(0, 1, 0)));
                    axe.setPos(defender.getX(), defender.getY() + 1, defender.getZ());
                    defender.level.addFreshEntity(axe);
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_boomerang) {
            if (ticks == 10) {
                defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 0.9F);
                defender.setWeaponToShow(0);
                if (target != null && !defender.level.isClientSide) {
                    BoomerangEntity projectile = new BoomerangEntity(YEEntityTypes.Boomerang.get(), defender.level);
                    projectile.moveTo(defender.getPosition(0).add(0, 1, 0));

                    projectile.setYHeadRot(defender.getYHeadRot());
                    projectile.setYRot(defender.getYHeadRot());
                    if (defender.getTeam() != null) {
                        defender.level.getScoreboard().addPlayerToTeam(projectile.getStringUUID(),
                                defender.level.getScoreboard().getPlayerTeam(defender.getTeam().getName()));
                    }

                    float power = (float) 5.0F;
                    projectile.setAcceleration(target.position().add(0, (target.getBbHeight() / 2.0D), 0).subtract(defender.position()).normalize().scale(power).scale(0.2d));
                    projectile.setGoFor(target);
                    projectile.setOlder();

                    projectile.setShooter(defender);
                    defender.level.addFreshEntity(projectile);
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_spikes) {
            if (target != null) {
                if (ticks == 15) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 2.0F, 1.0F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_SPIKE.get(), 2.0F, 1.0F);
                    CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 15);
                    defender.performSpellCasting(false);
                }
                if (ticks == 39) {
                    defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);

                    if (target instanceof Player) {
                        defender.setDeltaMovement((target.getX() - defender.getX()) * 0.2D, 1.6D, (target.getZ() - defender.getZ()) * 0.2D);
                    } else {
                        List<Mob> targets = defender.level.getEntitiesOfClass(Mob.class, target.getBoundingBox().inflate(30.0f), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && p instanceof Enemy && defender.isInAttackSight(p));
                        Vec3 strikeZone = EntityUtil.findDensestMobCluster(targets, 6.0d);

                        if (strikeZone != null) {
                            defender.averageXCord = strikeZone.x;
                            defender.averageZCord = strikeZone.z;
                        } else {
                            defender.averageXCord = target.getX();
                        }

                        if (strikeZone != null) {
                            defender.setDeltaMovement((defender.averageXCord - defender.getX()) * 0.2D, 3.0D, (defender.averageZCord - defender.getZ()) * 0.2D);
                        } else {
                            defender.setDeltaMovement((target.getX() - defender.getX()) * 0.2D, 1.6D, (target.getZ() - defender.getZ()) * 0.2D);
                        }
                    }
                    defender.jumpAttacking = true;
                }
                if (ticks >= 39 && !defender.isOnGround()) {
                    defender.performSpellWarn2(2);

                    if (!(target instanceof Player)) {
                        double d0 = defender.getX() - defender.averageXCord;
                        double d2 = defender.getZ() - defender.averageZCord;
                        double dist = d0 * d0 + d2 * d2;
                        if (dist < 9.0D) {
                            defender.slamTicks += 1;

                            if (defender.slamTicks > 0 && defender.slamTicks <= 10) {
                                if (defender.slamTicks == 1) {
                                    defender.setDeltaMovement(0.0D, 0.4D, 0.0D);
                                    defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.7F);
                                    defender.setAnimationState("spikes_slam");
                                }

                                defender.setDeltaMovement(0.0D, defender.getDeltaMovement().y, 0.0D);
                            }

                            if (defender.slamTicks > 10) {
                                defender.setDeltaMovement(0.0D, -4.0D, 0.0D);
                                double x = (int)(defender.averageXCord);
                                double z = (int)(defender.averageZCord);
                                int worldHeight = defender.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)x, (int)z);
                                if (defender.stretchHelper == 0) {
                                    defender.stretchHelper = defender.getY();
                                }
                                float stretcher = (float) (defender.stretchHelper - defender.getY());
                                if (worldHeight > defender.level.getMinBuildHeight() + 65 && stretcher != 0) {
                                    defender.setStretch(worldHeight * stretcher / 1000);
                                }
                            }
                        }
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_shurikens) {
            if (ticks == 4) {
                defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.2F);
            }
            if (ticks == 5) {
                defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.3F);
            }
            if (ticks == 6) {
                defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.5F);
            }
            if (ticks == 25) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SHURIKEN_LAUNCHER_WARN.get(), 2.0F, 1.0F);
            }
            if (ticks == 36) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SHOOT.get(), 3.0F, 1.0F);

                float radius2 = 1.1f;
                double x = defender.getX() + 0.8F * Math.sin(-defender.getYRot() * Math.PI / 180) + radius2 * Math.sin(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                double y = defender.getY() + 1.0 + radius2 * Math.sin(-defender.getXRot() * Math.PI / 180);
                double z = defender.getZ() + 0.8F * Math.cos(-defender.getYRot() * Math.PI / 180) + radius2 * Math.cos(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                List<LivingEntity> list = defender.level.getEntitiesOfClass(LivingEntity.class, new AABB(x - 2.0D, defender.getY(), z - 2.0D, x + 2.0D, defender.getY() + 2.0D, z + 2.0D));
                for (LivingEntity caught : list) {
                    if (caught != defender && caught.isAlive()) {
                        float amount = caught.getMaxHealth() / 12.5F;
                        caught.hurt(DamageSource.mobAttack(defender), (float) 12.0F + amount);
                    }
                }

                if (target != null) {
                    for (int i = 0; i < 75; ++i) {
                        ShurikenEntity shuriken = new ShurikenEntity(defender.level, defender);

                        shuriken.setPos(x, defender.getY() + 1.0D, z);

                        double d0 = target.getEyeY() - (double)1.1F;
                        double d1 = target.getX() - defender.getX();
                        double d2 = d0 - shuriken.getY();
                        double d3 = target.getZ() - defender.getZ();
                        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
                        shuriken.shoot(d1, d2 + d4, d3, 1.0F, 24.0F);
                        defender.level.addFreshEntity(shuriken);
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_chainsaw) {
            Vec3 lookTo = defender.getLookAngle().scale(4.0d);

            if (target != null) {
                lookTo = target.getBoundingBox().getCenter();

                if (!(target instanceof Player)) {
                    List<Mob> targets = defender.level.getEntitiesOfClass(Mob.class, defender.getBoundingBox().inflate(10.0f), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && p instanceof Enemy && defender.isInAttackSight(p));
                    Vec3 strikeZone = EntityUtil.findDensestMobCluster(targets, 6.0d);
                    if (strikeZone != null) lookTo = strikeZone;
                }
            }

            defender.setSpecialLookLocation(lookTo);
            if (ticks == 3) {
                defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
            }
            if (ticks == 15) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_CHAINSAW_CATCH.get(), 2.0F, 1.0F);
            }
            if (ticks == 19) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_CHAINSAW_WARN.get(), 2.0F, 1.0F);
            }
            if (ticks == 23) {
                defender.setShakeMultiplier(30);
            }
            if (ticks >= 30) {
                ChainsawEntity beam = null;
                if (ticks == 30) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CHAINSAW.get(), 2.0F, 1.0F);
                    if (!defender.level.isClientSide) {
                        beam = new ChainsawEntity(YEEntityTypes.Chainsaw.get(), defender.level, defender, defender.getX(), defender.getY() + 1.125, defender.getZ(), (float) ((defender.yHeadRot + 90) * Math.PI / 180), (float) (-defender.getXRot() * Math.PI / 180), 66);
                        defender.level.addFreshEntity(beam);
                    }
                }
                if (beam != null) {
                    float radius2 = 1.1f;
                    double x = defender.getX() + 0.8F * Math.sin(-defender.getYRot() * Math.PI / 180) + radius2 * Math.sin(-defender.yBodyRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double y = defender.getY() + 1.0 + radius2 * Math.sin(-defender.getXRot() * Math.PI / 180);
                    double z = defender.getZ() + 0.8F * Math.cos(-defender.getYRot() * Math.PI / 180) + radius2 * Math.cos(-defender.yBodyRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    beam.setPos(defender.getX(), defender.getY() + 1.125D, defender.getZ());

                    float yaw = defender.yHeadRot + 90;
                    float pitch = defender.getChainsawLookX();
                    beam.setYaw((float) (yaw * Math.PI / 180));
                    beam.setPitch((float) (pitch * Math.PI / 180));
                }
                defender.lerpChainsawLookX(defender.specialLookLocation, 100.0F, 4);

                if (defender.lerpChainsawSteps > 0) {
                    defender.setChainsawLookX(defender.getChainsawLookX() + (float)(defender.lerpChainsawX - (double)defender.getChainsawLookX()) / (float)defender.lerpChainsawSteps);
                    --defender.lerpChainsawSteps;
                }
            }
            if (ticks == 96) {
                defender.setShakeMultiplier(0);
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_claws) {
            if (target != null) {
                if (ticks == 12) {
                    float radius2 = 1.2f;
                    double x = defender.getX() + 0.8F * Math.sin(-defender.getYRot() * Math.PI / 180) + radius2 * Math.sin(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double y = defender.getY() + 1.0 + radius2 * Math.sin(-defender.getXRot() * Math.PI / 180);
                    double z = defender.getZ() + 0.8F * Math.cos(-defender.getYRot() * Math.PI / 180) + radius2 * Math.cos(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double thing = 1.2D;
                    List<LivingEntity> list = defender.level.getEntitiesOfClass(LivingEntity.class, new AABB(x - thing, defender.getY(), z - thing, x + thing, defender.getY() + thing, z + thing));
                    for (LivingEntity caught : list) {
                        if (caught != defender && caught.isAlive()) {
                            if (caught.hurt(DamageSource.mobAttack(defender), 8.0F)) {
                                caught.stopRiding();
                                defender.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 2.0F, 1.0F);
                                caught.setDeltaMovement(caught.getDeltaMovement().add(0.0D, 2.0D, 0.0D));
                                if (!caught.hasEffect(YEEffects.KNOCKED_OUT.get())) {
                                    caught.addEffect(new MobEffectInstance(YEEffects.KNOCKED_OUT.get(), 100, 0, true, true, true));
                                }
                                if (caught == target) {
                                    defender.shouldContinueAttacking = true;
                                    defender.clawsTarget = caught;
                                }
                            }
                        }
                    }
                }
                if (defender.clawsTarget != null && defender.shouldContinueAttacking && defender.clawsTarget.isAlive()) {
                    if (ticks == 20) {
                        defender.setAnimationState("claws_continue");
                    }
                    if (ticks == (5 + 22)) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_WHOOSH.get(), 2.0F, 1.0F);
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_SPIKE.get(), 2.0F, 1.5F);
                    }
                    if (ticks == (16 + 22)) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_JUMP.get(), 2.0F, 1.0F);
                        defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                        double multiplier = 0.2D;
                        defender.setDeltaMovement((defender.clawsTarget.getX() - defender.getX()) * multiplier,
                                (defender.clawsTarget.getY() - defender.getY()) * 0.1D,
                                (defender.clawsTarget.getZ() - defender.getZ()) * multiplier);
                        defender.jumpAttacking = true;
                    }
                    if (ticks > (16 + 22) && ticks < (30 + 22)) {
                        if (defender.distanceToSqr(defender.clawsTarget) < 4.5D && defender.getY() >= defender.clawsTarget.getY() - 0.2D && !defender.itsTimeToClawTarget) {
                            defender.itsTimeToClawTarget = true;
                            defender.jumpTicks = 21;
                            defender.setAnimationState("claws_end");

                            defender.clawsTarget.setOldPosAndRot();
                            defender.setOldPosAndRot();
                        }
                    }
                    if (defender.itsTimeToClawTarget) {
                        defender.setDeltaMovement(0.0D, 0.0D, 0.0D);
                        defender.clawsTarget.setDeltaMovement(0.0D, 0.0D, 0.0D);
                        if (defender.jumpTicks == (21 - 1) || defender.jumpTicks == (21 - 6)) {
                            if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 6.0F)) {
                                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 4);
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch());
                                defender.clawsTarget.invulnerableTime = 0;
                                defender.clawsTarget.hurtTime = 3;
                            }
                        }
                        if (defender.jumpTicks == (21 - 12) || defender.jumpTicks == (21 - 17)) {
                            if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 5.0F)) {
                                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 4);
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SMACK.get(), 2.0F, defender.getVoicePitch() + 0.2F);
                                defender.clawsTarget.hurtTime = 3;
                                defender.clawsTarget.invulnerableTime = 0;
                            }
                        }
                        if (defender.jumpTicks == 0) {
                            defender.itsTimeToClawTarget = false;
                            if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 8.0F)) {
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 2.0F, 1.0F);
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, 1.0F);
                                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.2f, 0, 15);
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SMACK.get(), 2.0F, defender.getVoicePitch());
                                defender.clawsTarget.invulnerableTime = 0;
                                defender.clawsTarget.fallDistance = 5.0F;
                                defender.clawsTarget.setDeltaMovement(0.0D, -1.2D, 0.0D);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void tickPhase2Attacks(DefenderEntity defender) {
        LivingEntity target = defender.tryToFindTarget();
        int ticks = defender.attackTicks;
        int ticks2 = defender.attackTicks2;

        if (defender.attackType == defender.attack_ratatatabow) {
            if (ticks == 30) {
                double mult = 0.04d;

                if (!defender.level.isClientSide) {
                    defender.setDiscardFriction(true);
                    defender.setMaxWobble(15);
                    if (target != null) {
                        defender.setDeltaMovement(((target.getX() - defender.getX()) * 2.0D) * mult,
                                1.5D,
                                ((target.getZ() - defender.getZ()) * 2.0D) * mult);
                    } else {
                        defender.setDeltaMovement(0,
                                1.5d,
                                0);
                    }
                }
                defender.setCustomRender(1);
                EntityUtil.makeCircleParticles(defender.level, defender.getPosition(0).add(0, 0.3, 0), ParticleTypes.POOF, 10, 1.0f, Vec3.ZERO, 0.0F);
            }
            if (ticks > 30) {
                if (defender.isOnGround() && ticks2 == 0) {
                    defender.setDiscardFriction(false);
                    defender.attackTicks2 = 1;
                    defender.setCustomRender(0);
                    defender.setAnimationState("ratatatabow2");
                }
                if (!defender.isOnGround()) {
                    if (ticks % 3 == 0) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_QUICK_WHOOSH2.get(), 1.0F, 0.7f);
                        defender.setDeltaMovement(defender.getDeltaMovement().add(0, -0.1, 0));
                    }
                }
            }
            if (ticks2 > 0 && ticks2 <= 60) {
                if (target != null) {
                    for (int i = 0; i < 5; i++) defender.fireProjectile(target, 1.0F, 6.0F);
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_sentryguns) {
            float f = defender.yBodyRot * ((float)Math.PI / 180F);
            float f1 = Mth.cos(f);
            float f2 = Mth.sin(f);

            double mult;
            Vec3 thereTo;

            if (ticks == 7) {
                defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                mult = -1.0d;
                thereTo = new Vec3(
                        defender.getX() + (double)f1 * mult,
                        defender.getY() + 1.5d,
                        defender.getZ() + (double)f2 * mult
                );
                throwSentry(defender, thereTo);
            }
            if (ticks == 13) {
                defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.6F);
                mult = 0.1d;
                thereTo = new Vec3(
                        defender.getX() + (double)f1 * mult,
                        defender.getY() + 3.5d,
                        defender.getZ() + (double)f2 * mult
                );
                throwSentry(defender, thereTo);
            }
            if (ticks == 19) {
                defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.7F);
                for (int i = 0; i < 2; i++) {
                    mult = 2.0d;
                    int whichDirection = i == 0 ? 1 : -1;
                    thereTo = new Vec3(
                            defender.getX() + (double)f1 * mult * whichDirection,
                            defender.getY() + 2.5d,
                            defender.getZ() + (double)f2 * mult * whichDirection
                    );
                    throwSentry(defender, thereTo);
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_creepergun) {
            if (target != null && ticks > 60) {
                if (ticks == 61) defender.setFreakOutInModel(true);
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_CREEPERGUN_SHOOT.get(), 2.5F, 1.0F);

                if (!defender.level.isClientSide) {
                    CreeperBulletEntity iGaveBirth = new CreeperBulletEntity(YEEntityTypes.CreeperBullet.get(), defender.level);
                    iGaveBirth.moveTo(defender.getPosition(0).add(0, 1.25D, 0));

                    iGaveBirth.setTarget(defender.getTarget());
                    iGaveBirth.setCollisionPos((int)target.getX(), (int)target.getEyeY(), (int)target.getZ());
                    iGaveBirth.wasShotFromDefender = true;
                    iGaveBirth.setShooter(defender);
                    iGaveBirth.setAnimationState("fly");
                    iGaveBirth.setShootY(defender.getYRot());
                    iGaveBirth.setShootX(defender.getXRot());

                    if (defender.level instanceof ServerLevel serverLevel) iGaveBirth.finalizeSpawn(serverLevel, defender.level.getCurrentDifficultyAt(defender.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);

                    if (defender.getTeam() != null) {
                        defender.level.getScoreboard().addPlayerToTeam(iGaveBirth.getStringUUID(),
                                defender.level.getScoreboard().getPlayerTeam(defender.getTeam().getName()));
                    }
                    defender.level.addFreshEntity(iGaveBirth);

                    List<CreeperBulletEntity> bullets = defender.level.getEntitiesOfClass(CreeperBulletEntity.class, target.getBoundingBox().inflate(30.0d));
                    if (bullets.size() > 200) {
                        boolean shouldContinue = true;
                        for (CreeperBulletEntity bullet : bullets) if (bullet.isPowered()) shouldContinue = false;

                        if (shouldContinue) {
                            CreeperBulletEntity chosenOne = bullets.get(new Random().nextInt(bullets.size()));
                            chosenOne.beTheChosenOne();
                        }
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_forcegun) {
            if (ticks == 10) {
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_FORCEGUN.get(), 3.0F, 1.0F);
                EntityUtil.makeCircleParticles(defender.level, defender.getPosition(0).add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, 35, 1.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 0.25F);
                EntityUtil.makeCircleParticles(defender.level, defender.getPosition(0).add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, 20, 0.25f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 1.0F);
                EntityUtil.makeCircleParticles(defender.level, defender.getPosition(0).add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, 30, 0.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 2.0F);
                EntityUtil.makeCircleParticles(defender.level, defender.getPosition(0).add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, 40, 0.75f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 5.0F);
                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.3f, 0, 20);

                float size = 8.0f;
                List<Entity> entities = EntityUtil.getEntitiesFromAABB(defender.level, size, defender, predicate -> (predicate != defender));
                Vec3 viewVec = defender.getLookAngle();
                for (Entity hit : entities) {
                    Vec3 hitVec = hit.position().subtract(defender.position()).normalize();
                    if (hitVec.length() > size) continue;
                    if (viewVec.dot(hitVec) > 0.5D) {
                        hit.hurtMarked = true;
                        if (hit instanceof LivingEntity living) living.setLastHurtByMob(defender);
                        hit.setDeltaMovement(hit.position().add(0, 0.5, 0).subtract(defender.position()).normalize().scale(4.0));
                        if (hit.isOnGround()) hit.setDeltaMovement(hit.getDeltaMovement().x, Math.abs(hit.getDeltaMovement().y), hit.getDeltaMovement().z);
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_poisondarts) {
            if (target != null) {
                if (ticks == 7 || ticks == 18) {
                    float radius2 = 1.1f;
                    double x = defender.getX() + 0.8F * Math.sin(-defender.getYRot() * Math.PI / 180) + radius2 * Math.sin(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double z = defender.getZ() + 0.8F * Math.cos(-defender.getYRot() * Math.PI / 180) + radius2 * Math.cos(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    for (int i = 0; i < 5; ++i) {
                        double y1 = defender.getY() + 1.3D;
                        float mult = 5.0f;

                        float $$4 = defender.yBodyRot * 0.017453292F;
                        float $$5 = Mth.cos($$4) * ((i - 2) * mult);
                        float $$6 = Mth.sin($$4) * ((i - 2) * mult);

                        double d0 = target.getEyeY();
                        double d1 = target.getX() + (double)$$5 - x;
                        double d2 = d0 - y1;
                        double d3 = target.getZ() + (double)$$6 - z;

                        DefenderArrowEntity arrow = defender.getArrow(1.0F);
                        arrow.setPos(x, y1, z);
                        arrow.setArrowType(1);
                        arrow.shoot(d1, d2, d3, 3.0F, 0.0F);

                        if (ticks == 7 && i != 2) {
                            defender.level.addFreshEntity(arrow);
                        }
                        if (ticks == 18 && i != 1  && i != 3) {
                            defender.level.addFreshEntity(arrow);
                        }
                    }
                }
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (defender.attackType == defender.attack_snipe) {
            if (ticks == 6) defender.playSound(YESoundEvents.ENTITY_DEFENDER_SNIPE_START.get(), 2.0F, 1.0F);
            if (ticks == 27) defender.level.playSound(null, defender, YESoundEvents.ENTITY_DEFENDER_SNIPE_SPIN.get(), defender.getSoundSource(), 2.0F, 1.0F);
            if (ticks > 30 && ticks < 60) {
                float spinSpeed = 3.0f;
                for (int i = 0; i < 2; ++i) {
                    double otherSide = i == 1 ? -1 : 1;
                    Vec3 shoot = defender.position().add(
                            Math.sin(defender.tickCount / spinSpeed) * 50 * otherSide,
                            0,
                            Math.cos(defender.tickCount / spinSpeed) * 50 * otherSide);
                    if (!defender.level.isClientSide) {
                        DeadlyArrowEntity deadlyArrow = new DeadlyArrowEntity(defender.level, defender, shoot);
                        defender.level.addFreshEntity(deadlyArrow);
                    }
                }
            }
            if (ticks == 36) {
                if (!defender.level.isClientSide) {
                    defender.setMaxWobble(20);
                }
                defender.setCustomRender(2);
            }
            if (ticks == 80) {
                defender.setCustomRender(0);
            }
            if (ticks == 91) {
                defender.setDeltaMovement(0, 1.0, 0);
                defender.playSound(YESoundEvents.ENTITY_DEFENDER_JUMP.get(), 2.0F, 1.0F);
            }
            Vec3 throwTo = defender.getLookAngle().scale(10.0d);
            if (ticks >= 91 && target != null) {
                List<Mob> targets = defender.level.getEntitiesOfClass(Mob.class, defender.getBoundingBox().inflate(30.0f), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && p instanceof Enemy && defender.isInAttackSight(p));
                Vec3 strikeZone = EntityUtil.findDensestMobCluster(targets, 9.0d);

                throwTo = target.getBoundingBox().getCenter();
                if (strikeZone != null && !(target instanceof Player)) {
                    throwTo = strikeZone;
                }
                defender.setSpecialLookLocation(throwTo);
            }
            if (ticks == 102) {
                defender.playSound(SoundEvents.TRIDENT_THROW, 2.0F, 1.0F);
                if (!defender.level.isClientSide) {
                    SniperRifleEntity sniperRifle = new SniperRifleEntity(defender.level, defender, throwTo);
                    defender.level.addFreshEntity(sniperRifle);
                }
                defender.setDeltaMovement(defender.getLookAngle().scale(-1));
            }
        }
    }

    protected static void throwSentry(DefenderEntity defender, Vec3 whereTo) {
        if (!defender.level.isClientSide) {
            SentryGunEntity iGaveBirth = new SentryGunEntity(YEEntityTypes.SentryGun.get(), defender.level);
            iGaveBirth.moveTo(defender.getPosition(0).add(0, 0.5, 0));

            double mult = 1.0d;
            Vec3 motion = new Vec3(whereTo.x(), whereTo.y(), whereTo.z()).subtract(iGaveBirth.position());
            Vec3 motionSquared = motion.multiply(motion);
            double sqrt = Math.sqrt(motionSquared.x + motionSquared.y + motionSquared.z) * (double)0.2F;
            iGaveBirth.setDeltaMovement(
                    ((motion.x * sqrt) * mult),
                    ((motion.y * sqrt) * mult),
                    ((motion.z * sqrt) * mult)
            );
            iGaveBirth.setOwner(defender);

            iGaveBirth.setTarget(defender.getTarget());

            if (defender.level instanceof ServerLevel serverLevel) iGaveBirth.finalizeSpawn(serverLevel, defender.level.getCurrentDifficultyAt(defender.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);

            if (defender.getTeam() != null) {
                defender.level.getScoreboard().addPlayerToTeam(iGaveBirth.getStringUUID(),
                        defender.level.getScoreboard().getPlayerTeam(defender.getTeam().getName()));
            }
            defender.level.addFreshEntity(iGaveBirth);
        }
    }
}
