package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;

public class RatatatabowGoal extends CustomAttackGoal {

    public RatatatabowGoal(DefenderEntity defender) {
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
        getDefender().playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_RATATATABOW_WARN.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 100 || getDefender().attackTicks2 <= 100;
    }

    @Override
    public void tick() {
        super.tick();

        if (getDefender().tryToFindTarget() == null && getDefender().isOnGround()) {
            this.getDefender().attackTicks = 10000;
            this.getDefender().attackTicks2 = 10000;
        }
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_ratatatabow = getTimeInSeconds(10);
    }
}
