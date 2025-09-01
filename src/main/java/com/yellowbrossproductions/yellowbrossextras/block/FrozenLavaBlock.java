package com.yellowbrossproductions.yellowbrossextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FrozenLavaBlock extends Block {

    public FrozenLavaBlock(Properties properties) {
        super(properties);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void randomTick(BlockState p_221238_, ServerLevel p_221239_, BlockPos p_221240_, RandomSource p_221241_) {
        this.tick(p_221238_, p_221239_, p_221240_, p_221241_);
    }

    @Override
    public void tick(BlockState p_222945_, ServerLevel worldIn, BlockPos pos, RandomSource p_222957_) {
        if (p_222957_.nextInt(4) == 0) {
            worldIn.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
            worldIn.neighborChanged(pos, Blocks.LAVA, pos);
        }
    }
}
