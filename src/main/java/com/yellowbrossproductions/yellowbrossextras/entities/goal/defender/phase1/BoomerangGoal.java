package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class BoomerangGoal extends CustomAttackGoal {

    public BoomerangGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_boomerang < 1 && getDefender().getPhase() == 1 && (!(getDefender().getTarget() instanceof Player) || getDefender().distanceTo(getDefender().getTarget()) > 12.0D);
    }

    @Override
    public void start() {
        getDefender().setAnimationState("boomerang");
        getDefender().setWeaponToShow(3);
        getDefender().attackType = getDefender().attack_boomerang;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 16;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_boomerang = getTimeInSeconds(30);
    }
}
