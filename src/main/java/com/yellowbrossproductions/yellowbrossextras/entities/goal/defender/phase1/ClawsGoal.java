package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class ClawsGoal extends CustomAttackGoal {

    public ClawsGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().getPhase() == 1 && getDefender().meleeAttackType == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState(10);
        getDefender().setWeaponToShow(7);
        getDefender().attackType = getDefender().CLAWS_ATTACK;
        getDefender().setImmediateTurn(true);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 26 || getDefender().shouldContinueAttacking || getDefender().clawsPunchTimer > 0;
    }

    @Override
    public void tick() {
        boolean target = getDefender().clawsTarget != null && !getDefender().clawsTarget.isRemoved() && getDefender().clawsTarget.isAlive();
        if (!(getDefender().attackTicks >= 22 && getDefender().attackTicks <= 30)) {
            if (target) {
                getDefender().getNavigation().stop();

                if (getDefender().clawsTarget != null) {
                    getDefender().getLookControl().setLookAt(getDefender().clawsTarget, 100.0F, 100.0F);
                }

                getDefender().getNavigation().stop();
            } else {
                super.tick();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().clawsCooldown = 600;
        getDefender().clawsTarget = null;
        getDefender().itsTimeToClawTarget = false;
    }
}
