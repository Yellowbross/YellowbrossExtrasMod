package com.yellowbrossproductions.yellowbrossextras.client.render.layer.sticky;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.client.model.StickFigureModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.YERenderTypes;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigure;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StickFigureColorLayer<T extends StickFigure, M extends StickFigureModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation TEXTURE;
    private final int color;
    private final float r;
    private final float g;
    private final float b;

    public StickFigureColorLayer(RenderLayerParent<T, M> p_117346_, ResourceLocation texture, int color) {
        super(p_117346_);
        this.TEXTURE = texture;
        this.color = color;
        this.r = (float) Vec3.fromRGB24(color).x;
        this.g = (float) Vec3.fromRGB24(color).y;
        this.b = (float) Vec3.fromRGB24(color).z;
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        VertexConsumer vertexconsumer = p_117350_.getBuffer(YERenderTypes.stickFigure(TEXTURE, false));
        this.getParentModel().renderToBuffer(p_117349_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, this.r, this.g, this.b, 1.0F);
    }
}
