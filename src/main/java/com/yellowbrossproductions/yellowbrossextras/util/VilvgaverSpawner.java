package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.Vilvgaver;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEGameRules;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class VilvgaverSpawner implements CustomSpawner {
    private final RandomSource random = RandomSource.create();
    private int tickDelay;
    private int breathTimeDelay;
    public static final Predicate<LivingEntity> BANNED_ENTITIES = (livingEntity) -> livingEntity instanceof PathfinderMob && (YellowbrossExtrasConfig.vilvgaverChallenge_mustNotSpawnNear.get().contains(livingEntity.getEncodeId()) || YellowbrossExtrasConfig.vilvgaverChallenge_mustNotSpawnNear.get().contains(livingEntity.getType().getKey(livingEntity.getType()).getNamespace()));

    @Override
    public int tick(ServerLevel pLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies) {
        if (this.breathTimeDelay > 0 && this.playerWithoutAVilvgaver(pLevel)) {
            this.breathTimeDelay -= 1;
        }
        if (!pLevel.getGameRules().getBoolean(YEGameRules.VILVGAVERCHALLENGE)) {
            return 0;
        } else if (pLevel.getDifficulty() == Difficulty.PEACEFUL) {
            return 0;
        } else if (--this.tickDelay > 0 || this.breathTimeDelay > 0) {
            return 0;
        } else {
            this.tickDelay = 20;
            if (this.spawn(pLevel)) {
                this.breathTimeDelay = YellowbrossExtrasConfig.vilvgaverChallenge_escapeTime.get() * 40;
                return 1;
            } else {
                return 0;
            }
        }
    }

    private boolean spawn(ServerLevel pLevel) {
        List<ServerPlayer> players = pLevel.players();
        boolean returning = false;
        for (Player player : players) {
            if (player == null) {
                returning = true;
            } else {
                if (noBannedMobsNearby(pLevel, player) && noBannedBlocksNearby(pLevel, player)) {
                    BlockPos blockpos = player.blockPosition();
                    int i = 48;
                    BlockPos blockpos2 = this.findSpawnPositionNear(pLevel, blockpos, 60);
                    if (blockpos2 != null &&
                            this.hasEnoughSpace(pLevel, blockpos2) &&
                            randomTest(player.getRandom()) == 0)
                    {
                        Vilvgaver vilvgaver = YEEntityTypes.Vilvgaver.get().spawn(pLevel, (CompoundTag)null, (Component)null, (Player)null, blockpos2, MobSpawnType.EVENT, false, false);
                        if (vilvgaver != null) {
                            vilvgaver.playSound(YESoundEvents.ENTITY_VILVGAVER_RESPAWN.get(), 10.0F, 1.0F);
                            AttributeInstance speed = vilvgaver.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
                            if (speed != null) {
                                speed.setBaseValue(YellowbrossExtrasConfig.vilvgaverChallenge_defaultSpeed.get());
                                if (vilvgaver.level.dimension() == Level.NETHER && YellowbrossExtrasConfig.vilvgaverChallenge_netherSpeed.get() > 0) {
                                    speed.setBaseValue(YellowbrossExtrasConfig.vilvgaverChallenge_netherSpeed.get());
                                }
                            }
                            vilvgaver.setChallenge(true);
                            vilvgaver.addEffect(new MobEffectInstance(MobEffects.GLOWING, YellowbrossExtrasConfig.vilvgaverChallenge_glowTime.get() * 20, 0, false, false, true));
                            returning = true;
                        }
                    } // else YellowbrossExtras.LOGGER.debug("Random chance failed...");
                }
            }
        }
        return returning;
    }

    private int randomTest(RandomSource source) {
        int random = YellowbrossExtrasConfig.vilvgaverChallenge_randomSpawnChance.get();
        if (random > 0) {
            return source.nextInt(random);
        }
        return 0;
    }

    @Nullable
    private BlockPos findSpawnPositionNear(LevelReader levelReader, BlockPos blockPos, int i1) {
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            int j = blockPos.getX() + this.random.nextInt(i1 * 2) - i1;
            int k = blockPos.getZ() + this.random.nextInt(i1 * 2) - i1;
            int l = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.NO_RESTRICTIONS, levelReader, blockpos1, YEEntityTypes.Vilvgaver.get())) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }

    private boolean hasEnoughSpace(BlockGetter blockGetter, BlockPos blockPos) {
        for(BlockPos blockpos : BlockPos.betweenClosed(blockPos, blockPos.offset(1, 2, 1))) {
            if (!blockGetter.getBlockState(blockpos).getCollisionShape(blockGetter, blockpos).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private boolean noBannedMobsNearby(ServerLevel serverLevel, Player player) {
        if (player == null) {
            return true;
        } else {
            List<Vilvgaver> list = serverLevel.getEntitiesOfClass(Vilvgaver.class, player.getBoundingBox().inflate(100.0D), (predicate) -> {
                return predicate.isChallenge() || predicate.tickCount < 200;
            });

            List<PathfinderMob> list2 = serverLevel.getEntitiesOfClass(PathfinderMob.class, player.getBoundingBox().inflate(YellowbrossExtrasConfig.vilvgaverChallenge_moblistRadius.get()), BANNED_ENTITIES);
            // if (!list2.isEmpty()) YellowbrossExtras.LOGGER.debug("Everything with the blacklist is in order!");

            return list.isEmpty() && list2.isEmpty();
        }
    }

    private boolean noBannedBlocksNearby(ServerLevel serverLevel, Player player) {
        if (player == null) {
            return true;
        } else {
            List<BlockState> list = serverLevel.getBlockStates(player.getBoundingBox().inflate(YellowbrossExtrasConfig.vilvgaverChallenge_blocklistRadius.get())).toList();

            boolean listCheck = true;

            for (BlockState blockState : list) {
                if (YellowbrossExtrasConfig.vilvgaverChallenge_blocklist.get().contains(Registry.BLOCK.getKey(blockState.getBlock()).toString())) {
                    listCheck = false;
                    // YellowbrossExtras.LOGGER.debug("Can't spawn! " + Registry.BLOCK.getKey(blockState.getBlock()) + " nearby!");
                }
            }

            return listCheck;
        }
    }

    private boolean playerWithoutAVilvgaver(ServerLevel serverLevel) {
        List<ServerPlayer> players = serverLevel.players();
        boolean returning = false;
        for (Player player : players) {
            if (player == null) {
                returning = true;
            } else {
                List<Vilvgaver> list = serverLevel.getEntitiesOfClass(Vilvgaver.class, player.getBoundingBox().inflate(100.0D), (predicate) -> {
                    return predicate.isChallenge() || predicate.tickCount < 200;
                });

                if (list.isEmpty()) {
                    returning = true;
                }
            }
        }
        return returning;
    }
}
