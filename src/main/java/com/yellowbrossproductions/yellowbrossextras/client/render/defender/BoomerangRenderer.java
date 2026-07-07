package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.BoomerangModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.BoomerangEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoomerangRenderer extends MobRenderer<BoomerangEntity, BoomerangModel<BoomerangEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/defender.png");

    public BoomerangRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BoomerangModel<>(renderManagerIn.bakeLayer(BoomerangModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(BoomerangEntity pEntity) {
        return TEXTURE;
    }
}
