package com.yellowbrossproductions.yellowbrossextras.client.render.layer.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ToymaklinModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.bosses.Toymaklin;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class ToymaklinOverlayLayer<T extends Toymaklin, M extends ToymaklinModel<T>> extends RenderLayer<T, M> {
    private static final Map<RenderType, Predicate<Toymaklin>> FACES = Util.make(new HashMap<>(), map -> {
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/toymaklin/idle.png")),
                e -> e.getFace() == 0 && e.getHealth() >= (e.getMaxHealth() / 2));
    });

    public ToymaklinOverlayLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isInvisible()) return;

        for (Map.Entry<RenderType, Predicate<Toymaklin>> entry : FACES.entrySet()) {
            if (entry.getValue().test(pLivingEntity))
                this.getParentModel().renderToBuffer(pPoseStack, pBuffer.getBuffer(entry.getKey()), pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0), 1, 1, 1, 1);
        }
    }
}
