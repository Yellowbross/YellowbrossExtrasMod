package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.PathGuideModel;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuide;
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
public class PathGuideRenderer extends EntityRenderer<PathGuide> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/gamemode_fun/path_guide/path_guide.png");
    private static final ResourceLocation WAITING = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/gamemode_fun/path_guide/path_guide_waiting.png");
    private final PathGuideModel model;

    public PathGuideRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_);
        this.model = new PathGuideModel<>(p_174304_.bakeLayer(PathGuideModel.LAYER_LOCATION));
    }

    public void render(PathGuide pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
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
    protected int getBlockLightLevel(PathGuide pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public boolean shouldRender(PathGuide pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return !pLivingEntity.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(PathGuide pEntity) {
        if (pEntity.isWaitingForSignal()) {
            return WAITING;
        }
        return TEXTURE;
    }
}
