package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.level.block.Blocks;

public class SawsGoal extends CustomAttackGoal {
    public SawsGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().cooldown_saws < 1 && getDefender().getPhase() == 1 && getDefender().getTarget() != null && getDefender().getTarget().getBlockStateOn() != Blocks.AIR.defaultBlockState();
    }

    @Override
    public void start() {
        getDefender().setAnimationState("saws");
        getDefender().attackType = getDefender().attack_saws;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 88;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().cooldown_saws = 200;
    }
}