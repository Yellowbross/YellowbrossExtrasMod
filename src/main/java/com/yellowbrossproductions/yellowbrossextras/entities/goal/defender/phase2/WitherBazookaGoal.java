package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class WitherBazookaGoal extends CustomAttackGoal {

    public WitherBazookaGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_witherbazooka < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("witherbazooka");
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_witherbazooka;
        getDefender().setWeaponToShow(13);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= (160 - (getDefender().getTarget() instanceof Player player && !getDefender().isInAttackSight(player) ? 40 : 0)) && !(getDefender().attackTicks > 60 && getDefender().tryToFindTarget() == null);
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_witherbazooka = getTimeInSeconds(20);
    }
}
