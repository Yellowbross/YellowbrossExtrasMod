package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.DefenderAxeModel;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.DefenderAxeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefenderAxeRenderer extends MobRenderer<DefenderAxeEntity, DefenderAxeModel<DefenderAxeEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/axe.png");

    public DefenderAxeRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DefenderAxeModel<>(renderManagerIn.bakeLayer(DefenderAxeModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderAxeEntity p_110775_1_) {
        return TEXTURE;
    }
}
