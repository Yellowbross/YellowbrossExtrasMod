package com.yellowbrossproductions.yellowbrossextras.util.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.BoomerangEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.ChainsawEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.DefenderAxeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.ShurikenEntity;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EffectRegisterer;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AttacksPart1 {
    public static void tickPhase1Attacks(DefenderEntity defender) {
        LivingEntity target = defender.tryToFindTarget();
        if (target != null) {
            if (defender.attackType == defender.SAWS_ATTACK) {
                if (defender.attackTicks == 7) {
                    defender.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.8F);
                    defender.makeExplodeParticles();
                    defender.setWeaponToShow(1);
                }
                if (defender.attackTicks == 15) {
                    defender.level.playSound(null, defender, YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SAW_START.get(), defender.getSoundSource(), 2.0F, 1.0F);
                }
                if (defender.attackTicks >= 22 && defender.attackTicks <= 82) {
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
                    if (t != null && (!(t instanceof Player) || (((defender.attackTicks - 22) % 30 == 0 && (defender.distanceTo(t) > 10.0D)) || defender.attackTicks == 22))) {
                        float power = (float) 5.5F;
                        defender.setCharge(t.position().subtract(defender.position()).normalize().scale(power).scale(0.2d));
                    }
                    defender.setDeltaMovement(defender.chargeX, defender.getDeltaMovement().y, defender.chargeZ);
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
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SAW.get(), 1.0F, 1.0F);
                                entity.hurt(DamageSource.mobAttack(defender).bypassArmor(), 10.0F);
                                entity.hurtMarked = true;
                                entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 0.4D, (-y / d * 0.4D) + 0.2D, -z / d * 0.4D));
                                defender.makeSawParticles2(entity);
                            }
                        }
                    }
                }
            }
            if (defender.attackType == defender.SWORD_ATTACK) {
                if (defender.attackTicks == 11) {
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_WHOOSH.get(), 2.0F, 1.0F);
                }
                if (defender.attackTicks == 12) {
                    float healing = 0.0F;

                    defender.makeSpinParticles();
                    for (Entity entity : defender.level.getEntities(defender, defender.getBoundingBox().inflate(15.0F))) {
                        if (EntityUtil.canHurtThisMob(entity, defender) && entity instanceof LivingEntity && entity.isAlive()) {
                            double x = defender.getX() - entity.getX();
                            double y = defender.getY() - entity.getY();
                            double z = defender.getZ() - entity.getZ();
                            double d = Math.sqrt(x * x + y * y + z * z);
                            if (defender.distanceTo(entity) < 3.0D) {
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch());
                                entity.hurt(DamageSource.mobAttack(defender), (float) defender.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                                entity.hurtMarked = true;
                                entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 2.5D, (-y / d * 0.4D) + 0.5D, -z / d * 2.5D));
                            }

                            healing += 2.0F;
                        }
                    }

                    if (!defender.level.isClientSide) {
                        defender.heal(Math.min(healing, 20.0F));
                    }
                }
            }
            if (defender.attackType == defender.AXES_ATTACK) {
                if (defender.attackTicks == (24 * defender.throwTimes)) {
                    defender.setAnimationState(0);
                    defender.setAnimationState(4);
                    defender.throwTimes += 1;
                }
                if ((defender.attackTicks == 6 + (24 * (defender.throwTimes - 1))) || (defender.attackTicks == 18 + (24 * (defender.throwTimes - 1)))) {
                    defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                    if (!defender.level.isClientSide) {
                        DefenderAxeEntity projectile = ModEntityTypes.DefenderAxe.get().create(defender.level);
                        assert projectile != null;
                        projectile.setPos(defender.getX(), defender.getY() + 1, defender.getZ());

                        projectile.setYHeadRot(defender.getYHeadRot());
                        projectile.setYRot(defender.getYHeadRot());

                        float power = (float) 6.0F;
                        projectile.setAcceleration(target.position().add(0, (target.getBbHeight() / 2.0D), 0).subtract(defender.position()).normalize().scale(power).scale(0.2d));

                        projectile.setShooter(defender);
                        defender.level.addFreshEntity(projectile);
                    }
                }
            }
            if (defender.attackType == defender.BOOMERANG_ATTACK) {
                if (defender.attackTicks == 10) {
                    defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 0.9F);
                    defender.setWeaponToShow(0);
                    if (!defender.level.isClientSide) {
                        BoomerangEntity projectile = ModEntityTypes.Boomerang.get().create(defender.level);
                        assert projectile != null;
                        projectile.setPos(defender.getX(), defender.getY() + 1, defender.getZ());

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
            if (defender.attackType == defender.SPIKES_ATTACK) {
                if (defender.attackTicks == 15) {
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 2.0F, 1.0F);
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIKE.get(), 2.0F, 1.0F);
                    CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 15);
                    defender.performSpellCasting(false);
                }
                if (defender.attackTicks == 39) {
                    defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);

                    if (target instanceof Player) {
                        defender.setDeltaMovement((target.getX() - defender.getX()) * 0.2D, 1.6D, (target.getZ() - defender.getZ()) * 0.2D);
                    } else {
                        int howManyEntities = 0;

                        for (Entity entity : defender.level.getEntities(target, target.getBoundingBox().inflate(20.0F))) {
                            if (EntityUtil.canHurtThisMob(entity, defender) && entity instanceof LivingEntity && entity.isAlive()) {
                                defender.averageXCord += entity.getX();
                                defender.averageZCord += entity.getZ();
                                howManyEntities += 1;
                            }
                        }

                        defender.averageXCord /= howManyEntities;
                        defender.averageZCord /= howManyEntities;

                        if (howManyEntities < 10) {
                            defender.setDeltaMovement((target.getX() - defender.getX()) * 0.2D, 1.6D, (target.getZ() - defender.getZ()) * 0.2D);
                        } else {
                            defender.setDeltaMovement((defender.averageXCord - defender.getX()) * 0.2D, 3.0D, (defender.averageZCord - defender.getZ()) * 0.2D);
                        }
                    }
                    defender.jumpAttacking = true;
                }
                if (defender.attackTicks >= 39 && !defender.isOnGround()) {
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
                                    defender.setAnimationState(17);
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
                                if (worldHeight > defender.level.getMinBuildHeight() + 1 && stretcher != 0) {
                                    defender.setStretch(worldHeight * stretcher / 1000);
                                }
                            }
                        }
                    }
                }
            }
            if (defender.attackType == defender.SHURIKENS_ATTACK) {
                if (defender.attackTicks == 4) {
                    defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.2F);
                }
                if (defender.attackTicks == 5) {
                    defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.3F);
                }
                if (defender.attackTicks == 6) {
                    defender.playSound(SoundEvents.WITCH_THROW, 2.0F, 1.5F);
                }
                if (defender.attackTicks == 16) {
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SHOOT.get(), 3.0F, 1.0F);

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

                    for (int i = 0; i < 75; ++i) {
                        ShurikenEntity shuriken = new ShurikenEntity(defender.level, defender);

                        shuriken.setPos(x, defender.getY() + 1.0D, z);

                        double d0 = target.getEyeY() - (double)1.1F;
                        double d1 = target.getX() - defender.getX();
                        double d2 = d0 - shuriken.getY();
                        double d3 = target.getZ() - defender.getZ();
                        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
                        if (target instanceof Player) {
                            shuriken.shoot(d1, d2 + d4, d3, 1.0F, 6.0F);
                        } else {
                            shuriken.shoot(d1, d2 + d4, d3, 1.0F, 16.0F);
                        }
                        defender.level.addFreshEntity(shuriken);
                    }
                }
            }
            if (defender.attackType == defender.CHAINSAW_ATTACK) {
                defender.setLaserPosition(target.getX(), target.getY() + (target.getBbHeight() / 2), target.getZ());
                if (defender.attackTicks == 3) {
                    defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                }
                if (defender.attackTicks == 15) {
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CHAINSAW_CATCH.get(), 2.0F, 1.0F);
                }
                if (defender.attackTicks == 19) {
                    defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CHAINSAW_WARN.get(), 2.0F, 1.0F);
                }
                if (defender.attackTicks == 23) {
                    defender.setShakeMultiplier(30);
                }
                if (defender.attackTicks >= 30) {
                    ChainsawEntity beam = null;
                    if (defender.attackTicks == 30) {
                        defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CHAINSAW.get(), 2.0F, 1.0F);
                        if (!defender.level.isClientSide) {
                            beam = new ChainsawEntity(ModEntityTypes.Chainsaw.get(), defender.level, defender, defender.getX(), defender.getY() + 1.125, defender.getZ(), (float) ((defender.yHeadRot + 90) * Math.PI / 180), (float) (-defender.getXRot() * Math.PI / 180), 66);
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
                    defender.lerpChainsawLookX(new Vec3(defender.laserX, defender.laserY, defender.laserZ), 100.0F, 4);

                    if (defender.lerpChainsawSteps > 0) {
                        defender.setChainsawLookX(defender.getChainsawLookX() + (float)(defender.lerpChainsawX - (double)defender.getChainsawLookX()) / (float)defender.lerpChainsawSteps);
                        --defender.lerpChainsawSteps;
                    }
                }
                if (defender.attackTicks == 96) {
                    defender.setShakeMultiplier(0);
                }
            }
            if (defender.attackType == defender.CLAWS_ATTACK) {
                if (defender.attackTicks == 12) {
                    float radius2 = 1.2f;
                    double x = defender.getX() + 0.8F * Math.sin(-defender.getYRot() * Math.PI / 180) + radius2 * Math.sin(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double y = defender.getY() + 1.0 + radius2 * Math.sin(-defender.getXRot() * Math.PI / 180);
                    double z = defender.getZ() + 0.8F * Math.cos(-defender.getYRot() * Math.PI / 180) + radius2 * Math.cos(-defender.yHeadRot * Math.PI / 180) * Math.cos(-defender.getXRot() * Math.PI / 180);
                    double thing = 1.2D;
                    List<LivingEntity> list = defender.level.getEntitiesOfClass(LivingEntity.class, new AABB(x - thing, defender.getY(), z - thing, x + thing, defender.getY() + thing, z + thing));
                    for (LivingEntity caught : list) {
                        if (caught != defender && caught.isAlive()) {
                            if (caught.hurt(DamageSource.mobAttack(defender), 8.0F)) {
                                defender.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 2.0F, 1.0F);
                                caught.setDeltaMovement(caught.getDeltaMovement().add(0.0D, 2.0D, 0.0D));
                                if (!caught.hasEffect(EffectRegisterer.KNOCKED_OUT.get())) {
                                    caught.addEffect(new MobEffectInstance(EffectRegisterer.KNOCKED_OUT.get(), 100, 0, true, true, true));
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
                    if (defender.attackTicks == 20) {
                        defender.setAnimationState(11);
                    }
                    if (defender.attackTicks == (5 + 22)) {
                        defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_WHOOSH.get(), 2.0F, 1.0F);
                        defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIKE.get(), 2.0F, 1.5F);
                    }
                    if (defender.attackTicks == (16 + 22)) {
                        defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_JUMP.get(), 2.0F, 1.0F);
                        defender.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
                        double multiplier = 0.2D;
                        defender.setDeltaMovement((defender.clawsTarget.getX() - defender.getX()) * multiplier,
                                (defender.clawsTarget.getY() - defender.getY()) * 0.1D,
                                (defender.clawsTarget.getZ() - defender.getZ()) * multiplier);
                        defender.jumpAttacking = true;
                    }
                    if (defender.attackTicks > (16 + 22) && defender.attackTicks < (30 + 22)) {
                        if (defender.distanceToSqr(defender.clawsTarget) < 4.5D && defender.getY() >= defender.clawsTarget.getY() - 0.2D && !defender.itsTimeToClawTarget) {
                            defender.itsTimeToClawTarget = true;
                            defender.jumpTicks = 21;
                            defender.setAnimationState(12);

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
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, defender.getVoicePitch());
                                defender.clawsTarget.invulnerableTime = 0;
                                defender.clawsTarget.hurtTime = 3;
                            }
                        }
                        if (defender.jumpTicks == (21 - 12) || defender.jumpTicks == (21 - 17)) {
                            if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 5.0F)) {
                                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.1f, 0, 4);
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SMACK.get(), 2.0F, defender.getVoicePitch() + 0.2F);
                                defender.clawsTarget.hurtTime = 3;
                                defender.clawsTarget.invulnerableTime = 0;
                            }
                        }
                        if (defender.jumpTicks == 0) {
                            defender.itsTimeToClawTarget = false;
                            if (defender.clawsTarget.hurt(DamageSource.mobAttack(defender), 8.0F)) {
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 2.0F, 1.0F);
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, 1.0F);
                                CameraShakeEntity.cameraShake(defender.level, defender.position(), 30, 0.2f, 0, 15);
                                defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SMACK.get(), 2.0F, defender.getVoicePitch());
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
        if (target != null) {
            if (defender.attackType == defender.RATATATABOW_ATTACK) {
                if (defender.attackTicks == 30) {
                    if (!defender.level.isClientSide) {
                        defender.setDeltaMovement(((target.getX() - defender.getX()) * 2.5D) * 0.08D, 1.0D, ((target.getZ() - defender.getZ()) * 2.5D) * 0.08D);
                    }
                    defender.setCustomRender(1);
                }
                if (defender.attackTicks > 30) {
                    if (defender.isOnGround() && defender.attackTicks2 == 0) {
                        defender.attackTicks2 = 1;
                        defender.setCustomRender(0);
                        defender.setAnimationState(18);
                    }
                    if (!defender.isOnGround() && defender.attackTicks % 3 == 0) {
                        defender.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_QUICK_WHOOSH.get(), 3.0F, defender.getVoicePitch() + 0.5F);
                    }
                }
                if (defender.attackTicks2 > 0 && defender.attackTicks2 <= 60) {
                    for (int i = 0; i < 5; i++) {
                        defender.fireProjectile(target, 1.0F, 6.0F);
                    }
                }
            }
        }
    }
}
