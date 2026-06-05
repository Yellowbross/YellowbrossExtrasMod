package com.yellowbrossproductions.yellowbrossextras.events;

import com.google.common.collect.Sets;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.AimbotEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.CreeperInfection;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.AbstractCreeperEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.LoseAIGoal;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.ConverslinEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.init.YellowbrossExtrasGameRules;
import com.yellowbrossproductions.yellowbrossextras.util.EffectRegisterer;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.RegistryHandler;
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
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

public class ServerEventHandler {
    @SubscribeEvent
    public void handleAIGoals(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(YellowbrossExtrasConfig.aiChangesNotAllowed.get().contains(entity.getEncodeId()) || YellowbrossExtrasConfig.aiChangesNotAllowed.get().contains(entity.getType().getKey(entity.getType()).getNamespace()))) {
            if (entity instanceof Mob mob) {
                ((Mob) entity).goalSelector.addGoal(0, new LoseAIGoal((Mob) entity));

                if (entity instanceof Raider || mob.getMobType() == MobType.ILLAGER) {
                    ((Mob) entity).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(((Mob) entity), AbstractCreeperEntity.class, true, p -> p instanceof CreeperInfection));
                    ((Mob) entity).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(((Mob) entity), AbstractOryctolin.class, true));
                }
                if (entity instanceof Villager) {
                    ((PathfinderMob) entity).goalSelector.addGoal(1, new AvoidEntityGoal<>(((PathfinderMob) entity), AbstractOryctolin.class, 8.0F, 0.8D, 0.8D));
                } else if (entity instanceof WanderingTrader) {
                    ((PathfinderMob) entity).goalSelector.addGoal(1, new AvoidEntityGoal<>(((PathfinderMob) entity), AbstractOryctolin.class, 8.0F, 0.5D, 0.5D));
                }

                if (entity instanceof Wolf) {
                    ((Mob) entity).targetSelector.addGoal(5, new NonTameRandomTargetGoal<>((TamableAnimal) entity, AbstractOryctolin.class, false, (Predicate<LivingEntity>)null));
                }

                ((Mob) entity).targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(((Mob) entity), AimbotEntity.class, true));
            }
        }
    }

    @SubscribeEvent
    public void hurtFunctions(LivingHurtEvent event) {
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
    public void creeperInfection(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof CreeperInfection && source.getEntity() instanceof LivingEntity living && entity instanceof Mob mob) {
            if (entity.level.getGameRules().getBoolean(YellowbrossExtrasGameRules.ENABLE_CREEPER_INFECTION)) {
                EntityUtil.convertMobToCreeper(mob, living, entity.getLevel());
            }
        }
    }

    @SubscribeEvent
    public void effectParticles(LivingEvent.LivingTickEvent event) {
        LivingEntity mob = event.getEntity();
        Random random = new Random();
        if (mob.hasEffect(EffectRegisterer.KNOCKED_OUT.get())) {
            EntityUtil.makeStunnedParticles(mob.level, mob);
        }
    }

    private static final Map<ServerLevel, VilvgaverSpawner> VILVGAVER_SPAWN_MAP = new HashMap<>();

    @SubscribeEvent
    public void loadCustomSpawners(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            VILVGAVER_SPAWN_MAP.put(serverWorld, new VilvgaverSpawner());
        }
    }

    @SubscribeEvent
    public void unloadCustomSpawners(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            VILVGAVER_SPAWN_MAP.remove(serverWorld);
        }
    }

    @SubscribeEvent
    public void tickCustomStuff(TickEvent.LevelTickEvent event) {
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
    public void huntedEffects1(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectRegisterer.HUNTED.get()) && YellowbrossExtrasConfig.vilvgaverChallenge_fallDamageAllowed.get()) {
            event.setDamageMultiplier(0.0000000000001F);
            if (event.getDistance() >= 4) {
                entity.heal(1.0F);
            }
        }
    }

    @SubscribeEvent
    public void huntedEffects2(TickEvent.PlayerTickEvent event) {
        Player mob = event.player;
        if (!mob.getLevel().isClientSide) {
            if (mob.getLevel().getGameRules().getBoolean(YellowbrossExtrasGameRules.VILVGAVERCHALLENGE)) {
                mob.addEffect(new MobEffectInstance(EffectRegisterer.HUNTED.get(), 1200, 0, false, false, true));

                if (YellowbrossExtrasConfig.vilvgaverChallenge_speedBuff.get() > -1) {
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, YellowbrossExtrasConfig.vilvgaverChallenge_speedBuff.get(), false, false, true));
                }
            }
            if (mob.hasEffect(EffectRegisterer.HUNTED.get())) {
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

                            mob.level.setBlockAndUpdate(blockpos1, RegistryHandler.FROZEN_LAVA.get().defaultBlockState());
                            mob.level.getProfiler().pop();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void huntedEffects3(PlayerEvent.BreakSpeed event) {
        Player mob = event.getEntity();
        if (!mob.getLevel().isClientSide) {
            ResourceLocation name = ForgeRegistries.BLOCKS.getKey(event.getState().getBlock());
            if (name != null) {
                if (mob.hasEffect(EffectRegisterer.HUNTED.get()) &&
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
    public void converslinHelper(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof IsOryctolinAligned && source.getEntity() instanceof LivingEntity living && entity instanceof Mob mob) {
            if (entity.level.getGameRules().getBoolean(YellowbrossExtrasGameRules.CONVERSLIN_CONVERSION)) {
                if (source.getEntity() instanceof ConverslinEntity) {
                    EntityUtil.convertMobToCarrot(mob, living, entity.getLevel());
                }
            }
        }
    }

    @SubscribeEvent
    public void defenderCarnage(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof DefenderEntity defender) {
            if (source.isExplosion() || defender.isCarnageAttack()) {
                defender.increaseDefendersCarnage();
            }
        }
    }

    @SubscribeEvent
    public void cancelHealing(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectRegisterer.KNOCKED_OUT.get())) {
            event.setCanceled(true);
        }
    }
}
