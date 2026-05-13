package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureColorLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureHeadLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureStaffLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureRenderer extends MobRenderer<StickFigureEntity, StickFigureModel<StickFigureEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/stick.png");
    private static final ResourceLocation STAFF = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/staff.png");

    public StickFigureRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StickFigureModel<>(renderManagerIn.bakeLayer(StickFigureModel.LAYER_LOCATION)), 0.3F);
        this.addLayer(new HeadItemLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer(), 1.2F));
        this.addLayer(new StickFigureColorLayer<>(this, TEXTURE, 0xCC6600));
        this.addLayer(new StickFigureHeadLayer<>(this, renderManagerIn.getEntityRenderDispatcher()));
        this.addLayer(new StickFigureStaffLayer<>(this, STAFF));
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
        boolean kingOrChosen = true;
        float size = kingOrChosen ? 1.0F : 0.9F;
        p_115315_.scale(size, size, size);
    }
}
