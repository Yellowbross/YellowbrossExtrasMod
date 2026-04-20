package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.StickFigureColorLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.StickFigureHeadLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureRenderer extends MobRenderer<StickFigureEntity, StickFigureModel<StickFigureEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/stick.png");

    public StickFigureRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StickFigureModel<>(renderManagerIn.bakeLayer(StickFigureModel.LAYER_LOCATION)), 0.3F);
        this.addLayer(new HeadItemLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer(), 1.2F));
        this.addLayer(new StickFigureColorLayer<>(this, TEXTURE));
        this.addLayer(new StickFigureHeadLayer<>(this, renderManagerIn.getEntityRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(StickFigureEntity p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(StickFigureEntity stick, float p_115456_, float p_115457_, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        super.render(stick, p_115456_, p_115457_, poseStack, bufferSource, light);
    }

    @Override
    protected void scale(StickFigureEntity p_115314_, PoseStack p_115315_, float p_115316_) {
        p_115315_.scale(0.9F, 0.9F,0.9F);
    }
}
