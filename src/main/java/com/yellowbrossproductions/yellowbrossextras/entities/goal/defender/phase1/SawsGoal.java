package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1;

import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.CustomAttackGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;

public class SawsGoal extends CustomAttackGoal {
    public SawsGoal(DefenderEntity defender) {
        super(defender);
    }

    @Override
    public boolean canUse() {
        return getDefender().doesAttackMeetNormalRequirements() && getRandom().nextInt(16) == 0 && getDefender().sawsCooldown < 1 && getDefender().getPhase() == 1 && getDefender().getTarget() != null && getDefender().getTarget().getBlockStateOn() != Blocks.AIR.defaultBlockState();
    }

    @Override
    public void start() {
        getDefender().setAnimationState(1);
        getDefender().attackType = getDefender().SAWS_ATTACK;
    }

    @Override
    public boolean canContinueToUse() {
        return getDefender().attackTicks <= 88;
    }

    @Override
    public void stop() {
        super.stop();
        getDefender().sawsCooldown = 200;
    }
}