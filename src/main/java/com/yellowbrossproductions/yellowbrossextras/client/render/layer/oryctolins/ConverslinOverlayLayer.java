package com.yellowbrossproductions.yellowbrossextras.client.render.layer.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ConverslinModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.Converslin;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class ConverslinOverlayLayer<T extends Converslin, M extends ConverslinModel<T>> extends RenderLayer<T, M> {
    private static final Map<RenderType, Predicate<Converslin>> FACES = Util.make(new HashMap<>(), map -> {
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/female1.png")),
                e -> e.getFace() == 0 && e.getHealth() >= (e.getMaxHealth() / 2));
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/scare.png")),
                e -> e.getFace() == 1);
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/cry.png")),
                e -> e.getFace() == 2);
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/female2.png")),
                e -> e.getFace() == 3 && e.getHealth() >= (e.getMaxHealth() / 2));
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/celebrate1.png")),
                e -> e.getFace() == 4);
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/blink1.png")),
                e -> e.getFace() == 5 && e.getHealth() >= (e.getMaxHealth() / 2));
        map.put(RenderType.entityCutoutNoCull(YellowbrossExtras.prefix("textures/entity/oryctolins/faces/near_death/female1.png")),
                e -> (e.getFace() == 0 || e.getFace() == 3 || e.getFace() == 5) && e.getHealth() < (e.getMaxHealth() / 2));
    });
    private static final RenderType GLOW = RenderType.eyes(new ResourceLocation(YellowbrossExtras.MOD_ID,"textures/entity/oryctolins/converslin/converslin_glow.png"));

    public ConverslinOverlayLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isInvisible()) return;

        for (Map.Entry<RenderType, Predicate<Converslin>> entry : FACES.entrySet()) {
            if (entry.getValue().test(pLivingEntity))
                this.getParentModel().renderToBuffer(pPoseStack, pBuffer.getBuffer(entry.getKey()), pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0), 1, 1, 1, 1);
        }

        this.getParentModel().renderToBuffer(pPoseStack, pBuffer.getBuffer(GLOW), 15728640, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
