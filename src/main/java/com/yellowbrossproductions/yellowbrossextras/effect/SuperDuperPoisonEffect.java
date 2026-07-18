package com.yellowbrossproductions.yellowbrossextras.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public class SuperDuperPoisonEffect extends CustomMobEffect {

    public SuperDuperPoisonEffect() {
        super(MobEffectCategory.HARMFUL, 0x77B200, true);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.getHealth() > 1 && pLivingEntity.getMobType() != MobType.UNDEAD) pLivingEntity.hurt(DamageSource.MAGIC, Math.min(pLivingEntity.getHealth() - 1, 1.0F * (pAmplifier + 1)));
    }

    @Override
    public boolean isDurationEffectTick(int i, int tick) {
        return true;
    }
}
