package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class PoisonDartsGoal extends CustomAttackGoal {

    public PoisonDartsGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_poisondarts < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("poisondarts");
        getDefender().setImmediateTurn(true);
        getDefender().setWeaponToShow(10);
        getDefender().attackType = getDefender().attack_poisondarts;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 30;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_poisondarts = getTimeInSeconds(15);
    }
}
