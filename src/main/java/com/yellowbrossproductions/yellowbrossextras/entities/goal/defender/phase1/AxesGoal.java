package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class AxesGoal extends CustomAttackGoal {

    public AxesGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().axesCooldown < 1 && getDefender().getPhase() == 1 && (getDefender().distanceTo(getDefender().getTarget()) > 10.0D);
    }

    @Override
    public void start() {
        getDefender().setAnimationState(4);
        getDefender().attackType = getDefender().AXES_ATTACK;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 69;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().axesCooldown = 200;
        getDefender().throwTimes = 1;
    }


}
