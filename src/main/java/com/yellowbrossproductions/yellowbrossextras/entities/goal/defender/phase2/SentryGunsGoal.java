package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class SentryGunsGoal extends CustomAttackGoal {

    public SentryGunsGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_sentryguns < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("sentryguns");
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
        getDefender().cooldown_sentryguns = getTimeInSeconds(30);
        if (getDefender().getTarget() instanceof Player) getDefender().timeToWaitBeforeUsingAnyOtherAttack = 120;
    }
}
