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

    public void randomTick(BlockState p_221238_, ServerLevel p_221239_, BlockPos p_221240_, RandomSource p_221241_) {
        this.tick(p_221238_, p_221239_, p_221240_, p_221241_);
    }

    @Override
    public void tick(BlockState p_222945_, ServerLevel worldIn, BlockPos pos, RandomSource p_222957_) {
        int size = YellowbrossExtrasConfig.pveBlocks_decayRadius.get();
        if (size > 0) {
            List<Player> list = worldIn.getEntitiesOfClass(Player.class, new AABB(pos).inflate(size));
            if (list.isEmpty()) {
                worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                worldIn.levelEvent(2001, pos, Block.getId(YEItemsAndBlocks.PVE_BLOCK.get().defaultBlockState()));
            }
        }
    }
}
