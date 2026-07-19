package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.OkayMisterImSorryModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.OkayMisterImSorry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OkayMisterImSorryRenderer extends EntityRenderer<OkayMisterImSorry> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/eastereggs/villager_soul.png");
    private final OkayMisterImSorryModel<OkayMisterImSorry> model;

    public OkayMisterImSorryRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new OkayMisterImSorryModel<>(pContext.bakeLayer(OkayMisterImSorryModel.LAYER_LOCATION));
    }

    @Override
    public void render(OkayMisterImSorry pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.pushPose();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);

        pPoseStack.translate(0, -1.5, 0);
        pPoseStack.translate(0, Math.sin((pEntity.tickCount + pPartialTick) / 10) / 3, 0);

        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(pEntity.getYRot());
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(OkayMisterImSorry pEntity) {
        return TEXTURE;
    }
}
