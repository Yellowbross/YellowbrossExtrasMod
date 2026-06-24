package com.yellowbrossproductions.yellowbrossextras.client.render.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.DefenderModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.SentryGunModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGunEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SentryGunRenderer extends MobRenderer<SentryGunEntity, SentryGunModel<SentryGunEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/sentry_gun.png");

    public SentryGunRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new SentryGunModel<>(renderManagerIn.bakeLayer(SentryGunModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    protected float getWhiteOverlayProgress(SentryGunEntity gun, float amount) {
        if (gun.getExplodeTimer() >= 3 * 20) {
            return 0.0f;
        }
        return gun.getExplodeTimer() % 10 >= 4 ? 0.0F : 1.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(SentryGunEntity pEntity) {
        return TEXTURE;
    }
}
