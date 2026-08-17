package com.yellowbrossproductions.yellowbrossextras.util.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGun;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.*;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Chainsaw;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.world.CustomExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AttacksPart1 {
    public static void tickPhase1Attacks(Defender defender) {
        if (!defender.isRemoved()) {
            if (defender.isAlive()) {
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
                            DefenderAxe axe = new DefenderAxe(defender.level, defender, target.getBoundingBox().getCenter().subtract(defender.position().add(0, 1, 0)));
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
                            Boomerang projectile = new Boomerang(YEEntityTypes.Boomerang.get(), defender.level);
                            projectile.moveTo(defender.position().add(0, 1, 0));

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
                            CameraShake.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 15);
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
                                Shuriken shuriken = new Shuriken(defender.level, defender);

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
                        Chainsaw beam = null;
                        if (ticks == 30) {
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_CHAINSAW.get(), 2.0F, 1.0F);
                            if (!defender.level.isClientSide) {
                                beam = new Chainsaw(YEEntityTypes.Chainsaw.get(), defender.level, defender, defender.getX(), defender.getY() + 1.125, defender.getZ(), (float) ((defender.yHeadRot + 90) * Math.PI / 180), (float) (-defender.getXRot() * Math.PI / 180), 66);
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
                                        CameraShake.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 4);
                                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch());
                                        defender.clawsTarget.invulnerableTime = 0;
                                        defender.clawsTarget.hurtTime = 3;
                                    }
                                }
                                if (defender.jumpTicks == (21 - 12) || defender.jumpTicks == (21 - 17)) {
                                    if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 5.0F)) {
                                        CameraShake.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 4);
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
                                        CameraShake.cameraShake(defender.level, defender.position(), 30, 0.2f, 0, 15);
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
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (defender.deathAttackTicks > 0) {
                defender.setDeltaMovement(0.0D, defender.getDeltaMovement().y, 0.0D);
                defender.setXRot(0.0F);

                if (defender.deathAttackTicks == 10) {
                    defender.setWeaponToShow(8);
                }

                if (defender.deathAttackTicks == 20 + 1) {
                    defender.stareYOffsetter = 4.1;
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.8F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.5F);
                    defender.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.7F);
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.4f, 0, 15);
                    EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 0.3, 0), ParticleTypes.POOF, false, 50, 1.0F, Vec3.ZERO, 0.0F);

                    for (LivingEntity entity : defender.level.getEntitiesOfClass(LivingEntity.class, defender.getBoundingBox().inflate(15.0F), p -> p.isAlive() && EntityUtil.canHurtThisMob(p, defender))) {
                        double x = defender.getX() - entity.getX();
                        double y = defender.getY() - entity.getY();
                        double z = defender.getZ() - entity.getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        if (defender.distanceTo(entity) < 3.0D) {
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_BOOMERANG_HIT.get(), 2.0F, defender.getVoicePitch());
                            entity.hurt(DamageSource.mobAttack(defender), 30.0F);
                            entity.hurtMarked = true;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 2.5D, (-y / d * 0.4D) + 0.8D, -z / d * 2.5D));
                        }
                    }
                }
                if (defender.deathAttackTicks == 35 + 1) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 1.2F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.7F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_SPIKE.get(), 3.0F, 0.7F);
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.2f, 0, 15);

                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(defender.level);
                    assert lightning != null;
                    lightning.setPos(defender.getX(), defender.getY() + 2.7D, defender.getZ());
                    lightning.setVisualOnly(true);
                    defender.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT, 3.0F, 1.0F);
                    defender.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 10000.0F, 1.0F);
                    defender.level.addFreshEntity(lightning);
                }
                if (defender.deathAttackTicks >= 35 + 1 && defender.deathAttackTicks < 45 + 1) {
                    defender.makeExcaliburLandParticles();
                }

                if (defender.deathAttackTicks >= 59 + 1 && defender.deathAttackTicks < 81 + 1) {
                    defender.stareYOffsetter += 0.05D;
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_EARTH_RUMBLE.get(), 3.0F, defender.getVoicePitch());
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.02f, 0, 15);
                }

                if (defender.deathAttackTicks == 81 + 1) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 1.2F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.7F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_SPIKE.get(), 3.0F, 0.5F);
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.3f, 0, 15);
                }

                if (defender.deathAttackTicks == 102 + 1) {
                    defender.stareYOffsetter = -0.4;
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.8F);
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.5F);
                    defender.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.0F);
                    defender.playSound(YESoundEvents.HUGE_SLAM.get(), 4.0F, 1.0F);
                    defender.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.7F);
                    CameraShake.cameraShake(defender.level, defender.position(), 30, 0.4f, 0, 15);
                    EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 0.3, 0), ParticleTypes.POOF, false, 50, 1.0F, Vec3.ZERO, 0.0F);
                    defender.makeRockExplodeParticles();

                    if (!defender.level.isClientSide) {
                        defender.level.explode(defender, defender.getX(), defender.getY() + 0.3, defender.getZ(), 6.0F, Explosion.BlockInteraction.NONE);
                        defender.level.explode(defender, defender.getX(), defender.getY() + 0.3, defender.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                        defender.level.explode(defender, defender.getX(), defender.getY() + 0.3, defender.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                    }
                }

                if (defender.deathAttackTicks == 114 + 1) {
                    defender.stareYOffsetter = 0.0;
                }

                if (defender.deathAttackTicks >= 125 + 1) {
                    defender.setHealth(defender.getHealth() + 1.0F);
                    defender.setHealthIFrames = 10;
                }

                if (defender.deathAttackTicks >= 125 + 1 && defender.deathAttackTicks < 640 + 1) {
                    defender.setDiscardFriction(true);

                    if ((defender.deathAttackTicks - (125 + 1)) % 4 == 0) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_QUICK_WHOOSH.get(), 3.0F, 1.0F);
                        CameraShake.cameraShake(defender.level, defender.position(), 30, 0.2f, 0, 8);
                    }

                    LivingEntity t = null;
                    List<Mob> list = defender.level.getEntitiesOfClass(Mob.class, defender.getBoundingBox().inflate(50.0D), p -> {
                        return p instanceof Enemy && EntityUtil.canHurtThisMob(p, defender) && ((p.getBlockStateOn() != Blocks.AIR.defaultBlockState())) && defender.isInAttackSight(p);
                    });
                    if (defender.getTarget() != null && (list.isEmpty() || defender.distanceTo(defender.getTarget()) < 25.0D)) {
                        t = defender.getTarget();
                    } else {
                        if (!list.isEmpty()) {
                            t = list.get(0);
                        }
                    }
                    if (t != null && (!(t instanceof Player) || ((defender.deathAttackTicks == 125 + 1) || (defender.distanceTo(t) > 30.0D)))) {
                        double chargex = defender.getX() - t.getX();
                        double chargey = defender.getY() - (t.getY() + t.getEyeHeight());
                        double chargez = defender.getZ() - t.getZ();
                        double charged = Math.sqrt(chargex * chargex + chargey * chargey + chargez * chargez);
                        float power = (float) 1.0F;
                        double motionX = -(chargex / charged * (double) power);
                        double motionY = -(chargey / charged * (double) power);
                        double motionZ = -(chargez / charged * (double) power);
                        defender.setCharge(motionX, motionY, motionZ);
                    }
                    defender.setDeltaMovement(defender.getDeltaMovement().multiply(1, 0, 1).add(defender.chargeX, defender.chargeY, defender.chargeZ));

                    for (LivingEntity entity : defender.level.getEntitiesOfClass(LivingEntity.class, defender.getBoundingBox().inflate(100.0F), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && EntityUtil.isMobNotInCreativeMode(p))) {
                        if (!(entity instanceof Player)) {
                            double x = defender.getX() - entity.getX();
                            double y = defender.getY() - entity.getY();
                            double z = defender.getZ() - entity.getZ();
                            double d = Math.sqrt(x * x + y * y + z * z);

                            double distance = defender.distanceTo(entity) * 0.1D;

                            if (defender.distanceTo(entity) < 40.0D && defender.distanceTo(entity) > 5.0D && entity.invulnerableTime == 0) {
                                if (entity.fallDistance > 10) {
                                    entity.fallDistance = 10;
                                }
                                entity.hurtMarked = true;
                                entity.setDeltaMovement(entity.getDeltaMovement().add(
                                        (x / d * 0.5D) / distance,
                                        (y / d * 1.0D) / distance,
                                        (z / d * 0.5D) / distance));
                            }
                        }
                    }

                    for (LivingEntity entity : defender.level.getEntitiesOfClass(LivingEntity.class, defender.getBoundingBox().inflate(15.0F), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && EntityUtil.isMobNotInCreativeMode(p))) {
                        double x = defender.getX() - entity.getX();
                        double y = defender.getY() - entity.getY();
                        double z = defender.getZ() - entity.getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        if (defender.distanceTo(entity) < 5.0D) {
                            if (entity.hurt(DamageSource.mobAttack(defender).bypassArmor(), 25.0F * EntityUtil.multiplyToScrewArmor((LivingEntity) entity, 0.25f))) {
                                for (int i = 0; i < 4; ++i) entity.hurt(DamageSource.mobAttack(defender).bypassArmor(), 25.0F * EntityUtil.multiplyToScrewArmor((LivingEntity) entity, 0.25f));
                                defender.playSound(YESoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch() - 0.2F);
                                CameraShake.cameraShake(defender.level, defender.position(), 10, 0.2f, 0, 8);
                                entity.hurtMarked = true;
                                if (defender.distanceTo(entity) < 2.0D) {
                                    entity.setDeltaMovement(entity.getDeltaMovement().add(
                                            (-0.5D + defender.getRandom().nextDouble()) * 3.0D,
                                            (-0.5D + defender.getRandom().nextDouble()) * 3.0D,
                                            (-0.5D + defender.getRandom().nextDouble()) * 3.0D));
                                } else {
                                    entity.setDeltaMovement(entity.getDeltaMovement().add(
                                            (x / d * 3.0D),
                                            (y / d * 3.0D),
                                            (z / d * 3.0D)));
                                }
                            }
                        }
                    }
                }
                if (defender.deathAttackTicks == 640 + 1) defender.setDiscardFriction(false);

                if (defender.deathAttackTicks >= 648 + 1) {
                    defender.setSecondHat(defender.getPhaseLimit() > 1 ? 2 : 0);
                }

                if (defender.deathAttackTicks == 650 + 1) {
                    defender.setDeltaMovement(0.0D, -3.0D, 0.0D);
                }

                if (defender.deathAttackTicks >= 672 + 1) {
                    defender.deathAttackTicks = 0;
                    defender.attackType = 0;
                    defender.setPhase(defender.getPhaseLimit() > 1 ? 2 : 0);
                    defender.setSecondHat(0);
                    defender.frame = 0;
                }
            }
        }
    }

    public static void tickPhase2Attacks(Defender defender) {
        if (!defender.isRemoved()) {
            LivingEntity target = defender.tryToFindTarget();

            if (defender.isAlive()) {
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
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 0.3, 0), ParticleTypes.POOF, false, 10, 1.0f, Vec3.ZERO, 0.0F);
                    }
                    if (ticks > 30) {
                        if (defender.isOnGround() && ticks2 == 0) {
                            defender.setDiscardFriction(false);
                            defender.jumpAttacking = true;
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
                            CreeperBullet iGaveBirth = new CreeperBullet(YEEntityTypes.CreeperBullet.get(), defender.level);
                            iGaveBirth.moveTo(defender.position().add(0, 1.25D, 0));

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

                            List<CreeperBullet> bullets = defender.level.getEntitiesOfClass(CreeperBullet.class, target.getBoundingBox().inflate(30.0d));
                            if (bullets.size() > 200) {
                                boolean shouldContinue = true;
                                for (CreeperBullet bullet : bullets) if (bullet.isPowered()) shouldContinue = false;

                                if (shouldContinue) {
                                    CreeperBullet chosenOne = bullets.get(new Random().nextInt(bullets.size()));
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
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, false, 35, 1.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 0.25F);
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, false, 20, 0.25f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 1.0F);
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, false, 30, 0.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 2.0F);
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0).add(defender.getLookAngle().scale(1.0)), ParticleTypes.POOF, false, 40, 0.75f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 5.0F);
                        CameraShake.cameraShake(defender.level, defender.position(), 30, 0.3f, 0, 20);

                        float size = 8.0f;
                        List<Entity> entities = EntityUtil.getEntitiesFromAABB(defender.level, size, defender, predicate -> (predicate != defender));
                        Vec3 viewVec = defender.getLookAngle();
                        for (Entity hit : entities) {
                            Vec3 hitVec = hit.position().subtract(defender.position()).normalize();
                            if (hitVec.length() > size) continue;
                            if (viewVec.dot(hitVec) > 0.5D && EntityUtil.isMobNotInCreativeMode(hit)) {
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

                                DefenderArrow arrow = defender.getArrow(1.0F);
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
                                DeadlyArrow deadlyArrow = new DeadlyArrow(defender.level, defender, 1.25d, shoot, 20);
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
                    Vec3 throwTo = defender.position().add(defender.getLookAngle().scale(10.0d));
                    if (ticks >= 91 && target != null) {
                        List<Mob> targets = defender.level.getEntitiesOfClass(Mob.class, target.getBoundingBox().inflate(30.0f), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && p instanceof Enemy && defender.isInAttackSight(p));
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
                            SniperRifle sniperRifle = new SniperRifle(defender.level, defender, throwTo);
                            defender.level.addFreshEntity(sniperRifle);
                        }
                        defender.setDeltaMovement(defender.getLookAngle().scale(-1));
                    }
                }
                // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (defender.attackType == defender.attack_witherbazooka) {
                    Vec3 throwTo = defender.getLookAngle().scale(10.0d);
                    if (ticks <= 24 && target != null) {
                        List<Mob> targets = defender.level.getEntitiesOfClass(Mob.class, target.getBoundingBox().inflate(30.0f), p -> EntityUtil.canHurtThisMob(p, defender) && p.isAlive() && p instanceof Enemy && defender.isInAttackSight(p));
                        Vec3 strikeZone = EntityUtil.findDensestMobCluster(targets, 15.0d);

                        throwTo = target.getBoundingBox().getCenter();
                        if (strikeZone != null && !(target instanceof Player)) {
                            throwTo = strikeZone;
                        }
                        defender.setSpecialLookLocation(throwTo);
                    }
                    if (ticks == 38) {
                        defender.setDeltaMovement(defender.getLookAngle().scale(-2).multiply(1, 0, 1).add(0, 0.6, 0));
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_SHOOT.get(), 3.0F, 1.0F);
                        CameraShake.cameraShake(defender.level, defender.position(), 20, 0.05f, 0, 10);
                        defender.setDiscardFriction(true);

                        if (!defender.level.isClientSide) {
                            SkullOfDoom skull = new SkullOfDoom(defender.level, defender, throwTo);
                            skull.setPos(defender.position().add(0, 1.5, 0));
                            defender.level.addFreshEntity(skull);
                        }
                    }
                    if (ticks > 38) {
                        if (defender.horizontalCollision || defender.isInWater()) {
                            CustomExplosion.create(defender, defender.position().add(0, 0.3, 0), 4.0F, true, false);
                            defender.setDiscardFriction(false);
                            defender.attackTicks2 = 9999;
                        } else if (!defender.getAnimationState().equals("witherbazooka_land") && defender.isOnGround()) {
                            defender.setAnimationState("witherbazooka_land");
                            defender.attackTicks2 = 1;
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_END.get(), 2.0F, 1.0F);
                            defender.setDiscardFriction(false);
                        }
                    }
                }
                // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (defender.attackType == defender.attack_icethrower) {
                    float f;
                    float f1;
                    float f2;

                    double mult;
                    Vec3 thereTo;
                    if (ticks >= 13 && ticks <= 23) {
                        f = (float)(Mth.wrapDegrees(defender.yBodyRot + 25.0f) * (Math.PI / 180F));
                        f1 = Mth.cos(f);
                        f2 = Mth.sin(f);

                        mult = 1.6d;
                        thereTo = new Vec3(
                                defender.getX() + (double)f1 * mult,
                                defender.getY() + 2.4d,
                                defender.getZ() + (double)f2 * mult
                        );

                        int x = target != null ? target.getBlockX() : defender.getBlockX();
                        int z = target != null ? target.getBlockZ() : defender.getBlockZ();

                        BlockPos blockPos = new BlockPos(x + ((-14 + defender.getRandom().nextInt(28)) * 4), 0,
                                z + ((-14 + defender.getRandom().nextInt(28)) * 4));

                        Icicle icicle = new Icicle(defender.level, defender, thereTo, blockPos);
                        icicle.setTimer(80 + (defender.getRandom().nextInt(8) * 20));
                        defender.level.addFreshEntity(icicle);
                    }
                    if (ticks > 23 && ticks <= 33) {
                        f = (float)(Mth.wrapDegrees(defender.yBodyRot + 195.0f) * (Math.PI / 180F));
                        f1 = Mth.cos(f);
                        f2 = Mth.sin(f);

                        mult = 0.75d;
                        thereTo = new Vec3(
                                defender.getX() + (double)f1 * mult,
                                defender.getY() + 2.7d,
                                defender.getZ() + (double)f2 * mult
                        );

                        int x = target != null ? target.getBlockX() : defender.getBlockX();
                        int z = target != null ? target.getBlockZ() : defender.getBlockZ();

                        BlockPos blockPos = new BlockPos(x + ((-7 + defender.getRandom().nextInt(14)) * 3), 0,
                                z + ((-7 + defender.getRandom().nextInt(14)) * 3));

                        Icicle icicle = new Icicle(defender.level, defender, thereTo, blockPos);
                        if (target != null) icicle.setDelayAndTarget(ticks - 23, target);
                        icicle.setTimer(30);
                        defender.level.addFreshEntity(icicle);
                    }
                    if (ticks == 37) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_ICETHROWER_MURDERHEADPHONEUSERS.get(), 3.0F, 1.0F);
                        CameraShake.cameraShake(defender.level, defender.position(), 50, 0.15f, 8, 5);
                    }
                    if (ticks >= 37 && ticks <= 47) {
                        for (int i = 0; i < 20; ++i) {
                            f = (float)(Mth.wrapDegrees(defender.yBodyRot + defender.getRandom().nextInt(360)) * (Math.PI / 180F));
                            f1 = Mth.cos(f);
                            f2 = Mth.sin(f);

                            mult = 1.2d;
                            thereTo = new Vec3(
                                    defender.getX() + (double)f1 * mult,
                                    defender.getY() + 2.5d,
                                    defender.getZ() + (double)f2 * mult
                            );

                            int x = target != null ? target.getBlockX() : defender.getBlockX();
                            int z = target != null ? target.getBlockZ() : defender.getBlockZ();

                            BlockPos blockPos = new BlockPos(x + ((-16 + defender.getRandom().nextInt(32)) * 3), 0,
                                    z + ((-16 + defender.getRandom().nextInt(32)) * 3));

                            Icicle icicle = new Icicle(defender.level, defender, thereTo, blockPos);
                            defender.level.addFreshEntity(icicle);
                        }
                    }
                }
            }
            // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (defender.deathAttackTicks > 0) {
                float f = defender.yBodyRot * ((float)Math.PI / 180F);
                float f1 = Mth.cos(f);
                float f2 = Mth.sin(f);

                if (defender.deathAttackTicks < 120) defender.setXRot(0.0F);
                defender.stareYOffsetter = defender.getEyeHeight();
                if (defender.deathAttackTicks == 25) {
                    defender.playSound(SoundEvents.FIRECHARGE_USE, 2.0F, 1.0F);
                    defender.playSound(SoundEvents.FIRECHARGE_USE, 2.0F, 0.8F);
                    defender.playSound(SoundEvents.FIRECHARGE_USE, 2.0F, 0.7F);
                    defender.playSound(SoundEvents.FIRECHARGE_USE, 2.0F, 0.6F);
                }
                if (defender.deathAttackTicks >= 25 && defender.deathAttackTicks < 32) {
                    float f3 = (defender.yBodyRot + 32.5F) * ((float)Math.PI / 180F);
                    float f4 = Mth.cos(f3);
                    float f5 = Mth.sin(f3);

                    double mult = 0.7d;
                    Vec3 thereTo = new Vec3(
                            defender.getX() + (double)f4 * mult,
                            defender.getY() + 1.4d,
                            defender.getZ() + (double)f5 * mult
                    );

                    for (int i = 0; i < 20; ++i) {
                        EntityUtil.makeAParticle(defender.level, ParticleTypes.FLAME, true, thereTo.add(defender.getLookAngle().multiply(1, 0, 1).scale(0.5)).add(((-0.5d + defender.getRandom().nextDouble()) * 0.6d), 0, ((-0.5d + defender.getRandom().nextDouble()) * 0.6d)), new Vec3(0, 0.25 + defender.getRandom().nextFloat(), 0));
                    }
                }
                if (defender.deathAttackTicks == 32) defender.setWeaponToShow(16);
                if (defender.deathAttackTicks == 43 || defender.deathAttackTicks == 52) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_FLAMETHROWER_WING.get(), 3.0F, 1.0F);
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.2f, 0, 10);
                }

                if (defender.deathAttackTicks >= 70 && defender.deathAttackTicks <= 86) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_EARTH_RUMBLE.get(), 3.0F, defender.getVoicePitch());
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.02f, 0, 15);
                }
                if (defender.deathAttackTicks == 87) {
                    defender.playSound(YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_SHOOT.get(), 4.0F, defender.getVoicePitch());
                    CameraShake.cameraShake(defender.level, defender.position(), 50, 0.1f, 0, 20);
                    for (int i = 0; i < 5; i++) {
                        double mult = i - 2;
                        Vec3 spawnAt = new Vec3(f1 * mult, -0.5d, f2 * mult);

                        for (int i1 = 0; i1 < 30; i1++) EntityUtil.makeAParticle(defender.level, ParticleTypes.LARGE_SMOKE, true, defender.position().add(0, 0.5, 0).add(spawnAt).add(defender.getLookAngle().scale(-1).multiply(1, 0, 1)), new Vec3(0, (-0.5f + defender.getRandom().nextFloat() + defender.getRandom().nextFloat()), 0));
                    }
                }
                if (defender.deathAttackTicks == 88) defender.setInvisible(true);

                if (defender.deathAttackTicks <= 120 && target != null) defender.lookAtWhileDead(target, 100.0F, 0.0F);

                if (defender.deathAttackTicks > 120 && defender.deathAttackTicks < 605) {
                    defender.setHealth(defender.getHealth() + 1.0F);
                    defender.setHealthIFrames = 10;

                    defender.clearFire();
                    defender.setDeltaMovement(0, target != null ? 0.01 : -1, 0);

                    List<Integer> bigBallTimes = List.of(144, 272, 304, 336, 368, 400);
                    List<Integer> bigBallTeleport = bigBallTimes.stream().map(time -> time - 5).toList();
                    List<Integer> bigBallHandleEffects = bigBallTimes.stream().map(time -> time + 19).toList();
                    List<Integer> bigBallFinallyDisappear = bigBallTimes.stream().map(time -> time + 21).toList();

                    List<Integer> rowTimes = List.of(176, 432);
                    List<Integer> rowTeleport = rowTimes.stream().map(time -> time - 5).toList();
                    List<Integer> rowDisappear = rowTimes.stream().map(time -> time + 64).toList();

                    // Big Ball
                    if (bigBallTeleport.contains(defender.deathAttackTicks)) {
                        double multiplier = -20.0d * (defender.deathAttackTicks == bigBallTeleport.get(3) || defender.deathAttackTicks == bigBallTeleport.get(5) ? -1 : 1);
                        double y = defender.getY();
                        Vec3 teleportLocation = defender.position().subtract(defender.getLookAngle().scale(multiplier));
                        if (target != null) {
                            teleportLocation = target.position().add(defender.getLookAngle().scale(multiplier));
                            y = target.getY();
                        }

                        if (!defender.level.isClientSide) {
                            defender.hasImpulse = true;
                            defender.teleportTo(teleportLocation.x, y + 6, teleportLocation.z);
                        }
                    }
                    if (bigBallTimes.contains(defender.deathAttackTicks)) {
                        defender.setInvisible(false);

                        Vec3 throwTo = defender.getLookAngle().scale(10.0d).subtract(0, 20, 0);
                        defender.setSpecialLookLocation(defender.position().add(throwTo));
                        if (target != null) {
                            throwTo = target.position().subtract(defender.position()).normalize().scale(0.06f);
                            defender.setSpecialLookLocation(target.position());
                        }

                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_FLAMETHROWER_WING.get(), 5.0F, 1.0F);

                        defender.setAnimationState("flamethrower_big");

                        if (!defender.level.isClientSide) {
                            Fireball fireball = new Fireball(defender.level, defender, defender.position().add(0, 8, 0), throwTo);
                            fireball.setShouldFlash(true);
                            fireball.setSize(3);
                            fireball.defenderYeet = true;
                            fireball.wasHit = true;
                            defender.level.addFreshEntity(fireball);
                        }
                    }
                    if (bigBallHandleEffects.contains(defender.deathAttackTicks)) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_FLAMETHROWER_WING.get(), 5.0F, 1.5F);
                        for (int i = 0; i < 50; i++) {
                            EntityUtil.makeAParticle(defender.level, ParticleTypes.FLAME, true, defender.getBoundingBox().getCenter(), new Vec3(0, -0.5f + defender.getRandom().nextFloat(), 0));
                        }
                        EntityUtil.makeAParticle(defender.level, ParticleTypes.EXPLOSION, true, defender.getBoundingBox().getCenter(), Vec3.ZERO);
                    }
                    if (bigBallFinallyDisappear.contains(defender.deathAttackTicks)) {
                        defender.setAnimationState("none");
                        defender.setSpecialLookLocation(Vec3.ZERO);
                        defender.setPos(defender.position().subtract(0, 6, 0));
                        defender.setInvisible(true);
                    }

                    // Row
                    if (rowTeleport.contains(defender.deathAttackTicks)) {
                        double travelDistance = 100;
                        double y = target != null ? target.getY() : defender.getY();
                        defender.setForcedDirection(defender.getRandom().nextInt(4) + 1);
                        Vec3 teleportLocation = target != null ? target.position() : defender.position();
                        switch (defender.getForcedDirection()) {
                            case 1: {
                                // East
                                teleportLocation = teleportLocation.subtract(travelDistance / 2, 0, 0);
                                defender.setCharge(travelDistance / 64, 0, 0);
                                break;
                            }
                            case 2: {
                                // South
                                teleportLocation = teleportLocation.subtract(0, 0, travelDistance / 2);
                                defender.setCharge(0, 0, travelDistance / 64);
                                break;
                            }
                            case 3: {
                                // West
                                teleportLocation = teleportLocation.add(travelDistance / 2, 0, 0);
                                defender.setCharge(-travelDistance / 64, 0, 0);
                                break;
                            }
                            case 4: {
                                // North
                                teleportLocation = teleportLocation.add(0, 0, travelDistance / 2);
                                defender.setCharge(0, 0, -travelDistance / 64);
                                break;
                            }
                        }

                        if (!defender.level.isClientSide) {
                            defender.hasImpulse = true;
                            defender.teleportTo(teleportLocation.x, y + 10, teleportLocation.z);
                            defender.setDiscardFriction(true);
                        }
                    }
                    if (rowTimes.contains(defender.deathAttackTicks)) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_FLAMETHROWER_WING.get(), 5.0F, 1.0F);
                        defender.setAnimationState("flamethrower_row");

                        defender.setInvisible(false);

                        EntityUtil.makeAParticle(defender.level, ParticleTypes.EXPLOSION, true, defender.getBoundingBox().getCenter(), Vec3.ZERO);
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0), ParticleTypes.FLAME, true, 60, 1.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 0.25F);
                    }
                    if (defender.getAnimationState().equals("flamethrower_row")) {
                        defender.setDeltaMovement(defender.chargeX, 0, defender.chargeZ);

                        if (defender.deathAttackTicks % 5 == 0) {
                            defender.playSound(YESoundEvents.AIMBOT_SHOOT.get(), 3.0F, 1.0F);
                            if (!defender.level.isClientSide) {
                                for (int i = 0; i < 2; i++) {
                                    double mult = 2.0d;
                                    int whichDirection = i == 0 ? 1 : -1;
                                    Vec3 throwTo = new Vec3(f1 * mult * whichDirection, -0.5d, f2 * mult * whichDirection);

                                    Fireball fireball = new Fireball(defender.level, defender, defender.position().add(throwTo), throwTo);
                                    fireball.setSize(2);
                                    defender.level.addFreshEntity(fireball);
                                }
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            double mult = i - 2;
                            Vec3 spawnAt = new Vec3(f1 * mult, -0.5d, f2 * mult);

                            EntityUtil.makeAParticle(defender.level, ParticleTypes.LARGE_SMOKE, true, defender.position().add(spawnAt.add(0, 1.5, 0)).add(defender.getLookAngle().scale(-2).multiply(1, 0, 1)), defender.getLookAngle().scale(-1).multiply(1, 0, 1));
                            if (!defender.level.isClientSide) {
                                Fireball fireball = new Fireball(defender.level, defender, defender.position().add(spawnAt), new Vec3(0, -0.01, 0));
                                fireball.setSize(1);
                                fireball.wasHit = true;
                                defender.level.addFreshEntity(fireball);
                            }
                        }
                    }
                    if (rowDisappear.contains(defender.deathAttackTicks) || (defender.getAnimationState().equals("flamethrower_row") && defender.horizontalCollision)) {
                        defender.playSound(YESoundEvents.ENTITY_DEFENDER_FLAMETHROWER_WING.get(), 3.0F, 1.5F);
                        EntityUtil.makeAParticle(defender.level, ParticleTypes.EXPLOSION, true, defender.getBoundingBox().getCenter(), Vec3.ZERO);
                        EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 1.5, 0), ParticleTypes.FLAME, true, 60, 1.5f, new Vec3(90.0f, -defender.getYRot(), 0.0f), 0.25F);

                        defender.setAnimationState("none");
                        defender.setDiscardFriction(false);
                        defender.setCharge(Vec3.ZERO);
                        defender.setForcedDirection(0);
                        if (target != null) defender.setPos(target.position().add(0, 3, 0));
                        defender.setInvisible(true);
                    }
                }
                if (defender.deathAttackTicks >= 605) {
                    defender.setDeltaMovement(0, -1, 0);
                    if (target != null) defender.setSpecialLookLocation(target.position().add(0, target.getEyeHeight(), 0));
                }
                if (defender.deathAttackTicks == 605) {
                    Vec3 landingPosition = target != null ? target.position().add(target.getLookAngle().scale(10.0D).multiply(1, 0, 1)) : defender.position();
                    BlockPos.MutableBlockPos yPosition = new BlockPos.MutableBlockPos(landingPosition.x, landingPosition.y, landingPosition.z);
                    while (yPosition.getY() > defender.level.getMinBuildHeight() && !defender.level.getBlockState(yPosition).getMaterial().blocksMotion()) {
                        yPosition.move(Direction.DOWN);
                    }

                    defender.setPos(landingPosition.x, yPosition.getY() + 1, landingPosition.z);
                    defender.setInvisible(false);
                    defender.setAnimationState("flamethrower_end");
                }
                if (defender.deathAttackTicks == 607) {
                    defender.playSound(YESoundEvents.HUGE_SLAM.get(), 4.0F, 1.0F);
                    CameraShake.cameraShake(defender.level, defender.position(), 80, 0.2f, 0, 30);
                    EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 0.3, 0), ParticleTypes.FLAME, false, 50, 0.7F, Vec3.ZERO, 0.0F);
                    EntityUtil.makeCircleParticles(defender.level, defender.position().add(0, 0.3, 0), ParticleTypes.LARGE_SMOKE, false, 70, 1.0F, Vec3.ZERO, 0.0F);
                    defender.setSecondHat(defender.getPhaseLimit() > 2 ? 3 : 0);

                    for (LivingEntity entity : defender.level.getEntitiesOfClass(LivingEntity.class, defender.getBoundingBox().inflate(15.0F), p -> p.isAlive() && EntityUtil.canHurtThisMob(p, defender))) {
                        double x = defender.getX() - entity.getX();
                        double y = defender.getY() - entity.getY();
                        double z = defender.getZ() - entity.getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        if (defender.distanceTo(entity) < 3.0D) {
                            defender.playSound(YESoundEvents.ENTITY_DEFENDER_BOOMERANG_HIT.get(), 2.0F, defender.getVoicePitch());
                            entity.hurt(DamageSource.mobAttack(defender), 30.0F);
                            entity.hurtMarked = true;
                            entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 2.5D, (-y / d * 0.4D) + 0.8D, -z / d * 2.5D));
                        }
                    }
                }

                if (defender.deathAttackTicks == 640) {
                    defender.deathAttackTicks = 0;
                    defender.attackType = 0;
                    defender.setPhase(defender.getPhaseLimit() > 2 ? 3 : 0);
                    defender.setSecondHat(0);
                    defender.frame = 0;
                    defender.setSpecialLookLocation(Vec3.ZERO);
                }
            }
        }
    }

    protected static void throwSentry(Defender defender, Vec3 whereTo) {
        if (!defender.level.isClientSide) {
            SentryGun iGaveBirth = new SentryGun(YEEntityTypes.SentryGun.get(), defender.level);
            iGaveBirth.moveTo(defender.position().add(0, 0.5, 0));

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
