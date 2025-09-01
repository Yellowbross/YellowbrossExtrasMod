package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class ExcaliburGoal extends CustomAttackGoal {

    public ExcaliburGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().attackType == getDefender().EXCALIBUR_ATTACK;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        getDefender().setImmediateTurn(true);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
