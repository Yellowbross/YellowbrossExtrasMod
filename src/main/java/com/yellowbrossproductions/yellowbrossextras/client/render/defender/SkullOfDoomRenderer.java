package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DeadlyArrowModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SkullOfDoomModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DeadlyArrow;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SkullOfDoom;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class SkullOfDoomRenderer extends EntityRenderer<SkullOfDoom> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/witherbazooka/skull_of_doom.png");
    private static final ResourceLocation GLOW = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/witherbazooka/glow.png");
    private final SkullOfDoomModel<SkullOfDoom> model;
    private final SkullOfDoomModel<SkullOfDoom> glow;
    private final Random random = new Random();

    public SkullOfDoomRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SkullOfDoomModel<>(pContext.bakeLayer(SkullOfDoomModel.LAYER_LOCATION));
        this.glow = new SkullOfDoomModel<>(pContext.bakeLayer(SkullOfDoomModel.LAYER_LOCATION));
    }

    @Override
    public void render(SkullOfDoom pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.pushPose();
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);

        pPoseStack.translate(this.getRenderOffset(pEntity, pPartialTick).x, this.getRenderOffset(pEntity, pPartialTick).y - 1.5d, this.getRenderOffset(pEntity, pPartialTick).z);

        int timer = pEntity.timer - pEntity.tickCount;
        int flashInterval = Math.max(2, timer / 10);

        float scaleUp = (int)(pEntity.tickCount / 10.0F) * 0.02F;
        float scale = timer % flashInterval >= (flashInterval / 2) ? scaleUp : -scaleUp;
        pPoseStack.scale(1.0F - scale, 1.0F + scale, 1.0F - scale);

        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(0.0f, pEntity.getYRot());
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, timer % flashInterval >= (flashInterval / 2) ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10) : OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        VertexConsumer vertexconsumer2 = pBuffer.getBuffer(RenderType.eyes(GLOW));
        this.glow.setupAnim(0.0f, pEntity.getYRot());
        this.glow.renderToBuffer(pPoseStack, vertexconsumer2, 15728640, timer % flashInterval >= (flashInterval / 2) ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10) : OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
    }

    @Override
    public Vec3 getRenderOffset(SkullOfDoom pEntity, float pPartialTicks) {
        float ranMult = 0.1f;
        return new Vec3((-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult, (-0.5 + random.nextDouble()) * ranMult);
    }

    @Override
    public ResourceLocation getTextureLocation(SkullOfDoom pEntity) {
        return TEXTURE;
    }
}
