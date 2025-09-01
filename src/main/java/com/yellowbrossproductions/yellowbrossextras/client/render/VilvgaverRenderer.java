package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.VilvgaverEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@OnlyIn(Dist.CLIENT)
public class VilvgaverRenderer extends EntityRenderer<Entity> {
    private static final float TEXTURE_WIDTH = 16;
    private static final float TEXTURE_HEIGHT = 16;
    private static final float START_RADIUS = -1.5f;

    public VilvgaverRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_114482_) {
        return null;
    }

    @Override
    public void render(Entity p_114485_, float p_114486_, float p_114487_, PoseStack poseStack, MultiBufferSource p_114489_, int light) {
        int delay = YellowbrossExtrasConfig.vilvgaverChallenge_delayTime.get() * 20;

        float f = p_114485_.tickCount / (float)delay;
        float f1 = Math.min(f, 1.0F);
        if (p_114485_ instanceof VilvgaverEntity vilvgaver) {
            if (!vilvgaver.isChallenge()) {
                f1 = 1.0F;
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 1.35D, 0.0D);

        // File minecraftDir = FMLPaths.GAMEDIR.get().toFile();
        // File folder = new File(minecraftDir.getAbsolutePath() + "/textures/entity/vilvgaver/");
        // int frameAmount = countFiles(folder.getPath()) + 1;
        // YellowbrossExtras.LOGGER.debug("Number of files in the folder: " + frameAmount);

        int frameAmount = YellowbrossExtrasConfig.vilvgaverTotalFrames.get();
        int frame = (p_114485_.tickCount % frameAmount) + 1;
        VertexConsumer sprite = p_114489_.getBuffer(RenderType.entityTranslucent(new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/vilvgaver/vilvgaver" + (frame) + ".png")));
        renderMonster(poseStack, sprite, light, f1);
        poseStack.popPose();
    }

    public int countFiles(String folderPath) {
        File folder = new File(folderPath);
        int count = 0;
        if (!folder.isDirectory()) {
            YellowbrossExtras.LOGGER.error(folderPath + " is not a directory, must look further into this.");
            return 0;
        } else {
            File[] files = folder.listFiles();
            if (files == null) {
                return 0;
            }
            for (File file : files) {
                if (file.isFile()) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    protected int getBlockLightLevel(Entity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    private void renderMonster(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning) {
        matrixStackIn.pushPose();
        Quaternion quat = this.entityRenderDispatcher.cameraOrientation();
        matrixStackIn.mulPose(quat);
        renderFlatQuad(matrixStackIn, builder, packedLightIn, warning);
        matrixStackIn.popPose();
    }

    private void renderFlatQuad(PoseStack matrixStackIn, VertexConsumer builder, int packedLightIn, float warning) {
        float minU = 0 + 16F / TEXTURE_WIDTH * 10;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, warning, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, warning, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1 * alpha, 1 * alpha, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
