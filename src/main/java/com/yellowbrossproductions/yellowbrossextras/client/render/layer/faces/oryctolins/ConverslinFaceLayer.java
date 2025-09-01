package com.yellowbrossproductions.yellowbrossextras.client.render.layer.faces.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.ConverslinModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConverslinFaceLayer<T extends LivingEntity> extends RenderLayer<T, ConverslinModel<T>> {
    private static final ResourceLocation FACE1 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/female1.png");
    private static final ResourceLocation FACE2 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/scare.png");
    private static final ResourceLocation FACE3 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/cry.png");
    private static final ResourceLocation FACE4 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/female2.png");
    private static final ResourceLocation FACE5 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/celebrate1.png");
    private static final ResourceLocation FACE6 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/blink1.png");
    private static final ResourceLocation OUCH1 = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/oryctolins/faces/near_death/female1.png");
    private final EntityModel<T> oryctomodel;

    public ConverslinFaceLayer(RenderLayerParent<T, ConverslinModel<T>> p_i226039_1_, EntityModelSet crap) {
        super(p_i226039_1_);
        this.oryctomodel = new ConverslinModel<>(crap.bakeLayer(ConverslinModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof AbstractOryctolin oryctolin) {
            if (!entitylivingbaseIn.isInvisible()) {
                this.getParentModel().copyPropertiesTo(this.oryctomodel);
                this.oryctomodel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.oryctomodel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(this.getFace(oryctolin.getFace(), oryctolin.getHealth() < (oryctolin.getMaxHealth() / 2))));
                this.oryctomodel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    private ResourceLocation getFace(int get, boolean nearDeath) {
        switch (get) {
            default:
                return FACE1;
            case 0:
                return nearDeath ? OUCH1 : FACE1;
            case 1:
                return FACE2;
            case 2:
                return FACE3;
            case 3:
                return nearDeath ? OUCH1 : FACE4;
            case 4:
                return FACE5;
            case 5:
                return nearDeath ? OUCH1 : FACE6;
        }
    }
}
