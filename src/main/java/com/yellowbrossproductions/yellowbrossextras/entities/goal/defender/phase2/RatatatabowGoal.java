package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;

public class RatatatabowGoal extends CustomAttackGoal {

    public RatatatabowGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_ratatatabow < 1 && getDefender().getPhase() == 2 && (getDefender().distanceTo(getDefender().getTarget()) > 15.0D) && (getDefender().distanceTo(getDefender().getTarget()) < 25.0D);
    }

    @Override
    public void start() {
        getDefender().setAnimationState("ratatatabow");
        getDefender().setWeaponToShow(9);
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_ratatatabow;
        getDefender().playSound(YESoundEvents.ENTITY_DEFENDER_RATATATABOW_WARN.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return (getDefender().attackTicks <= 100 || getDefender().attackTicks2 <= 100) && !(getDefender().tryToFindTarget() == null && getDefender().isOnGround());
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_ratatatabow = getTimeInSeconds(10);
    }
}
