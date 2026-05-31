package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.AbstractCreeperEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.CreeperInfection;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.SneakerEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.IntelligenceEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuideEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions.CarrotMinionEntity;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BlitzManager;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BunnyBlitz;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class EntityUtil {
    public static boolean canHurtThisMob(Entity target, Mob attacker) {
        if (target.isInvulnerable()) {
            return false;
        }
        if ((attacker.getTeam() != null || target.getTeam() != null)) {
            return attacker.getTeam() != target.getTeam();
        } else {
            return true;
        }
    }

    public static boolean isMobNotInCreativeMode(Entity entity) {
        if (entity instanceof Player) {
            return !((Player) entity).isCreative() && !((Player)entity).isSpectator();
        }
        return true;
    }

    public static boolean isMobOnOtherTeam2(Entity target, Mob attacker) {
        if (attacker.getTeam() != null || target.getTeam() != null) {
            return attacker.getTeam() != target.getTeam();
        } else {
            return true;
        }
    }

    public static void convertMobToCreeper(Mob entity, LivingEntity killer, Level level) {
        Random random = new Random();
        if (!entity.isRemoved() && !(entity instanceof CreeperInfection)) {
            if (!tryConvertCustomCreeper(entity, killer, level)) {
                AbstractCreeperEntity creeper = null;

                float paraCreeperWidth = 0.5F;

                float normalCreeperWidth = 1.0F;

                float ravagerCreeperWidth = 2.0F;

                if (entity.getBbWidth() < paraCreeperWidth) {
                    creeper = ModEntityTypes.Paracreeper.get().create(level);
                } else if (entity.getBbWidth() >= paraCreeperWidth && entity.getBbWidth() <= normalCreeperWidth) {
                    creeper = ModEntityTypes.Sneaker.get().create(level);
                    if (entity instanceof RangedAttackMob) {
                        if (random.nextBoolean()) {
                            creeper = ModEntityTypes.Sprayer.get().create(level);
                        }
                    }
                } else if (entity.getBbWidth() >= normalCreeperWidth && entity.getBbWidth() <= ravagerCreeperWidth) {
                    creeper = ModEntityTypes.Crawler.get().create(level);
                } else {
                    creeper = ModEntityTypes.Freaker.get().create(level);
                }

                if (creeper != null) {
                    creeper.copyPosition(entity);
                    creeper.setNoAi(entity.isNoAi());
                    if (entity.hasCustomName()) {
                        creeper.setCustomName(entity.getCustomName());
                        creeper.setCustomNameVisible(entity.isCustomNameVisible());
                    }

                    if (killer.getTeam() != null) {
                        level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                level.getScoreboard().getPlayerTeam(killer.getTeam().getName()));
                    }

                    if (entity.isPassenger() && entity.getVehicle() != null) {
                        Entity vehicle = entity.getVehicle();
                        entity.stopRiding();
                        creeper.startRiding(vehicle, true);
                    }

                    if (!entity.getPassengers().isEmpty()) {
                        for (Entity entity1 : entity.getPassengers()) {
                            if (!entity1.isRemoved()) {
                                entity1.stopRiding();
                                entity1.startRiding(creeper, true);
                            }
                        }
                    }

                    if (creeper instanceof SneakerEntity sneaker) {
                        if (random.nextInt(4) == 0) {
                            sneaker.setCreeperType(1);
                            List<SneakerEntity> list = level.getEntitiesOfClass(SneakerEntity.class, sneaker.getBoundingBox().inflate(30.0F), predicate -> {
                                return predicate.getCreeperType() == 2 && predicate != sneaker;
                            });
                            if (list.isEmpty()) {
                                if (random.nextInt(3) == 0) {
                                    sneaker.setCreeperType(2);
                                }
                            }
                        }
                    }

                    if (!level.isClientSide) {
                        level.addFreshEntity(creeper);
                    }
                    entity.discard();

                    creeper.playSound(SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, 3.0F, creeper.getVoicePitch());
                }
            }
        }
    }

    // code adapted from Dungeons Mobs
    public static boolean tryConvertCustomCreeper(Mob infected, LivingEntity killer, Level level) {
        List<? extends String> creeperConversions = YellowbrossExtrasConfig.creeperInfection_turnTo.get();
        if (creeperConversions.isEmpty()) return false;
        for (String string : creeperConversions) {
            List<String> splitArray = List.of(string.split(","));
            if (splitArray.size() == 2) {
                String entityID = splitArray.get(0);
                String convertTo = splitArray.get(1);

                if (entityID.equals(infected.getEncodeId())) {
                    EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(convertTo));
                    if (entityType != null) {
                        Entity entity = entityType.create(level);
                        if (entity instanceof Mob creeper) {
                            DifficultyInstance difficultyForLocation = level.getCurrentDifficultyAt(infected.blockPosition().above());
                            creeper.copyPosition(infected);
                            if (!level.isClientSide) {
                                creeper.finalizeSpawn((ServerLevelAccessor)level, difficultyForLocation, MobSpawnType.CONVERSION, (SpawnGroupData) null, (CompoundTag) null);
                            }
                            if (creeper instanceof Raider) {
                                ((Raider) creeper).setCanJoinRaid(false);
                            }

                            creeper.setNoAi(infected.isNoAi());
                            if (infected.hasCustomName()) {
                                creeper.setCustomName(infected.getCustomName());
                                creeper.setCustomNameVisible(infected.isCustomNameVisible());
                            }

                            if (killer.getTeam() != null) {
                                level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                        level.getScoreboard().getPlayerTeam(killer.getTeam().getName()));
                            }

                            if (infected.isPassenger() && infected.getVehicle() != null) {
                                Entity vehicle = infected.getVehicle();
                                infected.stopRiding();
                                creeper.startRiding(vehicle, true);
                            }

                            creeper.setPersistenceRequired();
                            if (!level.isClientSide) {
                                infected.discard();
                            }
                            creeper.playSound(SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, 3.0F, creeper.getVoicePitch());
                            return level.addFreshEntity(creeper);
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void convertMobToCarrot(Mob entity, LivingEntity killer, Level level) {
        Random random = new Random();
        if (!entity.isRemoved()) {
            if (!(entity instanceof IsOryctolinAligned)) {
                CarrotMinionEntity creeper = null;

                creeper = ModEntityTypes.CarrotMinion.get().create(level);

                if (creeper != null) {
                    creeper.copyPosition(entity);
                    creeper.setNoAi(entity.isNoAi());
                    if (entity.hasCustomName()) {
                        creeper.setCustomName(entity.getCustomName());
                        creeper.setCustomNameVisible(entity.isCustomNameVisible());
                    }

                    if (killer.getTeam() != null) {
                        level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                level.getScoreboard().getPlayerTeam(killer.getTeam().getName()));
                    }

                    if (entity.isPassenger() && entity.getVehicle() != null) {
                        Entity vehicle = entity.getVehicle();
                        entity.stopRiding();
                        creeper.startRiding(vehicle, true);
                    }

                    if (!entity.getPassengers().isEmpty()) {
                        for (Entity entity1 : entity.getPassengers()) {
                            if (!entity1.isRemoved()) {
                                entity1.stopRiding();
                                entity1.startRiding(creeper, true);
                            }
                        }
                    }

                    if (!level.isClientSide) level.addFreshEntity(creeper);
                    entity.discard();

                    creeper.playSound(SoundEvents.ZOMBIE_INFECT, 3.0F, creeper.getVoicePitch());
                }
            }
        }
    }

    public static void makeStunnedParticles(Level level, Entity caught) {
        final Random random = new Random();
        if (!level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)level).players()) {
                if (serverPlayer.distanceToSqr(caught) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 2; ++i) {
                        double d0 = (-0.5 + random.nextGaussian()) / 10;
                        double d1 = (-0.5 + random.nextGaussian()) / 10;
                        double d2 = (-0.5 + random.nextGaussian()) / 10;
                        packet.queueParticle(ParticleTypes.SPLASH, false, new Vec3(caught.getRandomX(0.5D), caught.getRandomY(), caught.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 2; ++i) {
                        double d0 = (-0.5 + random.nextGaussian()) / 10;
                        double d1 = (-0.5 + random.nextGaussian()) / 10;
                        double d2 = (-0.5 + random.nextGaussian()) / 10;
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(caught.getRandomX(0.5D), caught.getY() + caught.getBbHeight() + 0.2D, caught.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public static void makeCircleParticles(Level level, Entity spawner, ParticleOptions particleType, int amount, double y, float velocity) {
        final Random random = new Random();
        if (!level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)level).players()) {
                if (serverPlayer.distanceToSqr(spawner) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    // code adapted from Mowzie's Mobs
                    for (int i = 0; i < amount; i++) {
                        float TAU = (float) (2 * StrictMath.PI);

                        float yaw = i * (TAU / amount);
                        float vy = random.nextFloat() * 0.1F - 0.05f;
                        float vx = velocity * Mth.cos(yaw);
                        float vz = velocity * Mth.sin(yaw);
                        packet.queueParticle(particleType, false, new Vec3(spawner.getX(), spawner.getY() + y, spawner.getZ()), new Vec3(vx, vy, vz));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public static void broadcastMessage(Level world, Component m) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        for (Player player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(m);
        }
    }

    public static BunnyBlitz findNearestBlitz(PathfinderMob mob, ServerLevel level) {
        List<BunnyBlitz> list = BlitzManager.getRaids(level);
        if (list.isEmpty()) {
            return null;
        }
        BunnyBlitz closestBlitz = list.get(0);
        double minDistance = mob.distanceToSqr(new Vec3(closestBlitz.getCenter().getX(), closestBlitz.getCenter().getY(), closestBlitz.getCenter().getZ()));

        for (int i = 1; i < list.size(); i++) {
            double distance = mob.distanceToSqr(new Vec3(list.get(i).getCenter().getX(), list.get(i).getCenter().getY(), list.get(i).getCenter().getZ()));
            if (distance < minDistance) {
                minDistance = distance;
                closestBlitz = list.get(i);
            }
        }

        return closestBlitz;
    }

    public static IntelligenceEntity getNearestIntel(List<IntelligenceEntity> intels, LivingEntity playing) {
        double d0 = -1.0D;
        IntelligenceEntity t = null;

        for(IntelligenceEntity t1 : intels) {
            double d1 = t1.distanceToSqr(playing.getX(), playing.getY(), playing.getZ());
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }

    public static boolean isEnemyCapping(LivingEntity mob, LivingEntity defender) {
        for (Entity entity : mob.getPassengers()) {
            if (entity instanceof IntelligenceEntity intel) {
                if (intel.getTeam() == defender.getTeam()) return true;
            }
        }
        return false;
    }

    @Nullable
    public static PathGuideEntity getNearestGuide(List<PathGuideEntity> intels, LivingEntity playing) {
        double d0 = -1.0D;
        PathGuideEntity t = null;

        for(PathGuideEntity t1 : intels) {
            double d1 = t1.distanceToSqr(playing.getX(), playing.getY(), playing.getZ());
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }

    @Nullable
    public static BlockPos getNextGuide(PathGuideEntity guide, LivingEntity playing, Vec3 goal) {
        BlockPos t = null;

        Random random = new Random();
        List<BlockPos> randomList = Lists.newArrayList();

        for (BlockPos pos : guide.getNeighbors()) {
            Vec3 pos_distance = new Vec3(pos.getX(), pos.getY(), pos.getZ());
            Vec3 guide_distance = new Vec3(guide.getX(), guide.getY(), guide.getZ());
            if (goal.distanceToSqr(pos_distance) < goal.distanceToSqr(guide_distance)) {
                randomList.add(pos);
            }
            if (guide.getNeighbors().size() == 2) {
                double distance1 = goal.distanceToSqr(new Vec3(guide.getNeighbors().get(0).getX(), guide.getNeighbors().get(0).getY(), guide.getNeighbors().get(0).getZ()));
                double distance2 = goal.distanceToSqr(new Vec3(guide.getNeighbors().get(1).getX(), guide.getNeighbors().get(1).getY(), guide.getNeighbors().get(1).getZ()));
                randomList.add(guide.getNeighbors().get(distance1 <= distance2 ? 0 : 1));
            }
        }

        if (!randomList.isEmpty()) {
            t = randomList.get(random.nextInt(randomList.size()));
        }

        return t;
    }

    public static boolean checkGuide(BlockPos pos, LivingEntity playing, Vec3 goal) {
        PathGuideEntity guide = getGuideAt(pos, playing.level);
        if (guide != null) {
            for (BlockPos pos2 : guide.getNeighbors()) {
                Vec3 pos2_distance = new Vec3(pos2.getX(), pos2.getY(), pos2.getZ());
                Vec3 guide_distance = new Vec3(guide.getX(), guide.getY(), guide.getZ());
                if (goal.distanceToSqr(pos2_distance) < goal.distanceToSqr(guide_distance) && !(playing.distanceToSqr(pos2_distance) < 9.0D)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public static PathGuideEntity getGuideAt(BlockPos pos, Level level) {
        PathGuideEntity t = null;
        List<PathGuideEntity> list = level.getEntitiesOfClass(PathGuideEntity.class, new AABB(pos));

        if (!list.isEmpty()) {
            t = list.get(0);
        }
        return t;
    }

    public static boolean hasLineOfSightToLocation(LivingEntity mob, Vec3 location) {
        Vec3 vec3 = new Vec3(mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ());
        if (location.distanceTo(vec3) > 128.0D) {
            return false;
        } else {
            return mob.level.clip(new ClipContext(vec3, location, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob)).getType() == HitResult.Type.MISS;
        }
    }

    // code borrowed from Twilight Forest
    public static void makeSimpleTrail(Entity entity, SimpleParticleType type, int amount, double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        Random random = new Random();

        if (!entity.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)entity.level).players()) {
                if (serverPlayer.distanceToSqr(entity) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for (int i = 0; i < amount; i++) {
                        double trailFactor = i / (amount - 1.0D);
                        float f = (random.nextFloat() - 0.5F) * 0.2F;
                        float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                        float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                        double tx = srcX + (destX - srcX) * trailFactor;
                        double ty = srcY + (destY - srcY) * trailFactor;
                        double tz = srcZ + (destZ - srcZ) * trailFactor;
                        packet.queueParticle(type, false, new Vec3(tx, ty, tz), new Vec3(f, f1, f2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    // code adapted from Illage & Spillage: Respillaged
    public static void basicMobFlight(Mob mob, @Nullable LivingEntity target, double distanceRequired, int blocksToFlyNormal, int blocksToFlyAggressive) {
        BlockState blockState = mob.level.getBlockState(mob.blockPosition().below(blocksToFlyNormal));
        if (target != null) {
            if (mob.distanceToSqr(target) > distanceRequired)
                mob.setDeltaMovement(mob.getDeltaMovement().add(target.position().subtract(mob.position()).normalize().scale(0.016)));

            blockState = mob.level.getBlockState(mob.blockPosition().below(blocksToFlyAggressive));

            if (blockState.isAir()) {
                if (mob.getDeltaMovement().y > 0)
                    mob.setDeltaMovement(mob.getDeltaMovement().add(0, -0.01, 0));
            } else {
                mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.04, 0));
            }
        } else if (!blockState.isAir()) {
            mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.04, 0));
        } else {
            mob.setDeltaMovement(mob.getDeltaMovement().add(0, -0.01, 0));
        }
    }

    public static List<Entity> getEntitiesFromAABB(Level level, double size, Entity attacker, Predicate<? super Entity> predicate) {
        return level.getEntities(attacker, new AABB(attacker.getX() - size, attacker.getY() - size, attacker.getZ() - size, attacker.getX() + size, attacker.getY() + size, attacker.getZ() + size), predicate);
    }

    // Code lent by TheDarkPeasant since "animateWhen" does not exist in 1.19.2
    public static void animateWhen(AnimationState state, boolean condition, int tickCount) {
        state.stop();
        if (condition) state.startIfStopped(tickCount);
    }

    public static float multiplyToScrewArmor(LivingEntity entity, float multiplier) {
        if (!(entity instanceof Player)) {
            return Math.max(entity.getArmorValue() * multiplier, 1.0f);
        }
        return 1.0f;
    }
}
