package com.yellowbrossproductions.yellowbrossextras.events;

import com.google.common.collect.Sets;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.*;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.*;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGunEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.LoseAIGoal;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.ConverslinEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions.CarrotMinionEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.BoomerangEntity;
import com.yellowbrossproductions.yellowbrossextras.init.*;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.VilvgaverSpawner;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BlitzManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class YECommonEventHandler {

    @Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            PacketHandler.init();
        }

        @SubscribeEvent
        public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            SpawnPlacements.register(YEEntityTypes.Defender.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
            SpawnPlacements.register(YEEntityTypes.SentryGun.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
            SpawnPlacements.register(YEEntityTypes.CreeperBullet.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);

            SpawnPlacements.register(YEEntityTypes.Sneaker.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.Paracreeper.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.Crawler.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.Freaker.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.Sprayer.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);

            SpawnPlacements.register(YEEntityTypes.Vilvgaver.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);

            SpawnPlacements.register(YEEntityTypes.Converslin.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.CarrotMinion.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);

            SpawnPlacements.register(YEEntityTypes.AmoebicDevourer.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.HyperSnowGolem.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
            SpawnPlacements.register(YEEntityTypes.SkeletonSnap.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.Aimbot.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
            SpawnPlacements.register(YEEntityTypes.StickFigure.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
        }

        @SubscribeEvent
        public static void createAttributes(EntityAttributeCreationEvent event) {
            event.put(YEEntityTypes.Defender.get(), DefenderEntity.createAttributes().build());
            event.put(YEEntityTypes.SentryGun.get(), SentryGunEntity.createAttributes().build());
            event.put(YEEntityTypes.CreeperBullet.get(), CreeperBulletEntity.createAttributes().build());

            event.put(YEEntityTypes.Sneaker.get(), SneakerEntity.createAttributes().build());
            event.put(YEEntityTypes.Paracreeper.get(), ParacreeperEntity.createAttributes().build());
            event.put(YEEntityTypes.Crawler.get(), CrawlerEntity.createAttributes().build());
            event.put(YEEntityTypes.Freaker.get(), FreakerEntity.createAttributes().build());
            event.put(YEEntityTypes.Sprayer.get(), SprayerEntity.createAttributes().build());

            event.put(YEEntityTypes.Vilvgaver.get(), VilvgaverEntity.createAttributes().build());

            event.put(YEEntityTypes.AmoebicDevourer.get(), AmoebicDevourerEntity.createAttributes().build());
            event.put(YEEntityTypes.HyperSnowGolem.get(), HyperSnowGolemEntity.createAttributes().build());
            event.put(YEEntityTypes.SkeletonSnap.get(), SkeletonSnapEntity.createAttributes().build());
            event.put(YEEntityTypes.Aimbot.get(), AimbotEntity.createAttributes().build());
            event.put(YEEntityTypes.StickFigure.get(), StickFigureEntity.createAttributes().build());

            event.put(YEEntityTypes.Converslin.get(), ConverslinEntity.createAttributes().build());
            event.put(YEEntityTypes.CarrotMinion.get(), CarrotMinionEntity.createAttributes().build());

            event.put(YEEntityTypes.Boomerang.get(), BoomerangEntity.createAttributes().build());
        }
    }

    @SubscribeEvent
    public static void handleAIGoals(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(YellowbrossExtrasConfig.aiChangesNotAllowed.get().contains(entity.getEncodeId()) || YellowbrossExtrasConfig.aiChangesNotAllowed.get().contains(entity.getType().getKey(entity.getType()).getNamespace()))) {
            if (entity instanceof Mob mob) {
                mob.goalSelector.addGoal(0, new LoseAIGoal(mob));

                if (entity instanceof Raider || mob.getMobType() == MobType.ILLAGER) {
                    mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, AbstractCreeperEntity.class, true, p -> p instanceof CreeperInfection));
                    mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, AbstractOryctolin.class, true));
                }
                if (entity instanceof Villager) {
                    mob.goalSelector.addGoal(1, new AvoidEntityGoal<>(((PathfinderMob) entity), AbstractOryctolin.class, 8.0F, 0.8D, 0.8D));
                } else if (entity instanceof WanderingTrader) {
                    mob.goalSelector.addGoal(1, new AvoidEntityGoal<>(((PathfinderMob) entity), AbstractOryctolin.class, 8.0F, 0.5D, 0.5D));
                }

                if (entity instanceof Wolf) {
                    mob.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>((TamableAnimal) entity, AbstractOryctolin.class, false, (Predicate<LivingEntity>)null));
                }

                mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, AimbotEntity.class, true));
            }
        }
    }

    @SubscribeEvent
    public static void hurtFunctions(LivingHurtEvent event) {
        // creepers cheating
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof AbstractCreeperEntity && entity instanceof Mob mob) {
            mob.invulnerableTime = 0;
        }

        // no friendly fire
        if (source.getEntity() instanceof AbstractOryctolin oryctolin1 && entity instanceof AbstractOryctolin oryctolin2) {
            if (oryctolin1.getTeam() != null || oryctolin2.getTeam() != null) {
                if (!EntityUtil.canHurtThisMob(oryctolin1, oryctolin2)) {
                    event.setCanceled(true);
                }
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void makeThisMurderSpecial(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof CreeperInfection && source.getEntity() instanceof LivingEntity living && entity instanceof Mob mob) {
            if (entity.level.getGameRules().getBoolean(YEGameRules.ENABLE_CREEPER_INFECTION)) {
                EntityUtil.convertMobToCreeper(mob, living, entity.getLevel());
            }
        }

        if (source.getEntity() instanceof IsOryctolinAligned && source.getEntity() instanceof LivingEntity living && entity instanceof Mob mob) {
            if (entity.level.getGameRules().getBoolean(YEGameRules.CONVERSLIN_CONVERSION)) {
                if (source.getEntity() instanceof ConverslinEntity) {
                    EntityUtil.convertMobToCarrot(mob, living, entity.getLevel());
                }
            }
        }

        if (source.getEntity() instanceof DefenderEntity defender) {
            if (source.isExplosion() || defender.isCarnageAttack()) {
                defender.increaseDefendersCarnage();
            }
        }

        if (entity.hasEffect(YEEffects.SUPER_DUPER_POISON.get()) && entity instanceof Mob mob && !entity.level.isClientSide) {
            EntityUtil.makeAParticle(entity.level, YEParticleTypes.SUPERDUPERPOISON_EXPLOSION.get(), false, entity.getBoundingBox().getCenter(), Vec3.ZERO);
            for (int i = 0; i < 100; ++i) {
                Random random = new Random();
                EntityUtil.makeAParticle(entity.level, YEParticleTypes.SUPERDUPERPOISON_DRIP.get(), false, entity.getBoundingBox().getCenter(), new Vec3(
                        -0.5f + random.nextFloat(),
                        (-0.5f + random.nextFloat()) + (entity.isOnGround() ? 0.5f : 0.0f),
                        -0.5f + random.nextFloat()
                ));
            }
            List<Entity> list = entity.level.getEntities(entity, entity.getBoundingBox().inflate(4.0f), p -> !(p instanceof DefenderEntity) && p instanceof LivingEntity && EntityUtil.isMobNotInCreativeMode(p));
            for (Entity hit : list) {
                hit.hurtMarked = true;
                hit.setDeltaMovement(hit.getDeltaMovement()
                        .add(hit.position().subtract(entity.position()).normalize().scale(2.5f))
                        .add(0, hit.isOnGround() ? 0.4d : 0, 0));
            }
            CameraShakeEntity.cameraShake(entity.level, entity.position(), 20, 0.4f, 2, 3);
            entity.playSound(YESoundEvents.SUPERDUPERPOISON_EXPLOSION.get(), 4.0F, 1.0F);
            EntityUtil.fireCircleOfPoisonBalls(entity.level, mob, 10, mob.getBbHeight() / 2, 1.0f, true);
            entity.discard();
        }
    }

    @SubscribeEvent
    public static void doEffectVFX(LivingEvent.LivingTickEvent event) {
        LivingEntity mob = event.getEntity();
        Random random = new Random();
        if (mob.hasEffect(YEEffects.KNOCKED_OUT.get())) {
            EntityUtil.makeStunnedParticles(mob.level, mob);
        }
    }

    private static final Map<ServerLevel, VilvgaverSpawner> VILVGAVER_SPAWN_MAP = new HashMap<>();

    @SubscribeEvent
    public static void loadCustomSpawners(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            VILVGAVER_SPAWN_MAP.put(serverWorld, new VilvgaverSpawner());
        }
    }

    @SubscribeEvent
    public static void unloadCustomSpawners(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            VILVGAVER_SPAWN_MAP.remove(serverWorld);
        }
    }

    @SubscribeEvent
    public static void tickCustomStuff(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide && event.level instanceof ServerLevel server) {
            VilvgaverSpawner vilvgaverSpawner = VILVGAVER_SPAWN_MAP.get(server);
            if (vilvgaverSpawner != null) {
                vilvgaverSpawner.tick(server, false, false);
            }
            if (event.phase == TickEvent.Phase.END) {
                BlitzManager.tickRaids(event.level);
            }
        }
    }

    @SubscribeEvent
    public static void huntedEffects1(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(YEEffects.HUNTED.get()) && YellowbrossExtrasConfig.vilvgaverChallenge_fallDamageAllowed.get()) {
            event.setDamageMultiplier(0.0000000000001F);
            if (event.getDistance() >= 4) {
                entity.heal(1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void huntedEffects2(TickEvent.PlayerTickEvent event) {
        Player mob = event.player;
        if (!mob.getLevel().isClientSide) {
            if (mob.getLevel().getGameRules().getBoolean(YEGameRules.VILVGAVERCHALLENGE)) {
                mob.addEffect(new MobEffectInstance(YEEffects.HUNTED.get(), 1200, 0, false, false, true));

                if (YellowbrossExtrasConfig.vilvgaverChallenge_speedBuff.get() > -1) {
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, YellowbrossExtrasConfig.vilvgaverChallenge_speedBuff.get(), false, false, true));
                }
            }
            if (mob.hasEffect(YEEffects.HUNTED.get())) {
                if (YellowbrossExtrasConfig.vilvgaverChallenge_restoreHunger.get()) {
                    mob.getFoodData().setFoodLevel(20);
                }

                if (mob.level.dimension() == Level.NETHER) {
                    final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
                    Set<BlockPos> set = Sets.newHashSet();
                    int k;
                    int l;
                    for(int j = 0; j < 16; ++j) {
                        for(k = 0; k < 16; ++k) {
                            for(l = 0; l < 16; ++l) {
                                if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                    double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                                    double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                                    double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                    d0 /= d3;
                                    d1 /= d3;
                                    d2 /= d3;
                                    float f = (float) (YellowbrossExtrasConfig.vilvgaverChallenge_lavaFreezeRadius.get() * (0.7F + mob.level.random.nextFloat() * 0.6F));
                                    double d4 = mob.getX();
                                    double d6 = mob.getY();
                                    double d8 = mob.getZ();

                                    for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                        BlockPos blockpos = new BlockPos(d4, d6, d8);
                                        BlockState blockstate = mob.level.getBlockState(blockpos);
                                        FluidState fluidstate = mob.level.getFluidState(blockpos);
                                        if (!mob.level.isInWorldBounds(blockpos)) {
                                            break;
                                        }

                                        if (!blockstate.isAir()) {
                                            set.add(blockpos);
                                        }

                                        d4 += d0 * 0.30000001192092896;
                                        d6 += d1 * 0.30000001192092896;
                                        d8 += d2 * 0.30000001192092896;
                                    }
                                }
                            }
                        }
                    }

                    toBlow.addAll(set);
                    ObjectListIterator var5 = toBlow.iterator();
                    while(var5.hasNext()) {
                        BlockPos blockpos = (BlockPos)var5.next();
                        BlockState blockstate = mob.level.getBlockState(blockpos);
                        net.minecraft.world.level.block.Block block = blockstate.getBlock();
                        if (!blockstate.isAir() && blockstate.is(Blocks.LAVA)) {
                            BlockPos blockpos1 = blockpos.immutable();
                            mob.level.getProfiler().push("explosion_blocks");

                            mob.level.setBlockAndUpdate(blockpos1, YEItemsAndBlocks.FROZEN_LAVA.get().defaultBlockState());
                            mob.level.getProfiler().pop();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void huntedEffects3(PlayerEvent.BreakSpeed event) {
        Player mob = event.getEntity();
        if (!mob.getLevel().isClientSide) {
            ResourceLocation name = ForgeRegistries.BLOCKS.getKey(event.getState().getBlock());
            if (name != null) {
                if (mob.hasEffect(YEEffects.HUNTED.get()) &&
                        YellowbrossExtrasConfig.vilvgaverChallenge_blockInstabreaks.get().contains(name.toString())) {
                    event.setNewSpeed(50.0F);
                    if (event.getPosition().isPresent()) {
                        mob.getLevel().levelEvent(2001, event.getPosition().get(), Block.getId(event.getState()));
                        mob.getLevel().setBlockAndUpdate(event.getPosition().get(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(YEEffects.KNOCKED_OUT.get())) {
            event.setCanceled(true);
        }
    }
}
