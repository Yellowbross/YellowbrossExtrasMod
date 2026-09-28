package com.yellowbrossproductions.yellowbrossextras.client.render.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ToymaklinModel;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.HeadItemLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.oryctolins.ToymaklinOverlayLayer;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.bosses.Toymaklin;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ToymaklinRenderer extends MobRenderer<Toymaklin, ToymaklinModel<Toymaklin>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/toymaklin/toymaklin.png");
    private final Random random = new Random();

    public ToymaklinRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ToymaklinModel<>(renderManagerIn.bakeLayer(ToymaklinModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ToymaklinOverlayLayer<>(this));
        this.addLayer(new HeadItemLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public Vec3 getRenderOffset(Toymaklin pEntity, float pPartialTicks) {
        return new Vec3(
                (this.random.nextGaussian() * 0.001D) * pEntity.getShakeMultiplier(),
                0.0D,
                (this.random.nextGaussian() * 0.001D) * pEntity.getShakeMultiplier()
        );
    }

    @Override
    protected void scale(Toymaklin pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        float f1 = 0.8F;
        pMatrixStack.scale(f1, f1, f1);
    }

    @Override
    public ResourceLocation getTextureLocation(Toymaklin pEntity) {
        return TEXTURE;
    }
}
