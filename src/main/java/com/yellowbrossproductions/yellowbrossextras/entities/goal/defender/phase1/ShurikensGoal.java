package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class ShurikensGoal extends CustomAttackGoal {

    public ShurikensGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_shurikens < 1 && getDefender().getPhase() == 1;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("shurikens");
        getDefender().setWeaponToShow(5);
        getDefender().attackType = getDefender().attack_shurikens;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 50;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_shurikens = getTimeInSeconds(10);
    }
}
