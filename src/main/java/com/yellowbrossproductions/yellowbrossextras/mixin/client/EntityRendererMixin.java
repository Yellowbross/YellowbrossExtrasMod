package com.yellowbrossproductions.yellowbrossextras.mixin.client;

import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Unique
    private final RandomSource random = RandomSource.create();

    @Inject(method = "getRenderOffset(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"), cancellable = true)
    private void onGetRenderOffset(Entity pEntity, float pPartialTicks, CallbackInfoReturnable<Vec3> cir) {
        if (pEntity instanceof LivingEntity living && living.getHealth() > 0) {
            Vec3 originalOffset = cir.getReturnValue();
            Vec3 customOffset = new Vec3(
                    this.random.nextGaussian() * (0.03D * ((living.getMaxHealth() / living.getHealth()) / living.getMaxHealth())),
                    0.0D,
                    this.random.nextGaussian() * (0.03D * ((living.getMaxHealth() / living.getHealth()) / living.getMaxHealth())));
            if (living.hasEffect(YEEffects.SUPER_DUPER_POISON.get()) && originalOffset != null) cir.setReturnValue(originalOffset.add(customOffset));
        }
    }
}
