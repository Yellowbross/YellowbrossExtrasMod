package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.AmoebicDevourerModel;
import com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AmoebicDevourerRenderer extends MobRenderer<AmoebicDevourerEntity, AmoebicDevourerModel<AmoebicDevourerEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/amoebic_devourer/amoebic_devourer.png");

    public AmoebicDevourerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new AmoebicDevourerModel<>(renderManagerIn.bakeLayer(AmoebicDevourerModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(AmoebicDevourerEntity p_110775_1_) {
        return TEXTURE;
    }
}
