package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SpikeModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SpikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpikeRenderer extends EntityRenderer<SpikeEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/spike.png");
    private final SpikeModel<SpikeEntity> model;

    public SpikeRenderer(EntityRendererProvider.Context p_174100_) {
        super(p_174100_);
        this.model = new SpikeModel<>(p_174100_.bakeLayer(SpikeModel.LAYER_LOCATION));
    }

    @Override
    protected int getBlockLightLevel(SpikeEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    public void render(SpikeEntity p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
        float f = p_114528_.getAnimationProgress(p_114530_);
        if (f != 0.0F) {
            float f1 = 1.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            p_114531_.pushPose();
            p_114531_.mulPose(Vector3f.YP.rotationDegrees(90.0F - p_114528_.getYRot()));
            p_114531_.scale(1.0F, -f1, 1.0F);
            float f2 = 0.03125F;
            this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
            p_114531_.translate(0.0D, -1.6D, 0.0D);
            VertexConsumer vertexconsumer = p_114532_.getBuffer(this.model.renderType(TEXTURE));
            this.model.renderToBuffer(p_114531_, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_114531_.popPose();
            super.render(p_114528_, p_114529_, p_114530_, p_114531_, p_114532_, p_114533_);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(SpikeEntity p_114482_) {
        return TEXTURE;
    }
}
