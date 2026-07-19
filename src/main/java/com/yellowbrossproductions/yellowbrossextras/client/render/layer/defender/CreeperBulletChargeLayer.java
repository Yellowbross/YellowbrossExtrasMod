package com.yellowbrossproductions.yellowbrossextras.client.render.layer.defender;

import com.yellowbrossproductions.yellowbrossextras.client.model.defender.CreeperBulletModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperBulletChargeLayer extends EnergySwirlLayer<CreeperBullet, CreeperBulletModel<CreeperBullet>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final CreeperBulletModel<CreeperBullet> model;

    public CreeperBulletChargeLayer(RenderLayerParent<CreeperBullet, CreeperBulletModel<CreeperBullet>> p_116967_, EntityModelSet set) {
        super(p_116967_);
        this.model = new CreeperBulletModel<>(set.bakeLayer(CreeperBulletModel.LAYER_LOCATION), true);
    }

    @Override
    protected float xOffset(float p_116968_) {
        return p_116968_ * 0.01f;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<CreeperBullet> model() {
        return this.model;
    }
}
