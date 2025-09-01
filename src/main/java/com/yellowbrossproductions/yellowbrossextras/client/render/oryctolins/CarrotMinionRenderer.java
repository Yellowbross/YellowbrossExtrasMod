package com.yellowbrossproductions.yellowbrossextras.client.render.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.CarrotMinionModel;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ConverslinModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.ConverslinGlowLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.faces.oryctolins.ConverslinFaceLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.ConverslinEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions.CarrotMinionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CarrotMinionRenderer extends MobRenderer<CarrotMinionEntity, CarrotMinionModel<CarrotMinionEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/carrot_minion/carrot_minion.png");
    private static final ResourceLocation NEAR_DEATH = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/carrot_minion/carrot_minion.png");
    private final Random random = new Random();

    public CarrotMinionRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CarrotMinionModel<>(renderManagerIn.bakeLayer(CarrotMinionModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new HeadItemLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    protected void scale(CarrotMinionEntity p_115314_, PoseStack p_115315_, float p_115316_) {
        float f1 = 0.8F;
        p_115315_.scale(f1, f1, f1);
    }

    @Override
    public ResourceLocation getTextureLocation(CarrotMinionEntity p_110775_1_) {
        if (p_110775_1_.getHealth() < (p_110775_1_.getMaxHealth() / 2)) {
            return NEAR_DEATH;
        }
        return TEXTURE;
    }
}
