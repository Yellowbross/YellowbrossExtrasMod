package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.render.YERenderTypes;
import com.yellowbrossproductions.yellowbrossextras.client.render.util.RenderUtil;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.Fireball;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class FireballRenderer extends EntityRenderer<Fireball> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/fireball.png");
    private final Random random = new Random();

    private static final float TEXTURE_WIDTH = 128;
    private static final float TEXTURE_HEIGHT = 128;

    private static final float BALL_SIZE_HUGE = 64;
    private static final float BALL_SIZE_BIG = 32;
    private static final float BALL_SIZE_SMALL = 16;

    public FireballRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public boolean shouldRender(Fireball pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(Fireball pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        Vec3 vec3 = new Vec3(this.getRenderOffset(pEntity, pPartialTick).x, this.getRenderOffset(pEntity, pPartialTick).y, this.getRenderOffset(pEntity, pPartialTick).z);

        for (int i = 0; i < 2; ++i) {
            pPoseStack.pushPose();

            VertexConsumer sprite = i == 0 ? pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE)) : pBuffer.getBuffer(YERenderTypes.noShadingAllowed(TEXTURE, false));

            pPoseStack.translate(vec3.x, vec3.y, vec3.z);

            renderBallWithType(pEntity, pPartialTick, pPoseStack, pBuffer, sprite);

            pPoseStack.popPose();
        }
    }

    @Override
    public Vec3 getRenderOffset(Fireball pEntity, float pPartialTicks) {
        float ranMult = 0.2f;
        if (pEntity.getSize() == 3) return new Vec3((-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult);
        return super.getRenderOffset(pEntity, pPartialTicks);
    }

    @Override
    protected int getBlockLightLevel(Fireball pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Fireball pEntity) {
        return TEXTURE;
    }

    private void renderBallWithType(Fireball pEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, VertexConsumer sprite) {
        if (pEntity.getSize() == 3) renderHugeBall(pPoseStack, sprite, pEntity.tickCount, pPartialTick, pEntity.shouldFlash());
        if (pEntity.getSize() == 2) renderBigBall(pPoseStack, sprite, pEntity.tickCount, pPartialTick);
        if (pEntity.getSize() == 1) renderSmallBall(pPoseStack, sprite, pEntity.tickCount, pPartialTick);
    }

    private void renderHugeBall(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick, boolean shouldFlash) {
        poseStack.pushPose();
        poseStack.translate(0, 2.75, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float age = ticks + partialTick;

        int interval = 10;

        float size = Math.min(age, 3.0f);
        float mult = -5.0f;
        float calculation = age * mult;
        poseStack.scale(size, size, size);
        poseStack.mulPose(Axis.ZP.rotationDegrees(calculation));
        RenderUtil.drawSprite(poseStack, buffer, 0, 0, ((ticks % interval >= ((float)interval / 2)) && shouldFlash ? BALL_SIZE_HUGE : 0), BALL_SIZE_HUGE, BALL_SIZE_HUGE + ((ticks % interval >= ((float)interval / 2)) && shouldFlash ? BALL_SIZE_HUGE : 0), TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }

    private void renderBigBall(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float age = ticks + partialTick;

        float size = 1.0f;
        float mult = -15.0f;
        float calculation = age * mult;
        poseStack.scale(size, size, size);
        poseStack.mulPose(Axis.ZP.rotationDegrees(calculation));
        RenderUtil.drawSprite(poseStack, buffer, 0, BALL_SIZE_HUGE, 0, BALL_SIZE_HUGE + BALL_SIZE_BIG, BALL_SIZE_BIG, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }

    private void renderSmallBall(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float age = ticks + partialTick;

        float size = 0.5f;
        float mult = -40.0f;
        float calculation = age * mult;
        poseStack.scale(size, size, size);
        poseStack.mulPose(Axis.ZP.rotationDegrees(calculation));
        RenderUtil.drawSprite(poseStack, buffer, 0, BALL_SIZE_HUGE + BALL_SIZE_BIG, 0, BALL_SIZE_HUGE + BALL_SIZE_BIG + BALL_SIZE_SMALL, BALL_SIZE_SMALL, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }
}
