package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DefenderAxeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefenderAxeRenderer<T extends DefenderAxeEntity> extends EntityRenderer<T> {
    private static final ItemStack RENDER_ITEM = Items.IRON_AXE.getDefaultInstance();
    private final ItemRenderer itemRenderer;

    public DefenderAxeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.itemRenderer = renderManagerIn.getItemRenderer();
    }

    @Override
    public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int blockLight) {
        poseStack.pushPose();

        poseStack.mulPose(Vector3f.YP.rotationDegrees(-(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) + 90)));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(-(entity.tickCount + partialTick) * 25));

        this.itemRenderer.renderStatic(null, RENDER_ITEM, ItemTransforms.TransformType.FIXED, false, poseStack, bufferSource, entity.level, blockLight, OverlayTexture.NO_OVERLAY, entity.getId());
        super.render(entity, yaw, partialTick, poseStack, bufferSource, blockLight);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderAxeEntity pEntity) {
        return null;
    }
}
