package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WitherBazookaGoal extends CustomAttackGoal {

    public WitherBazookaGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_witherbazooka < 1 && getDefender().getPhase() == 2 && getDefender().getTarget().isOnGround();
    }

    @Override
    public void start() {
        getDefender().setAnimationState("witherbazooka");
        getDefender().setImmediateTurn(true);
        getDefender().attackType = getDefender().attack_witherbazooka;
        getDefender().setWeaponToShow(13);
        getDefender().playSound(YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_START.get(), 2.0F, 1.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 40 || getDefender().shouldDiscardFriction() || getDefender().attackTicks2 > 0 && getDefender().attackTicks2 < 26;
    }

    @Override
    public void tick() {
        getDefender().getNavigation().stop();

        if (getDefender().specialLookLocation != Vec3.ZERO && getDefender().attackTicks2 < 1) {
            getDefender().getLookControl().setLookAt(getDefender().specialLookLocation.x, getDefender().specialLookLocation.y, getDefender().specialLookLocation.z, 100.0F, 100.0F);
        }
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().setDiscardFriction(false);
        getDefender().cooldown_witherbazooka = getTimeInSeconds(60);
    }
}
