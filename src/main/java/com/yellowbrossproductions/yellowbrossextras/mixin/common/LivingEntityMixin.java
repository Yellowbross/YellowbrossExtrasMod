package com.yellowbrossproductions.yellowbrossextras.mixin.common;

import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.mixin.access.LivingEntityAccess;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

// code adapted from Illage & Spillage: Respillaged
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAccess {
    @Unique
    private static final EntityDataAccessor<Boolean> SUPER_DUPER_POISON = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(SUPER_DUPER_POISON, false);
    }

    @Override
    public boolean ye$isSuperDuperPoison() {
        return this.entityData.get(SUPER_DUPER_POISON);
    }

    @Override
    public void ye$setSuperDuperPoison(boolean target) {
        this.entityData.set(SUPER_DUPER_POISON, target);
    }
}
