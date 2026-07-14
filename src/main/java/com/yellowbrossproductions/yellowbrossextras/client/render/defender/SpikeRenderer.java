package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SpikeModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Spike;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpikeRenderer extends EntityRenderer<Spike> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/spike.png");
    private final SpikeModel<Spike> model;

    public SpikeRenderer(EntityRendererProvider.Context p_174100_) {
        super(p_174100_);
        this.model = new SpikeModel<>(p_174100_.bakeLayer(SpikeModel.LAYER_LOCATION));
    }

    @Override
    protected int getBlockLightLevel(Spike pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(Spike pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float f = pEntity.getAnimationProgress(pPartialTick);
        if (f != 0.0F) {
            float f1 = 1.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            pPoseStack.scale(1.0F, -f1, 1.0F);
            float f2 = 0.03125F;
            this.model.setupAnim(pEntity, f, 0.0F, 0.0F, pEntity.getYRot(), pEntity.getXRot());
            pPoseStack.translate(0.0D, -1.6D, 0.0D);
            VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Spike pEntity) {
        return TEXTURE;
    }
}
