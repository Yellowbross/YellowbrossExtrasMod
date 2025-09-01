package com.yellowbrossproductions.yellowbrossextras.entities.goal;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class StareAtDefenderGoal extends Goal {
    private final Mob entity;

    public StareAtDefenderGoal(Mob affected) {
        this.entity = affected;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        List<DefenderEntity> list = this.entity.level.getEntitiesOfClass(DefenderEntity.class, this.entity.getBoundingBox().inflate(50.0D), predicate -> {
            return predicate.isDeadOrDying() && predicate.deathAttackTicks > 0;
        });
        return !list.isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.entity instanceof Mob) {
            List<DefenderEntity> list = this.entity.level.getEntitiesOfClass(DefenderEntity.class, this.entity.getBoundingBox().inflate(50.0D), predicate -> {
                return predicate.isDeadOrDying() && predicate.deathAttackTicks > 0 && EntityUtil.isMobOnOtherTeam2(predicate, (Mob) this.entity);
            });

            if (!list.isEmpty()) {
                ((Mob) this.entity).getNavigation().stop();
                ((Mob) this.entity).setTarget(null);
                ((Mob) this.entity).getLookControl().setLookAt(
                        list.get(0).getX(),
                        list.get(0).getY() + list.get(0).getEyeHeight() + list.get(0).stareYOffsetter,
                        list.get(0).getZ(),
                        30.0F, 30.0F);
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
