package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class CreeperGunGoal extends CustomAttackGoal {

    public CreeperGunGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(12) == 0 && getDefender().cooldown_creepergun < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("creepergun");
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_creepergun;
        getDefender().setWeaponToShow(15);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= (160 - (getDefender().getTarget() instanceof Player player && !getDefender().isInAttackSight(player) ? 40 : 0)) && !(getDefender().attackTicks > 60 && getDefender().tryToFindTarget() == null);
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_creepergun = getTimeInSeconds(20);
        if (getDefender().getTarget() instanceof Player) getDefender().timeToWaitBeforeUsingAnyOtherAttack = 40;
    }
}
