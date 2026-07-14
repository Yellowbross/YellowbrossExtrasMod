package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.AmoebicDevourerModel;
import com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AmoebicDevourerRenderer extends MobRenderer<AmoebicDevourer, AmoebicDevourerModel<AmoebicDevourer>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/amoebic_devourer/amoebic_devourer.png");

    public AmoebicDevourerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AmoebicDevourerModel<>(renderManagerIn.bakeLayer(AmoebicDevourerModel.LAYER_LOCATION)), 0.7F);
    }

    public void render(AmoebicDevourer p_115976_, float p_115977_, float p_115978_, PoseStack p_115979_, MultiBufferSource p_115980_, int p_115981_) {
        this.shadowRadius = 0.25F * (float)p_115976_.getSize();
        super.render(p_115976_, p_115977_, p_115978_, p_115979_, p_115980_, p_115981_);
    }

    protected void scale(AmoebicDevourer p_115983_, PoseStack p_115984_, float p_115985_) {
        float smallMult = 0.5f;
        p_115984_.scale(0.999F, 0.999F, 0.999F);
        p_115984_.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)p_115983_.getSize();
        float f2 = Mth.lerp(p_115985_, p_115983_.oSquish, p_115983_.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        p_115984_.scale(f3 * f1 * smallMult, 1.0F / f3 * f1 * smallMult, f3 * f1 * smallMult);
    }

    @Override
    public ResourceLocation getTextureLocation(AmoebicDevourer p_110775_1_) {
        return TEXTURE;
    }
}
