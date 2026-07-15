package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.WitherExplosion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class WitherExplosionRenderer extends EntityRenderer<WitherExplosion> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/witherbazooka/explosion.png");
    private final Random random = new Random();

    private static final float TEXTURE_WIDTH = 342;
    private static final float TEXTURE_HEIGHT = 452;

    private static final float BODY_WIDTH = 152;
    private static final float BODY_HEIGHT = 196;
    private static final float PARTICLE_SIZE = 64;
    private static final float DIE_WIDTH = 190;
    private static final float DIE_HEIGHT = 109;

    private static final int fadeAfter = 60;

    public WitherExplosionRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(WitherExplosion pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        pPoseStack.translate(this.getRenderOffset(pEntity, pPartialTick).x, this.getRenderOffset(pEntity, pPartialTick).y, this.getRenderOffset(pEntity, pPartialTick).z);

        VertexConsumer sprite = pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        renderBody(pPoseStack, sprite, pEntity.tickCount, pPartialTick);

        float age = pEntity.tickCount + pPartialTick;
        for (int i = 1; i <= 5; ++i) {
            for (int i2 = 0; i2 < 20; ++i2) {
                float calculation = age * 0.75f / 20;
                float mult;
                if (i == 1) mult = 2.5f + (age / 20);
                else mult = (5.25f - i + (age / 30)) * 0.75f;
                float x = (float) (Math.sin((i2 / 5.0f) * (Math.PI / 2)) * mult);
                float z = (float) (Math.cos((i2 / 5.0f) * (Math.PI / 2)) * mult);
                float y = switch(i) {
                    case 1 -> 0.5f;
                    case 2 -> calculation + 4.0f;
                    case 3 -> calculation + 5.0f;
                    case 4 -> calculation + 5.5f;
                    case 5 -> calculation + 5.75f;
                    default -> 0;
                };
                Vector3f vec = new Vector3f(x, y, z);

                renderParticle(pPoseStack, sprite, pEntity.tickCount, pPartialTick, vec);
            }
        }

        renderDie(pPoseStack, sprite, pEntity.tickCount, pPartialTick);

        if (pEntity.tickCount <= 10) renderExplosion(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE)), pEntity.tickCount, pPartialTick);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(WitherExplosion pEntity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(WitherExplosion pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public Vec3 getRenderOffset(WitherExplosion pEntity, float pPartialTicks) {
        float ranMult = 0.05f;
        return new Vec3((-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult);
    }

    private void renderBody(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot() + 180.0f));
        float age = ticks + partialTick;

        float size = 3.25f;
        float height = 0.75f;
        float mult = 0.75f;
        float calculation = age * mult / 20;
        poseStack.scale(size - calculation, (size * height) + calculation, size - calculation);
        poseStack.translate(0, 0.75 + (calculation / 20), 0);
        this.drawBody(poseStack, buffer, ticks > 40 ? (age - 40) / 20 : 0);
        poseStack.popPose();
    }

    private void renderParticle(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick, Vector3f vector3f) {
        poseStack.pushPose();
        poseStack.translate(vector3f.x(), vector3f.y(), vector3f.z());
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float age = ticks + partialTick;

        float size = 1.0f;
        float mult = 6.0f;
        float calculation = age * mult;
        poseStack.scale(size, size, size);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(calculation));
        this.drawParticle(poseStack, buffer, ticks > 40 ? (age - 40) / 20 : 0);
        poseStack.popPose();
    }

    private void renderDie(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick) {
        poseStack.pushPose();
        float ranMult = 0.2f;
        float age = ticks + partialTick;

        float width = DIE_WIDTH / DIE_HEIGHT;
        float size = 3.0f;
        float mult = 1.5f;
        float calculation = age * mult / 20;
        poseStack.scale((size * width) + (calculation / 10), size + (calculation / 10), (size * width) + (calculation / 10));
        poseStack.translate((-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult);
        poseStack.translate(0, 3.25f + (calculation / 8), 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        this.drawDie(poseStack, buffer, ticks > 40 ? (age - 40) / 20 : 0);
        poseStack.popPose();
    }

    private void renderExplosion(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float age = 1 + ticks + partialTick;

        float size = 1000.0f;
        float mult = 50.0f;
        float calculation = age * mult;
        poseStack.scale(size / calculation, size / calculation, size / calculation);
        this.drawExplosion(poseStack, buffer);
        poseStack.popPose();
    }

    private void drawBody(PoseStack matrixStackIn, VertexConsumer builder, float age) {
        float minU = 0;
        float minV = 0;
        float maxU = BODY_WIDTH / TEXTURE_WIDTH;
        float maxV = BODY_HEIGHT / TEXTURE_HEIGHT;
        float alpha = 1 - age;
        Matrix4f pose = matrixStackIn.last().pose();
        Matrix3f normal = matrixStackIn.last().normal();
        drawVertex(pose, normal, builder, 1, 1, 0, minU, minV, alpha, 15);
        drawVertex(pose, normal, builder, 1, -1, 0, minU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, -1, 0, maxU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, 1, 0, maxU, minV, alpha, 15);
    }

    private void drawParticle(PoseStack matrixStackIn, VertexConsumer builder, float age) {
        float minU = BODY_WIDTH / TEXTURE_WIDTH;
        float minV = 0;
        float maxU = (BODY_WIDTH + PARTICLE_SIZE) / TEXTURE_WIDTH;
        float maxV = PARTICLE_SIZE / TEXTURE_HEIGHT;
        float alpha = 1 - age;
        Matrix4f pose = matrixStackIn.last().pose();
        Matrix3f normal = matrixStackIn.last().normal();
        drawVertex(pose, normal, builder, 1, 1, 0, minU, minV, alpha, 15);
        drawVertex(pose, normal, builder, 1, -1, 0, minU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, -1, 0, maxU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, 1, 0, maxU, minV, alpha, 15);
    }

    private void drawDie(PoseStack matrixStackIn, VertexConsumer builder, float age) {
        float minU = BODY_WIDTH / TEXTURE_WIDTH;
        float minV = PARTICLE_SIZE / TEXTURE_HEIGHT;
        float maxU = (BODY_WIDTH + DIE_WIDTH) / TEXTURE_WIDTH;
        float maxV = (PARTICLE_SIZE + DIE_HEIGHT) / TEXTURE_HEIGHT;
        float alpha = 1 - age;
        Matrix4f pose = matrixStackIn.last().pose();
        Matrix3f normal = matrixStackIn.last().normal();
        drawVertex(pose, normal, builder, 1, 1, 0, minU, minV, alpha, 15);
        drawVertex(pose, normal, builder, 1, -1, 0, minU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, -1, 0, maxU, maxV, alpha, 15);
        drawVertex(pose, normal, builder, -1, 1, 0, maxU, minV, alpha, 15);
    }

    private void drawExplosion(PoseStack matrixStackIn, VertexConsumer builder) {
        float minU = 0;
        float minV = BODY_HEIGHT / TEXTURE_HEIGHT;
        float maxU = 256 / TEXTURE_WIDTH;
        float maxV = (BODY_HEIGHT + 256) / TEXTURE_HEIGHT;
        Matrix4f pose = matrixStackIn.last().pose();
        Matrix3f normal = matrixStackIn.last().normal();
        drawVertex(pose, normal, builder, 1, 1, 0, minU, minV, 1, 15);
        drawVertex(pose, normal, builder, 1, -1, 0, minU, maxV, 1, 15);
        drawVertex(pose, normal, builder, -1, -1, 0, maxU, maxV, 1, 15);
        drawVertex(pose, normal, builder, -1, 1, 0, maxU, minV, 1, 15);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
