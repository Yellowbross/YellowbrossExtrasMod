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

    public void render(PathGuide p_116484_, float p_116485_, float p_116486_, PoseStack p_116487_, MultiBufferSource p_116488_, int p_116489_) {
        p_116487_.pushPose();
        p_116487_.scale(-1.0F, -1.0F, -1.0F);
        p_116487_.translate(0.0D, -1.5D, 0.0D);
        VertexConsumer vertexconsumer = p_116488_.getBuffer(this.model.renderType(this.getTextureLocation(p_116484_)));
        this.model.setupAnim(p_116484_, p_116484_.tickCount);
        this.model.renderToBuffer(p_116487_, vertexconsumer, p_116489_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_116487_.popPose();
        super.render(p_116484_, p_116485_, p_116486_, p_116487_, p_116488_, p_116489_);
    }

    @Override
    protected int getBlockLightLevel(PathGuide p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public boolean shouldRender(PathGuide p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return !p_114491_.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(PathGuide p_114482_) {
        if (p_114482_.isWaitingForSignal()) {
            return WAITING;
        }
        return TEXTURE;
    }
}
