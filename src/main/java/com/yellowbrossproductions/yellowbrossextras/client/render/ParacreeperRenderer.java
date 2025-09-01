package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.ParacreeperModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.SneakerModel;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.ParacreeperEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.SneakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParacreeperRenderer extends MobRenderer<ParacreeperEntity, ParacreeperModel<ParacreeperEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/creepers/paracreeper.png");

    public ParacreeperRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ParacreeperModel<>(renderManagerIn.bakeLayer(ParacreeperModel.LAYER_LOCATION)), 0.5F);
    }

    protected void scale(ParacreeperEntity p_114046_, PoseStack p_114047_, float p_114048_) {
        float f = p_114046_.getSwelling(p_114048_);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        p_114047_.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(ParacreeperEntity p_114043_, float p_114044_) {
        float f = p_114043_.getSwelling(p_114044_);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(ParacreeperEntity p_110775_1_) {
        return TEXTURE;
    }
}
