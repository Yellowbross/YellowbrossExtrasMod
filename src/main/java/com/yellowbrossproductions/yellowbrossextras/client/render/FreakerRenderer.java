package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.FreakerModel;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.Freaker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FreakerRenderer extends MobRenderer<Freaker, FreakerModel<Freaker>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/creepers/freaker.png");

    public FreakerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new FreakerModel<>(renderManagerIn.bakeLayer(FreakerModel.LAYER_LOCATION)), 2.5F);
    }

    protected void scale(Freaker creeper, PoseStack poseStack, float partialTick) {
        float f = creeper.getSwelling(partialTick);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(Freaker p_114043_, float p_114044_) {
        float f = p_114043_.getSwelling(p_114044_);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Freaker p_110775_1_) {
        return TEXTURE;
    }
}
