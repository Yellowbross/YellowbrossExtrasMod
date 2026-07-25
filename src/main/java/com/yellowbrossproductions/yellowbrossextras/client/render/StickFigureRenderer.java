package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureColorLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureHeadLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky.StickFigureStaffLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigure;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureRenderer extends MobRenderer<StickFigure, StickFigureModel<StickFigure>> {
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
    public ResourceLocation getTextureLocation(StickFigure pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(StickFigure pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected void scale(StickFigure pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        boolean kingOrChosen = true;
        float size = kingOrChosen ? 1.0F : 0.9F;
        pMatrixStack.scale(size, size, size);
    }
}
