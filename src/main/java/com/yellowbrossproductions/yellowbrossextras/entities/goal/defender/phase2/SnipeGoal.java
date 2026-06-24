package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class SnipeGoal extends CustomAttackGoal {

    public SnipeGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_snipe < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("snipe");
        getDefender().setImmediateTurn(true);
        getDefender().setWeaponToShow(12);
        getDefender().attackType = getDefender().attack_snipe;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 120;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_snipe = getTimeInSeconds(20);
    }
}
