package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class ShurikensGoal extends CustomAttackGoal {

    public ShurikensGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().shurikensCooldown < 1 && getDefender().getPhase() == 1;
    }

    @Override
    public void start() {
        getDefender().setAnimationState(8);
        getDefender().setWeaponToShow(5);
        getDefender().attackType = getDefender().SHURIKENS_ATTACK;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 30;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().shurikensCooldown = 200;
    }
}
