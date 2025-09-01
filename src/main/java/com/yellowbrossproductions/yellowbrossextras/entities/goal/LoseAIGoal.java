package com.yellowbrossproductions.yellowbrossextras.entities.goal;

import com.yellowbrossproductions.yellowbrossextras.util.EffectRegisterer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.SpellcasterIllager;

import java.util.EnumSet;

public class LoseAIGoal extends Goal {
    private final LivingEntity entity;

    public LoseAIGoal(LivingEntity affected) {
        this.entity = affected;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.entity.hasEffect(EffectRegisterer.KNOCKED_OUT.get());
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.entity instanceof Mob) {
            ((Mob) this.entity).getNavigation().stop();
            ((Mob) this.entity).setTarget(null);
            ((Mob) this.entity).getLookControl().setLookAt(this.entity.getX(), this.entity.getY(), this.entity.getZ());
        }
    }
}
