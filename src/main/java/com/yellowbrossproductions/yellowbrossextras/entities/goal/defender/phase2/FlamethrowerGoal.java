package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class FlamethrowerGoal extends CustomAttackGoal {

    public FlamethrowerGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().attackType == getDefender().attack_flamethrower;
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
