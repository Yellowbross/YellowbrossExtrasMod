package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.render.YERenderTypes;
import com.yellowbrossproductions.yellowbrossextras.client.render.util.RenderUtil;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.Icicle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IcicleRenderer extends EntityRenderer<Icicle> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/icethrower.png");

    private static final float TEXTURE_WIDTH = 80;
    private static final float TEXTURE_HEIGHT = 110;

    private static final float WARNING_SIZE = 40;
    private static final float EXPLOSION_WIDTH = 50;
    private static final float EXPLOSION_HEIGHT = 70;
    private static final float ICICLE_WIDTH = 8;
    private static final float ICICLE_HEIGHT = 27;

    public IcicleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(Icicle pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        VertexConsumer sprite = pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        if (pEntity.tickCount <= 4) renderIcicle(pPoseStack, sprite, pEntity.tickCount, pPartialTick, pEntity.getTrueTimer(),  false);
        if (pEntity.tickCount > 6 + pEntity.getDelay() && pEntity.tickCount < pEntity.getTrueTimer()) renderWarning(pPoseStack, sprite, pEntity.tickCount);
        if (pEntity.tickCount >= pEntity.getTrueTimer() - 4 && pEntity.tickCount < pEntity.getTrueTimer())
            renderIcicle(pPoseStack, sprite, pEntity.tickCount, pPartialTick, pEntity.getTrueTimer(), true);
        if (pEntity.tickCount >= pEntity.getTrueTimer()) renderExplosion(pPoseStack, sprite, pEntity.tickCount, pPartialTick, pEntity.getTrueTimer());

        pPoseStack.popPose();

        pPoseStack.pushPose();

        VertexConsumer glow = pBuffer.getBuffer(YERenderTypes.stickFigure(TEXTURE, false));

        if (pEntity.tickCount > 6 + pEntity.getDelay() && pEntity.tickCount < pEntity.getTrueTimer()) renderWarning(pPoseStack, glow, pEntity.tickCount);

        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRender(Icicle pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    private void renderIcicle(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick, int timer, boolean fallingOrRising) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot() + 180.0f));
        float age = ticks + partialTick - (fallingOrRising ? (timer - 4) : 0) - 0.3f;

        float size = 0.15f;
        float height = size * 3.375f * 5.0f;
        poseStack.scale(size, height, size);
        double fly = ((100 * age) / 4);
        double y = fallingOrRising ? (100 - fly) : (0.75 + fly);

        poseStack.translate(0, y / 5, 0);
        RenderUtil.drawSprite(poseStack, buffer, 0,
                EXPLOSION_WIDTH + (fallingOrRising ? ICICLE_WIDTH : 0),
                WARNING_SIZE,
                EXPLOSION_WIDTH + ICICLE_WIDTH + (fallingOrRising ? ICICLE_WIDTH : 0),
                WARNING_SIZE + ICICLE_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }

    private void renderWarning(PoseStack poseStack, VertexConsumer buffer, float ticks) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        float size = 2.0f;
        poseStack.scale(size, size, size);
        poseStack.translate(0, 0, -0.01);
        RenderUtil.drawSprite(poseStack, buffer, 0,
                (ticks % 2 == 0 ? WARNING_SIZE : 0),
                0,
                WARNING_SIZE + (ticks % 2 == 0 ? WARNING_SIZE : 0),
                WARNING_SIZE,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }

    private void renderExplosion(PoseStack poseStack, VertexConsumer buffer, float ticks, float partialTick, int timer) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot() + 180.0f));
        float age = ticks + partialTick - timer;

        float size = 2.0f;
        float height = size * (EXPLOSION_HEIGHT / EXPLOSION_WIDTH);
        float wobbleSin = 1.0f + (age > 5 ? 0 : (float)(Math.sin(age * 4) / ((age + 1) * 10)));
        float wobbleCos = 1.0f + (age > 5 ? 0 : (float)(Math.cos(age * 4) / ((age + 1) * 10)));
        poseStack.scale(size * -wobbleCos, height * wobbleSin, size * wobbleCos);
        poseStack.translate(0, -0.1 + wobbleCos, 0);
        RenderUtil.drawSprite(poseStack, buffer, ticks > timer + 20 ? Math.min(1, (age - 20) / 20) : 0,
                0,
                WARNING_SIZE,
                EXPLOSION_WIDTH,
                WARNING_SIZE + EXPLOSION_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);
        poseStack.popPose();
    }

    public ResourceLocation getTextureLocation(Icicle pEntity) {
        return TEXTURE;
    }
}
