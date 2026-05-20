package com.yellowbrossproductions.yellowbrossextras.entities.goal;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StareAtDefenderGoal extends Goal {
    private final Mob mob;
    private final DefenderEntity defender;

    public StareAtDefenderGoal(Mob affected, DefenderEntity defender) {
        this.mob = affected;
        this.defender = defender;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return YellowbrossExtrasConfig.defender_distractEnemies.get() && defender != null && defender.isDeadOrDying() && defender.deathAttackTicks > 0 && !defender.isRemoved();
    }

    @Override
    public void tick() {
        if (mob == null || defender == null) return;

        mob.getNavigation().stop();
        mob.setTarget(null);
        mob.getLookControl().setLookAt(defender.getX(), defender.getY() + defender.stareYOffsetter, defender.getZ(), 30.0F, 30.0F);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }
}
