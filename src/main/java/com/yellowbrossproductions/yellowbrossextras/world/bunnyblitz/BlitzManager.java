package com.yellowbrossproductions.yellowbrossextras.world.bunnyblitz;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

// Code borrowed from Custom Raids
public class BlitzManager {

    public static void tickRaids(Level world) {
        if(!world.isClientSide) {
            final WorldBlitzData data = WorldBlitzData.getInvasionData(world);
            data.tick(world);
        }
    }

    public static boolean hasRaidNearby(ServerLevel world, BlockPos pos) {
        if (world == null) {
            return false;
        }
        final List<BunnyBlitz> list = getRaids(world);
        for(BunnyBlitz r : list) {
            if(Math.abs(r.getCenter().getX() - pos.getX()) <= BunnyBlitz.getRaidRange()
                    || Math.abs(r.getCenter().getY() - pos.getY()) <= BunnyBlitz.getRaidRange()
                    || Math.abs(r.getCenter().getZ() - pos.getZ()) <= BunnyBlitz.getRaidRange()) {
                return true;
            }
        }
        return false;
    }

    public static BunnyBlitz createRaid(ServerLevel world, BlockPos pos) {
        final WorldBlitzData data = WorldBlitzData.getInvasionData(world);
        return data.createRaid(world, pos);
    }

    public static List<BunnyBlitz> getRaids(ServerLevel world) {
        return WorldBlitzData.getInvasionData(world).getRaids();
    }

    public static boolean isRaider(ServerLevel world, LivingEntity entity) {
        final WorldBlitzData data = WorldBlitzData.getInvasionData(world);
        for(BunnyBlitz raid : data.getRaids()) {
            if(raid.isRaider(entity)) {
                return true;
            }
        }
        return false;
    }
}
