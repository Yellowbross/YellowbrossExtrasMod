package com.yellowbrossproductions.yellowbrossextras.mixin.client;

import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(method = "isShaking(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true
    )
    private void onIsShaking(LivingEntity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEntity.hasEffect(YEEffects.SUPER_DUPER_POISON.get())) {
            cir.setReturnValue(true);
        }
    }
}