package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.phys.Vec3;

public class FlamethrowerGoal extends CustomAttackGoal {

    public FlamethrowerGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().attackType == getDefender().attack_flamethrower;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        getDefender().setImmediateTurn(true);
    }

    @Override
    public void tick() {
        getDefender().getNavigation().stop();

        if (getDefender().getAnimationState().equals("flamethrower_big") || getDefender().deathAttackTicks > 605) {
            if (getDefender().specialLookLocation != Vec3.ZERO) {
                getDefender().getLookControl().setLookAt(getDefender().specialLookLocation.x, getDefender().specialLookLocation.y, getDefender().specialLookLocation.z, 100.0F, 100.0F);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
