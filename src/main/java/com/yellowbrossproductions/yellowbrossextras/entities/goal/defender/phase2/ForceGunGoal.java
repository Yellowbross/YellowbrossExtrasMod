package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.world.phys.Vec3;

public class ForceGunGoal extends CustomAttackGoal {

    public ForceGunGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().getPhase() == 2 && getDefender().meleeAttackType == 1;
    }

    @Override
    public void start() {
        getDefender().setAnimationState("forcegun");
        getDefender().setWeaponToShow(11);
        getDefender().attackType = getDefender().attack_forcegun;
        getDefender().setImmediateTurn(true);
        getDefender().playSound(YESoundEvents.ENTITY_DEFENDER_FORCEGUN_START.get(), 1.0F, 1.0F);

        if (getDefender().tryToFindTarget() != null) getDefender().setSpecialLookLocation(getDefender().tryToFindTarget().getPosition(0).add(0, getDefender().tryToFindTarget().getEyeHeight(), 0));
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 20;
    }

    @Override
    public void tick() {
        getDefender().getNavigation().stop();

        if (getDefender().specialLookLocation != Vec3.ZERO) {
            getDefender().getLookControl().setLookAt(getDefender().specialLookLocation.x, getDefender().specialLookLocation.y, getDefender().specialLookLocation.z, 100.0F, 100.0F);
        }

        getDefender().getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
