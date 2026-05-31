package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;

public class SpikesGoal extends CustomAttackGoal {

    public SpikesGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(8) == 0 && getDefender().cooldown_spikes < 1 && getDefender().getPhase() == 1 && getDefender().isOnGround();
    }

    @Override
    public void start() {
        getDefender().setAnimationState("spikes");
        getDefender().setWeaponToShow(4);
        getDefender().attackType = getDefender().attack_spikes;
        getDefender().playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIN_SHORT.get(), 1.5F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 50 || getDefender().jumpAttacking || getDefender().jumpTicks > 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (getDefender().attackTicks == 39 && getDefender().tryToFindTarget() == null) this.getDefender().attackTicks = 10000;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_spikes = getTimeInSeconds(30);
    }
}
