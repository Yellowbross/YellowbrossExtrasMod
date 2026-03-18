package com.yellowbrossproductions.yellowbrossextras.entities.goal;

import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BlitzManager;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BunnyBlitz;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RunTowardBlitzGoal extends Goal {
    PathfinderMob mob;
    Level level;
    BlockPos pos;
    ServerLevel serverLevel = null;

    public RunTowardBlitzGoal(PathfinderMob entity) {
        this.mob = entity;
        this.level = entity.getLevel();
        this.pos = entity.blockPosition();
        if (this.level instanceof ServerLevel) {
            this.serverLevel = (ServerLevel) entity.getLevel();
        }
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return BlitzManager.hasRaidNearby(this.serverLevel, this.pos) && this.mob.getTarget() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return BlitzManager.hasRaidNearby(this.serverLevel, this.pos) && this.mob.getTarget() == null;
    }

    public void tick() {
        if (this.serverLevel != null) {
            if (BlitzManager.hasRaidNearby(this.serverLevel, this.pos)) {
                if (!this.mob.isPathFinding()) {
                    BunnyBlitz blitz = EntityUtil.findNearestBlitz(this.mob, this.serverLevel);
                    if (blitz != null) {
                        Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 15, 4, Vec3.atBottomCenterOf(blitz.getCenter()), (double)((float)Math.PI / 2F));
                        if (vec3 != null) {
                            this.mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.5D);
                        }
                    }
                }
            }
        }

    }
}
