package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DefenderModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class DefenderRenderer extends MobRenderer<DefenderEntity, DefenderModel<DefenderEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/defender.png");
    private final Random random = new Random();

    private static final ResourceLocation SPINNY_P2 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/phase2_spinny.png");

    private static final float TEXTURE_WIDTH = 64;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = -1f;

    public DefenderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DefenderModel<>(renderManagerIn.bakeLayer(DefenderModel.LAYER_LOCATION)), 0.6F);
    }

    @Override
    public Vec3 getRenderOffset(DefenderEntity p_114483_, float p_114484_) {
        return new Vec3(
                (this.random.nextGaussian() * 0.001D) * p_114483_.getShakeMultiplier(),
                0.0D,
                (this.random.nextGaussian() * 0.001D) * p_114483_.getShakeMultiplier()
        );
    }

    @Override
    protected void scale(DefenderEntity defender, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.0F / (defender.getStretch() + 1),
                (defender.getStretch() + 1),
                1.0F / (defender.getStretch() + 1));
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderEntity p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(DefenderEntity defender, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int blockLightIn) {
        switch (defender.getCustomRender()) {
            case 1:
                renderSprite1(defender, entityYaw, partialTick, poseStack, multiBufferSource, blockLightIn, 1.0F);
                break;

            default: super.render(defender, entityYaw, partialTick, poseStack, multiBufferSource, blockLightIn);
        }
    }

    public void renderSprite1(DefenderEntity defender, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.35D, 0.0D);

        float f = defender.getWobbling(partialTick);

        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;

        float f1 = 1.0F + Mth.sin(f * 10.0F) * f * 0.5F;

        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.4F) / f1;

        poseStack.scale(f2, f3, f2);

        VertexConsumer sprite = multiBufferSource.getBuffer(RenderType.entityTranslucent(SPINNY_P2));
        float ageInTicks = defender.tickCount + partialTick;
        int loop = (int) (ageInTicks) % 4;
        renderSpin(loop, poseStack, sprite, light, alpha, ageInTicks > 4);
        poseStack.popPose();
    }

    private void renderSpin(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float alpha, boolean startOver) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, alpha, startOver);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning, boolean startOver) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = startOver ? -16F / TEXTURE_HEIGHT : 0;
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
