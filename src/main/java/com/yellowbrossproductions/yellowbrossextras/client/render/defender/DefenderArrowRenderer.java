package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DefenderArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefenderArrowRenderer extends ArrowRenderer<DefenderArrowEntity> {
    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public DefenderArrowRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderArrowEntity p_114482_) {
        return NORMAL_ARROW_LOCATION;
    }
}
