package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Random;

public class CustomAttackGoal extends Goal {
    DefenderEntity defender;
    Random random = new Random();

    public CustomAttackGoal(DefenderEntity defender) {
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.defender = defender;
    }

    public DefenderEntity getDefender() {
        return this.defender;
    }

    public Random getRandom() {
        return random;
    }

    public boolean doesAttackMeetNormalRequirements() {
        return getDefender().attackType == 0 && getDefender().getTarget() != null && getDefender().isInAttackSight(getDefender().getTarget()) && getDefender().getTarget().isAlive() && !((getDefender().damageTaken >= 30.0F) && getDefender().isOnGround()) && !getDefender().isRemoved() && getDefender().deathAttackTicks <= 0 && getDefender().timeToWaitBeforeUsingAnyOtherAttack < 1;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.defender.getNavigation().stop();

        LivingEntity entity = this.defender.tryToFindTarget();
        if (entity != null) {
            this.defender.getLookControl().setLookAt(entity, 100.0F, 100.0F);
        }

        this.defender.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.defender.attackTicks = 0;
        this.defender.attackType = 0;
        this.defender.setAnimationState(0);
        this.defender.setWeaponToShow(0);
        this.defender.meleeAttackType = 0;
        this.defender.setShakeMultiplier(0);
        this.defender.setImmediateTurn(false);
        this.defender.stretchHelper = 0;
        this.defender.setStretch(0);
        this.defender.averageXCord = 0;
        this.defender.averageZCord = 0;
        this.defender.slamTicks = 0;
        this.defender.attackTicks2 = 0;
        this.defender.setCustomRender(0);
    }
}
