package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.player.Player;

public class ChainsawGoal extends CustomAttackGoal {

    public ChainsawGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().chainsawCooldown < 1 && getDefender().getPhase() == 1 && (getDefender().distanceTo(getDefender().getTarget()) > 6.0D) && (getDefender().distanceTo(getDefender().getTarget()) < 35.0D);
    }

    @Override
    public void start() {
        getDefender().setAnimationState(9);
        getDefender().setWeaponToShow(6);
        getDefender().attackType = getDefender().CHAINSAW_ATTACK;
        getDefender().setImmediateTurn(true);
        getDefender().setChainsawLookX(0.0F);
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 100;
    }

    @Override
    public void tick() {
        getDefender().getNavigation().stop();

        if (getDefender().laserX != 0 || getDefender().laserY != 0 || getDefender().laserZ != 0) {
            if (getDefender().getTarget() instanceof Player) {
                if (getDefender().attackTicks > 20) {
                    getDefender().getLookControl().setLookAt(getDefender().laserX, getDefender().laserY, getDefender().laserZ, 0.6F, 100.0F);
                }
            } else {
                getDefender().getLookControl().setLookAt(getDefender().laserX, getDefender().laserY, getDefender().laserZ, 1.5F, 100.0F);
            }
        }

        getDefender().getNavigation().stop();
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().chainsawCooldown = 200;
        getDefender().setLaserPosition(0,0,0);
    }
}
