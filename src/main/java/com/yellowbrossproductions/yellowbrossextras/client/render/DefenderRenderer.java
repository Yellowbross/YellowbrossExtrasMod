package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.DefenderModel;
import com.yellowbrossproductions.yellowbrossextras.entities.DefenderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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
    private static final float TEXTURE_HEIGHT = 16;
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
    protected void scale(DefenderEntity p_115314_, PoseStack p_115315_, float p_115316_) {
        p_115315_.scale(1.0F / (p_115314_.getStretch() + 1), (p_115314_.getStretch() + 1), 1.0F / (p_115314_.getStretch() + 1));
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderEntity p_110775_1_) {
        return TEXTURE;
    }

    @Override
    public void render(DefenderEntity p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        switch (p_115455_.getCustomRender()) {
            case 0:
            default:
                super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
                break;
            case 1:
                renderSprite1(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_, 1.0F);
                break;
        }
    }

    public void renderSprite1(Entity p_114485_, float p_114486_, float p_114487_, PoseStack poseStack, MultiBufferSource p_114489_, int light, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.35D, 0.0D);
        VertexConsumer sprite = p_114489_.getBuffer(RenderType.entityTranslucent(SPINNY_P2));
        renderSpin((int) (p_114485_.tickCount) % 4, poseStack, sprite, light, alpha);
        poseStack.popPose();
    }

    private void renderSpin(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float alpha) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(frame, matrixStackIn, builder, packedLightIn, alpha);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(int frame, PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
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
