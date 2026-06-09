package com.yellowbrossproductions.yellowbrossextras.mixin.common;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.effect.CustomMobEffect;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.world.CustomExplosion;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void ye$injectOnEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
        if (this.level.isClientSide) return;

        CustomExplosion.create(this, this.getX(), this.getY() + 0.3, this.getZ(), 6.0F, true);
        if (pEffectInstance.getEffect() == YEEffects.SUPER_DUPER_POISON.get()) {
            for (ServerPlayer player : ((ServerLevel) this.level).players()) {
                if (player.getId() == this.getId()) continue;
                player.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), pEffectInstance));
            }
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void ye$injectOnEffectUpdated(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        if (this.level.isClientSide) return;

        if (pEffectInstance.getEffect() == YEEffects.SUPER_DUPER_POISON.get())
            for (ServerPlayer player : ((ServerLevel) this.level).players()) {
                if (player.getId() == this.getId()) continue;
                player.connection.send(new ClientboundUpdateMobEffectPacket(this.getId(), pEffectInstance));
            }
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void ye$injectOnEffectRemoved(MobEffectInstance pEffectInstance, CallbackInfo ci) {
        if (this.level.isClientSide) return;

        if (pEffectInstance.getEffect() == YEEffects.SUPER_DUPER_POISON.get()) {
            for (ServerPlayer player : ((ServerLevel) this.level).players()) {
                if (player.getId() == this.getId()) continue;
                player.connection.send(new ClientboundRemoveMobEffectPacket(this.getId(), pEffectInstance.getEffect()));
            }
        }
    }
}
