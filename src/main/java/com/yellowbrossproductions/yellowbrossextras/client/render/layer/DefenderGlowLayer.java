package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DefenderModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ConverslinModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefenderGlowLayer<T extends LivingEntity> extends EyesLayer<T, DefenderModel<T>> {
    private static final RenderType LAYER = RenderType.eyes(new ResourceLocation(YellowbrossExtras.MOD_ID,"textures/entity/defender/defender_glow.png"));

    public DefenderGlowLayer(RenderLayerParent<T, DefenderModel<T>> p_i226039_1_) {
        super(p_i226039_1_);
    }

    @Override
    public RenderType renderType() {
        return LAYER;
    }
}
