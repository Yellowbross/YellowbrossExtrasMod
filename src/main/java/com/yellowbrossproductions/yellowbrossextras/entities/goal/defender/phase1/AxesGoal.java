package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class AxesGoal extends CustomAttackGoal {

    public AxesGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_axes < 1 && getDefender().getPhase() == 1 && (getDefender().distanceTo(getDefender().getTarget()) > 10.0D);
    }

    @Override
    public void start() {
        getDefender().setAnimationState("axes");
        getDefender().attackType = getDefender().attack_axes;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 69;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_axes = getTimeInSeconds(10);
        getDefender().throwTimes = 1;
    }


}
