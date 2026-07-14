package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DeadlyArrowModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DeadlyArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeadlyArrowRenderer extends EntityRenderer<DeadlyArrow> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/deadly_arrow.png");
    private final DeadlyArrowModel<DeadlyArrow> model;

    public DeadlyArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DeadlyArrowModel<>(context.bakeLayer(DeadlyArrowModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(DeadlyArrow pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return pLivingEntity.tickCount >= 20;
    }

    @Override
    public void render(DeadlyArrow arrow, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(arrow, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        if (arrow.tickCount >= 20) {
            pPoseStack.pushPose();
            float maxTime = arrow.getMaxTime() - 1;

            double x = (arrow.getCollisionPos().getX() - arrow.getX()) * ((arrow.tickCount - 20) + pPartialTick) / maxTime;
            double y = (arrow.getCollisionPos().getY() - arrow.getY()) * ((arrow.tickCount - 20) + pPartialTick) / maxTime;
            double z = (arrow.getCollisionPos().getZ() - arrow.getZ()) * ((arrow.tickCount - 20) + pPartialTick) / maxTime;
            pPoseStack.translate(x, y - 1, z);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(arrow.getYRot()));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(arrow.getXRot()));

            VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(arrow)));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            pPoseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(DeadlyArrow pEntity) {
        return TEXTURE;
    }
}
