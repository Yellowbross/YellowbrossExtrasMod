package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class SwordGoal  extends CustomAttackGoal {

    public SwordGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().getPhase() == 1 && getDefender().meleeAttackType == 1;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("sword");
        getDefender().setWeaponToShow(2);
        getDefender().attackType = getDefender().attack_sword;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 24;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
