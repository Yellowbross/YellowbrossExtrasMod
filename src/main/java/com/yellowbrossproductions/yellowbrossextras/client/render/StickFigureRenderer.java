package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureRenderer extends MobRenderer<StickFigureEntity, StickFigureModel<StickFigureEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public StickFigureRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StickFigureModel<>(renderManagerIn.bakeLayer(StickFigureModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(StickFigureEntity p_110775_1_) {
        return TEXTURE;
    }
}
