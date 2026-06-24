package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.ChainsawEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ChainsawRenderer extends EntityRenderer<ChainsawEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/chainsaw.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3f;
    private static final float BEAM_RADIUS = 0.5f;
    private final Random random = new Random();

    public ChainsawRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Vec3 getRenderOffset(ChainsawEntity pEntity, float pPartialTicks) {
        return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
    }

    @Override
    public void render(ChainsawEntity beam, float entityYaw, float delta, PoseStack poseStack, MultiBufferSource pBuffer, int light) {
        double collidePosX = beam.prevCollidePosX + (beam.collidePosX - beam.prevCollidePosX) * delta;
        double collidePosY = beam.prevCollidePosY + (beam.collidePosY - beam.prevCollidePosY) * delta;
        double collidePosZ = beam.prevCollidePosZ + (beam.collidePosZ - beam.prevCollidePosZ) * delta;
        double posX = beam.xo + (beam.getX() - beam.xo) * delta;
        double posY = beam.yo + (beam.getY() - beam.yo) * delta;
        double posZ = beam.zo + (beam.getZ() - beam.zo) * delta;
        float yaw = beam.prevYaw + (beam.renderYaw - beam.prevYaw) * delta;
        float pitch = beam.prevPitch + (beam.renderPitch - beam.prevPitch) * delta;

        double end = Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        float length = Math.min(((beam.tickCount + delta) - 1) * 3, ((float) end));
        int frame = Mth.floor((2 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }

        VertexConsumer ivertexbuilder = pBuffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(beam)));

        if (beam.tickCount > 1) {
            renderBeam(frame, length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, poseStack, ivertexbuilder, light);
        }

        poseStack.pushPose();
        poseStack.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        if (length == ((float) end)) {
            renderEnd(frame, beam.blockSide, poseStack, ivertexbuilder, light);
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ChainsawEntity pEntity) {
        return TEXTURE;
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0 + 16F / TEXTURE_WIDTH * 10;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderStart(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderEnd(int frame, Direction side, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
    }

    private void drawBeam(int frame, float length, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 2;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, 0, 0, minU, minV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, length, 0, minU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, length, 0, maxU, maxV, 1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, 0, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderBeam(int frame, float length, float yaw, float pitch, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(new Quaternion(90, 0, 0, true));
        matrixStackIn.mulPose(new Quaternion(0, 0, yaw - 90f, true));
        matrixStackIn.mulPose(new Quaternion(-pitch, 0, 0, true));
        matrixStackIn.pushPose();
        drawBeam(frame, length, matrixStackIn, builder, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
