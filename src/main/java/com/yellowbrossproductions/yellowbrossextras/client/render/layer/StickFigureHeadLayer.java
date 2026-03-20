package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.YERenderTypes;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import com.yellowbrossproductions.yellowbrossextras.client.render.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// THANK YOU DARKPEASANT THANK YOU THANK YOU THANK YOUUUUUUUUUUU
@OnlyIn(Dist.CLIENT)
public class StickFigureHeadLayer<T extends StickFigureEntity, M extends StickFigureModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation HEAD = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/stick_figure/head.png");

    private static final float TEXTURE_WIDTH = 16;
    private static final float TEXTURE_HEIGHT = 16;
    private static final float START_RADIUS = -0.25f;
    private final EntityRenderDispatcher entityRenderDispatcher;

    public StickFigureHeadLayer(RenderLayerParent<T, M> pRenderer, EntityRenderDispatcher pDispatcher) {
        super(pRenderer);
        this.entityRenderDispatcher = pDispatcher;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pPoseStack.pushPose();

        PoseStack headStack = new PoseStack();
        headStack.pushPose();
        RenderUtil.translatePoseStackToPart(this.getParentModel(), headStack, "head");
        Matrix4f patrick = headStack.last().pose();
        Vector4f headPos = new Vector4f(0, 0, 0, 1);
        headPos.transform(patrick);
        pPoseStack.translate(headPos.x(), headPos.y(), headPos.z());
        headStack.popPose();

        pPoseStack.scale(-1, 1, 1);
        // pPoseStack.translate(0, START_RADIUS / 2, 0);
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(RenderUtil.getDefaultBodyRot(pLivingEntity, pPartialTick)));
        pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        this.renderFlatQuad(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(HEAD)));
        this.renderFlatQuad(pPoseStack, pBuffer.getBuffer(YERenderTypes.stickFigure(HEAD, false)));
        pPoseStack.popPose();
    }

    private void renderFlatQuad(PoseStack pPoseStack, VertexConsumer pBuffer) {
        float minU = 0 + 16F / TEXTURE_WIDTH * 10;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        Matrix4f pose = pPoseStack.last().pose();
        Matrix3f normal = pPoseStack.last().normal();
        this.drawVertex(pose, normal, pBuffer, -START_RADIUS, -START_RADIUS, minU, minV);
        this.drawVertex(pose, normal, pBuffer, -START_RADIUS, START_RADIUS, minU, maxV);
        this.drawVertex(pose, normal, pBuffer, START_RADIUS, START_RADIUS, maxU, maxV);
        this.drawVertex(pose, normal, pBuffer, START_RADIUS, -START_RADIUS, maxU, minV);
    }

    private void drawVertex(Matrix4f pPose, Matrix3f pNormals, VertexConsumer pBuffer, float pOffsetX, float pOffsetY, float pTextureX, float pTextureY) {
        pBuffer.vertex(pPose, pOffsetX, pOffsetY, 0)
                .color(1F, 1F, 1F, 1F)
                .uv(pTextureX, pTextureY)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15)
                .normal(pNormals, 0, 1, 0)
                .endVertex();
    }
}
