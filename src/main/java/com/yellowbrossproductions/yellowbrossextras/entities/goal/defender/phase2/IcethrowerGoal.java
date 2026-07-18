package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.world.entity.player.Player;

public class IcethrowerGoal extends CustomAttackGoal {

    public IcethrowerGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(12) == 0 && getDefender().cooldown_icethrower < 1 && getDefender().getPhase() == 2;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("icethrower");
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_icethrower;
        getDefender().setWeaponToShow(14);
        getDefender().playSound(YESoundEvents.ENTITY_DEFENDER_ICETHROWER_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 60;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_icethrower = getTimeInSeconds(30);
        if (getDefender().getTarget() instanceof Player) getDefender().timeToWaitBeforeUsingAnyOtherAttack = 60;
    }
}
