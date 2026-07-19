package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DefenderModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.WatchYoToneBuddyBoyModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.defender.DefenderGlowLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.util.RenderUtil;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class DefenderRenderer extends MobRenderer<Defender, DefenderModel<Defender>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/defender.png");
    private final Random random = new Random();

    private static final ResourceLocation EASTER_EGG_1 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/eastereggs/watchyotonebuddyboy.png");
    private final WatchYoToneBuddyBoyModel<Defender> easterEgg1;

    private static final ResourceLocation SPRITESHEET = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/2d_effects.png");

    private static final float SHEET_WIDTH = 256;
    private static final float SHEET_HEIGHT = 217;

    private static final float SPINNY_SIZE = 64;
    private static final float TORNADO_WIDTH = 58;
    private static final float TORNADO_HEIGHT = 89;

    public DefenderRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DefenderModel<>(pContext.bakeLayer(DefenderModel.LAYER_LOCATION)), 0.6F);
        this.addLayer(new DefenderGlowLayer<>(this));
        this.easterEgg1 = new WatchYoToneBuddyBoyModel<>(pContext.bakeLayer(WatchYoToneBuddyBoyModel.LAYER_LOCATION));
    }

    @Override
    public Vec3 getRenderOffset(Defender pEntity, float pPartialTicks) {
        return new Vec3(
                (this.random.nextGaussian() * 0.001D) * pEntity.getShakeMultiplier(),
                0.0D,
                (this.random.nextGaussian() * 0.001D) * pEntity.getShakeMultiplier()
        );
    }

    @Override
    protected void scale(Defender defender, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.0F / (defender.getStretch() + 1),
                (defender.getStretch() + 1),
                1.0F / (defender.getStretch() + 1));
    }

    @Override
    public ResourceLocation getTextureLocation(Defender pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(Defender defender, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        switch (defender.getCustomRender()) {
            case 1:
                renderSpinny(defender, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
                break;
            case 2:
                renderTornado(defender, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
                break;
            case 3: {
                renderMonster(defender, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
                break;
            }

            default: super.render(defender, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
    }

    public void renderSpinny(Defender defender, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.35D, 0.0D);

        float f = defender.getWobbling(partialTick);

        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;

        float f1 = 1.0F + Mth.sin(f * 10.0F) * f * 0.5F;

        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.4F) / f1;

        poseStack.scale(f2, f3, f2);

        VertexConsumer sprite = multiBufferSource.getBuffer(RenderType.entityTranslucent(SPRITESHEET));
        float ageInTicks = defender.tickCount + partialTick;
        int loop = (int) (ageInTicks) % 4;
        boolean startOver = ageInTicks > 4;

        poseStack.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(quat);

        RenderUtil.drawSprite(poseStack, sprite, 0,
                loop * SPINNY_SIZE,
                startOver ? SPINNY_SIZE : 0,
                SPINNY_SIZE * (loop + 1),
                SPINNY_SIZE + (startOver ? SPINNY_SIZE : 0),
                SHEET_WIDTH, SHEET_HEIGHT);

        poseStack.popPose();

        poseStack.popPose();
    }

    public void renderTornado(Defender defender, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        poseStack.pushPose();
        float f = defender.getWobbling(partialTick);

        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;

        float f1 = 1.0F + Mth.sin(f * 10.0F) * f * 0.5F;

        float f2 = (0.85F + f * 0.4F) * f1;
        float f3 = (1.35F + f * 0.4F) / f1;

        poseStack.scale(f2, f3, f2);
        poseStack.translate(0.0D, 1.0D + (f3 * 0.05f), 0.0D);

        int loop = (int)((defender.tickCount + partialTick) % 4);
        VertexConsumer sprite = multiBufferSource.getBuffer(RenderType.entityTranslucent(SPRITESHEET));

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-this.entityRenderDispatcher.camera.getYRot() + 180.0f));

        RenderUtil.drawSprite(poseStack, sprite, 0,
                loop * TORNADO_WIDTH,
                SPINNY_SIZE * 2,
                TORNADO_WIDTH * (loop + 1),
                SPINNY_SIZE * 2 + TORNADO_HEIGHT,
                SHEET_WIDTH, SHEET_HEIGHT);

        poseStack.popPose();

        poseStack.popPose();
    }

    public void renderMonster(Defender defender, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        pPoseStack.translate((-0.5 + random.nextDouble()), -1.5, (-0.5 + random.nextDouble()));

        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.easterEgg1.renderType(EASTER_EGG_1));

        float f = Mth.rotLerp(pPartialTicks, defender.yBodyRotO, defender.yBodyRot);
        float f1 = Mth.rotLerp(pPartialTicks, defender.yHeadRotO, defender.yHeadRot);
        float f2 = f1 - f;
        float f3 = Mth.wrapDegrees(f2);
        if (f3 < -85.0F) {
            f3 = -85.0F;
        }

        if (f3 >= 85.0F) {
            f3 = 85.0F;
        }

        f = f1 - f3;
        if (f3 * f3 > 2500.0F) {
            f += f3 * 0.2F;
        }

        f2 = f1 - f;
        float f6 = Mth.lerp(pPartialTicks, defender.xRotO, defender.getXRot());
        float f8 = Mth.lerp(pPartialTicks, defender.animationSpeedOld, defender.animationSpeed);
        float f5 = defender.animationPosition - defender.animationSpeed * (1.0F - pPartialTicks);
        float f7 = this.getBob(defender, pPartialTicks);

        this.setupRotations(defender, pPoseStack, f7, -f, pPartialTicks);
        this.easterEgg1.prepareMobModel(defender, f5, f8, pPartialTicks);
        this.easterEgg1.setupAnim(defender, f5, f8, f7, f2, f6);
        this.easterEgg1.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
    }
}
