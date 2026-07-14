package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class ExcaliburGoal extends CustomAttackGoal {

    public ExcaliburGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().attackType == getDefender().attack_excalibur;
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
