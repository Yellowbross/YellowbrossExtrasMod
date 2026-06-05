package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.AimbotModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.SkeletonSnapModel;
import com.yellowbrossproductions.yellowbrossextras.entities.AimbotEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.SkeletonSnapEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AimbotRenderer extends MobRenderer<AimbotEntity, AimbotModel<AimbotEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/aimbot.png");

    public AimbotRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AimbotModel<>(renderManagerIn.bakeLayer(AimbotModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(AimbotEntity p_110775_1_) {
        return TEXTURE;
    }
}
