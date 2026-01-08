package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.StickFigureHeadLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureRenderer extends MobRenderer<StickFigureEntity, StickFigureModel<StickFigureEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/stick.png");
    private static final ResourceLocation HEAD = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/head.png");
    private final StickFigureModel model;

    private static final float TEXTURE_WIDTH = 16;
    private static final float TEXTURE_HEIGHT = 16;
    private static final float START_RADIUS = -0.25f;

    public StickFigureRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StickFigureModel<>(renderManagerIn.bakeLayer(StickFigureModel.LAYER_LOCATION)), 0.3F);
        this.model = this.getModel();
        this.addLayer(new HeadItemLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
        this.addLayer(new StickFigureHeadLayer<>(this, renderManagerIn.getEntityRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(StickFigureEntity p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(StickFigureEntity stick, float p_115456_, float p_115457_, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        super.render(stick, p_115456_, p_115457_, poseStack, bufferSource, light);

        // Head
        // poseStack.pushPose();
        // this.model.translateToSticksHead(poseStack);
        // poseStack.translate(0.0D, 3.3D, 0.0D);
        // VertexConsumer sprite = bufferSource.getBuffer(RenderType.entityTranslucent(HEAD));
        // renderHead(poseStack, sprite, light, 1.0F);
        // poseStack.popPose();
    }

    private void renderHead(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(matrixStackIn, builder, packedLightIn, warning);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning) {
        float minU = 0 + 16F / TEXTURE_WIDTH * 10;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, warning, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1 * alpha, 1 * alpha, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
