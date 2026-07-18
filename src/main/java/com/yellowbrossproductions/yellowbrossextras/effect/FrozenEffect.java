package com.yellowbrossproductions.yellowbrossextras.effect;

import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class FrozenEffect extends CustomMobEffect {

    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0x92B9FE, true);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(UUID.randomUUID()), -1.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.setTicksFrozen(pLivingEntity.getTicksRequiredToFreeze() + 20);
        pLivingEntity.setDeltaMovement(0, pLivingEntity.getDeltaMovement().y, 0);
        EntityUtil.makeAParticle(pLivingEntity.level, ParticleTypes.SNOWFLAKE, false, new Vec3(pLivingEntity.getRandomX(1.0D), pLivingEntity.getRandomY(), pLivingEntity.getRandomZ(1.0D)), new Vec3(0, -0.1, 0));
    }

    @Override
    public boolean isDurationEffectTick(int i, int tick) {
        return true;
    }
}
