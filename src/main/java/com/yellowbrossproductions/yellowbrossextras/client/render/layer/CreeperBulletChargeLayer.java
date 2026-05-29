package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import com.yellowbrossproductions.yellowbrossextras.client.model.CreeperBulletModel;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBulletEntity;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperBulletChargeLayer extends EnergySwirlLayer<CreeperBulletEntity, CreeperBulletModel<CreeperBulletEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final CreeperBulletModel<CreeperBulletEntity> model;

    public CreeperBulletChargeLayer(RenderLayerParent<CreeperBulletEntity, CreeperBulletModel<CreeperBulletEntity>> p_116967_, EntityModelSet set) {
        super(p_116967_);
        this.model = new CreeperBulletModel<>(set.bakeLayer(CreeperBulletModel.LAYER_LOCATION));
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
    protected EntityModel<CreeperBulletEntity> model() {
        return this.model;
    }
}
