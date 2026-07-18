package com.yellowbrossproductions.yellowbrossextras.entities.goal;

import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LoseAIGoal extends Goal {
    private final LivingEntity entity;

    public LoseAIGoal(LivingEntity affected) {
        this.entity = affected;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.entity.hasEffect(YEEffects.KNOCKED_OUT.get()) || this.entity.hasEffect(YEEffects.FROZEN.get());
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.entity instanceof Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
            if (!mob.hasEffect(YEEffects.FROZEN.get())) mob.getLookControl().setLookAt(this.entity.getX(), this.entity.getY(), this.entity.getZ());
        }
    }
}
