package com.yellowbrossproductions.yellowbrossextras.effect;

import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.UUID;

public class KnockedOutEffect extends CustomMobEffect {

    public KnockedOutEffect() {
        super(MobEffectCategory.HARMFUL, 3484199, false);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(UUID.randomUUID()), -1.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        final Random random = new Random();
        for(int i = 0; i < 2; ++i) {
            double d0 = (-0.5 + random.nextGaussian()) / 10;
            double d1 = (-0.5 + random.nextGaussian()) / 10;
            double d2 = (-0.5 + random.nextGaussian()) / 10;
            EntityUtil.makeAParticle(pLivingEntity.level, ParticleTypes.SPLASH, false, new Vec3(pLivingEntity.getRandomX(0.5D), pLivingEntity.getRandomY(), pLivingEntity.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 2; ++i) {
            double d0 = (-0.5 + random.nextGaussian()) / 10;
            double d1 = (-0.5 + random.nextGaussian()) / 10;
            double d2 = (-0.5 + random.nextGaussian()) / 10;
            EntityUtil.makeAParticle(pLivingEntity.level, ParticleTypes.CRIT, false, new Vec3(pLivingEntity.getRandomX(0.5D), pLivingEntity.getY() + pLivingEntity.getBbHeight() + 0.2D, pLivingEntity.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
