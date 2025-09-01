package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class BoomerangGoal extends CustomAttackGoal {

    public BoomerangGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().boomerangCooldown < 1 && getDefender().getPhase() == 1;
    }

    @Override
    public void start() {
        getDefender().setAnimationState(5);
        getDefender().setWeaponToShow(3);
        getDefender().attackType = getDefender().BOOMERANG_ATTACK;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 16;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().boomerangCooldown = 600;
    }
}
