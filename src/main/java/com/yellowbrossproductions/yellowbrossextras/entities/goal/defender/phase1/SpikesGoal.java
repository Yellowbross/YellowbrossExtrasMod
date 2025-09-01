package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;

public class SpikesGoal extends CustomAttackGoal {

    public SpikesGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(8) == 0 && getDefender().spikesCooldown < 1 && getDefender().getPhase() == 1 && getDefender().isOnGround();
    }

    @Override
    public void start() {
        getDefender().setAnimationState(6);
        getDefender().setWeaponToShow(4);
        getDefender().attackType = getDefender().SPIKES_ATTACK;
        getDefender().playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIN_SHORT.get(), 1.5F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 50 || getDefender().jumpAttacking || getDefender().jumpTicks > 0;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().spikesCooldown = 600;
    }
}
