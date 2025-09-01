package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ConverslinModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConverslinGlowLayer<T extends LivingEntity> extends EyesLayer<T, ConverslinModel<T>> {
    private static final RenderType LAYER = RenderType.eyes(new ResourceLocation(YellowbrossExtras.MOD_ID,"textures/entity/oryctolins/converslin/converslin_glow.png"));

    public ConverslinGlowLayer(RenderLayerParent<T, ConverslinModel<T>> p_i226039_1_) {
        super(p_i226039_1_);
    }

    @Override
    public RenderType renderType() {
        return LAYER;
    }
}
