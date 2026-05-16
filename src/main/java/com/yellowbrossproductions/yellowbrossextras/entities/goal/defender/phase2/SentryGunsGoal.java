package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;

public class SentryGunsGoal extends CustomAttackGoal {

    public SentryGunsGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_sentryguns < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState(22);
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_sentryguns;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 33;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_sentryguns = 600;
        getDefender().timeToWaitBeforeUsingAnyOtherAttack = 120;
    }
}
