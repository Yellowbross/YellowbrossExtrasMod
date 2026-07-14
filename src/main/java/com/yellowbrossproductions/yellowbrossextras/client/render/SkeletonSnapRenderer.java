package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.yellowbrossproductions.yellowbrossextras.client.model.SkeletonSnapModel;
import com.yellowbrossproductions.yellowbrossextras.entities.SkeletonSnap;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkeletonSnapRenderer extends MobRenderer<SkeletonSnap, SkeletonSnapModel<SkeletonSnap>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SkeletonSnapRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SkeletonSnapModel<>(renderManagerIn.bakeLayer(SkeletonSnapModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonSnap p_110775_1_) {
        return TEXTURE;
    }
}
