package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SniperRifleModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SpikeModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SpikeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SniperRifleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SniperRifleRenderer extends EntityRenderer<SniperRifleEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/defender.png");
    private final SniperRifleModel<SniperRifleEntity> model;

    public SniperRifleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SniperRifleModel<>(context.bakeLayer(SniperRifleModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(SniperRifleEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(SniperRifleEntity rifle, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(rifle, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.pushPose();
        float maxTime = rifle.getMaxTime() - 1;

        double x = (rifle.getCollisionPos().getX() - rifle.getX()) * (rifle.tickCount + pPartialTick) / maxTime;
        double y = (rifle.getCollisionPos().getY() - rifle.getY()) * (rifle.tickCount + pPartialTick) / maxTime;
        double z = (rifle.getCollisionPos().getZ() - rifle.getZ()) * (rifle.tickCount + pPartialTick) / maxTime;
        pPoseStack.translate(x, y, z);
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(rifle.getYRot()));
        pPoseStack.mulPose(Vector3f.XP.rotationDegrees(rifle.getXRot()));

        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(rifle)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SniperRifleEntity pEntity) {
        return TEXTURE;
    }
}
