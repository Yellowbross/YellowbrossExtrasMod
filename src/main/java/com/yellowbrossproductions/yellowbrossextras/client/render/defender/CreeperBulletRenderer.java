package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.CreeperBulletModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.CreeperBulletChargeLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperBulletRenderer extends MobRenderer<CreeperBullet, CreeperBulletModel<CreeperBullet>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/defender.png");

    public CreeperBulletRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CreeperBulletModel<>(renderManagerIn.bakeLayer(CreeperBulletModel.LAYER_LOCATION), false), 0.25F);
        this.addLayer(new CreeperBulletChargeLayer(this, renderManagerIn.getModelSet()));
    }

    protected void scale(CreeperBullet creeper, PoseStack poseStack, float partialTick) {
        float f = creeper.getSwelling(partialTick);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2 * 0.6f, f3 * 0.6f, f2 * 0.6f);
    }

    protected float getWhiteOverlayProgress(CreeperBullet pLivingEntity, float pPartialTicks) {
        float f = pLivingEntity.getSwelling(pPartialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperBullet pEntity) {
        return TEXTURE;
    }
}
