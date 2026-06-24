package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DefenderArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefenderArrowRenderer extends ArrowRenderer<DefenderArrowEntity> {
    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation POISON_DART_LOCATION = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/poison_dart.png");

    public DefenderArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(DefenderArrowEntity arrow) {
        switch (arrow.getArrowType()) {
            case 1 : return POISON_DART_LOCATION;
            default : return NORMAL_ARROW_LOCATION;
        }
    }
}
