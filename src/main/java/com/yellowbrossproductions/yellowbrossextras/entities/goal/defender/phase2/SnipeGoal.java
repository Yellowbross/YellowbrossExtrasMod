package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.phys.Vec3;

public class SnipeGoal extends CustomAttackGoal {

    public SnipeGoal(Defender defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_snipe < 1 && getDefender().getPhase() == 2 && (getDefender().distanceTo(getDefender().getTarget()) < 40.0D);
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
    public void tick() {
        if (getDefender().attackTicks < 91) super.tick();
        else {
            getDefender().getNavigation().stop();

            if (getDefender().specialLookLocation != Vec3.ZERO) {
                getDefender().getLookControl().setLookAt(getDefender().specialLookLocation.x, getDefender().specialLookLocation.y, getDefender().specialLookLocation.z, 100.0F, 100.0F);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_snipe = getTimeInSeconds(20);
    }
}
