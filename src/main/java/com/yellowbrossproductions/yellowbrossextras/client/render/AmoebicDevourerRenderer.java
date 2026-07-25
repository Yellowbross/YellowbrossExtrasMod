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

    public void render(AmoebicDevourer pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.shadowRadius = 0.25F * (float)pEntity.getSize();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    protected void scale(AmoebicDevourer pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        float smallMult = 0.5f;
        pPoseStack.scale(0.999F, 0.999F, 0.999F);
        pPoseStack.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)pLivingEntity.getSize();
        float f2 = Mth.lerp(pPartialTickTime, pLivingEntity.oSquish, pLivingEntity.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        pPoseStack.scale(f3 * f1 * smallMult, 1.0F / f3 * f1 * smallMult, f3 * f1 * smallMult);
    }

    @Override
    public ResourceLocation getTextureLocation(AmoebicDevourer pEntity) {
        return TEXTURE;
    }
}
