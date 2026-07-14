package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.HyperSnowGolemModel;
import com.yellowbrossproductions.yellowbrossextras.entities.HyperSnowGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HyperSnowGolemRenderer extends MobRenderer<HyperSnowGolem, HyperSnowGolemModel<HyperSnowGolem>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/hyper_snow_golem.png");

    public HyperSnowGolemRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new HyperSnowGolemModel<>(renderManagerIn.bakeLayer(HyperSnowGolemModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(HyperSnowGolem p_110775_1_) {
        return TEXTURE;
    }
}
