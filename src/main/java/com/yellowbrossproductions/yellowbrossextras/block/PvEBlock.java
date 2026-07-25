package com.yellowbrossproductions.yellowbrossextras.block;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.init.YEItemsAndBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PvEBlock extends Block {

    public PvEBlock(Properties properties) {
        super(properties);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int size = YellowbrossExtrasConfig.pveBlocks_decayRadius.get();
        if (size > 0) {
            List<Player> list = pLevel.getEntitiesOfClass(Player.class, new AABB(pPos).inflate(size));
            if (list.isEmpty()) {
                pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
                pLevel.levelEvent(2001, pPos, Block.getId(YEItemsAndBlocks.PVE_BLOCK.get().defaultBlockState()));
            }
        }
    }
}
