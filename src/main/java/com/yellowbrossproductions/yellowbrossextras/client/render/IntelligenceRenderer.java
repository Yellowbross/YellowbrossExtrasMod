package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.IntelligenceModel;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.Intelligence;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IntelligenceRenderer extends EntityRenderer<Intelligence> {
    private static final ResourceLocation RED = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/gamemode_fun/intelligence_red.png");
    private static final ResourceLocation BLU = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/gamemode_fun/intelligence_blu.png");
    private static final ResourceLocation UNKNOWN = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/gamemode_fun/intelligence_unknown.png");
    private final IntelligenceModel model;

    public IntelligenceRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_);
        this.model = new IntelligenceModel<>(p_174304_.bakeLayer(IntelligenceModel.LAYER_LOCATION));
    }

    public void render(Intelligence pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.scale(-1.0F, -1.0F, -1.0F);
        pPoseStack.translate(0.0D, -1.5D, 0.0D);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(pEntity, pEntity.tickCount);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    protected int getBlockLightLevel(Intelligence pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public boolean shouldRender(Intelligence pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return !pLivingEntity.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(Intelligence pEntity) {
        if (pEntity.getTeam() != null) {
            if (pEntity.getTeam().getColor() == ChatFormatting.RED) return RED;
            if (pEntity.getTeam().getColor() == ChatFormatting.BLUE) return BLU;
        }
        return UNKNOWN;
    }
}
