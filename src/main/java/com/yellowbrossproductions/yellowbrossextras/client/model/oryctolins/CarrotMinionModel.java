package com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.CustomHeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Random;

public class CarrotMinionModel<T extends Entity> extends HierarchicalModel<T> implements CustomHeadedModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "carrot_minion"), "main");
    private final ModelPart root;
    private float partialTick;
    private final ModelPart all;
    private final Random random = new Random();

    public CarrotMinionModel(ModelPart root) {
        this.root = root;

        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -3.5F, -2.5F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -17.5F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition neck2 = neck.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition sprout = head.addOrReplaceChild("sprout", CubeListBuilder.create().texOffs(48, 44).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition leaves1 = sprout.addOrReplaceChild("leaves1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leaf1 = leaves1.addOrReplaceChild("leaf1", CubeListBuilder.create().texOffs(28, 52).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves2 = sprout.addOrReplaceChild("leaves2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition leaf2 = leaves2.addOrReplaceChild("leaf2", CubeListBuilder.create().texOffs(28, 52).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves3 = sprout.addOrReplaceChild("leaves3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition leaf3 = leaves3.addOrReplaceChild("leaf3", CubeListBuilder.create().texOffs(28, 52).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves4 = sprout.addOrReplaceChild("leaves4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leaf4 = leaves4.addOrReplaceChild("leaf4", CubeListBuilder.create().texOffs(28, 52).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition sprout2 = sprout.addOrReplaceChild("sprout2", CubeListBuilder.create().texOffs(48, 44).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.5F, -1.0F, -0.2618F, 0.3491F, 0.0873F));

        PartDefinition knee1 = leg1.addOrReplaceChild("knee1", CubeListBuilder.create().texOffs(0, 29).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.6981F, 0.0F, 0.0873F));

        PartDefinition foot1 = knee1.addOrReplaceChild("foot1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4363F, 0.0F, -0.0873F));

        PartDefinition footrot1 = foot1.addOrReplaceChild("footrot1", CubeListBuilder.create().texOffs(-2, 20).addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition foothalf1 = footrot1.addOrReplaceChild("foothalf1", CubeListBuilder.create().texOffs(-2, 26).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 2.5F, -1.0F, -0.2618F, -0.3491F, -0.0873F));

        PartDefinition knee2 = leg2.addOrReplaceChild("knee2", CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.6981F, 0.0F, -0.0873F));

        PartDefinition foot2 = knee2.addOrReplaceChild("foot2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.4363F, 0.0F, 0.0873F));

        PartDefinition footrot2 = foot2.addOrReplaceChild("footrot2", CubeListBuilder.create().texOffs(-2, 20).mirror().addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition foothalf2 = footrot2.addOrReplaceChild("foothalf2", CubeListBuilder.create().texOffs(-2, 26).mirror().addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 2.5F, 1.0F, 0.6981F, -0.3491F, 0.0873F));

        PartDefinition knee3 = leg3.addOrReplaceChild("knee3", CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.6981F, 3.1416F, -0.0873F));

        PartDefinition foot3 = knee3.addOrReplaceChild("foot3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.3491F, 0.0F, 0.0873F));

        PartDefinition footrot3 = foot3.addOrReplaceChild("footrot3", CubeListBuilder.create().texOffs(-2, 20).mirror().addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition foothalf3 = footrot3.addOrReplaceChild("foothalf3", CubeListBuilder.create().texOffs(-2, 26).mirror().addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.5F, 1.0F, 0.6981F, 0.3491F, -0.0873F));

        PartDefinition knee4 = leg4.addOrReplaceChild("knee4", CubeListBuilder.create().texOffs(0, 29).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.6981F, 3.1416F, 0.0873F));

        PartDefinition foot4 = knee4.addOrReplaceChild("foot4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, -0.3491F, 0.0F, -0.0873F));

        PartDefinition footrot4 = foot4.addOrReplaceChild("footrot4", CubeListBuilder.create().texOffs(-2, 20).addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition foothalf4 = footrot4.addOrReplaceChild("foothalf4", CubeListBuilder.create().texOffs(-2, 26).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.3491F, 0.0F, 0.0F));

        PartDefinition arm1 = body.addOrReplaceChild("arm1", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -2.5F, 0.0F, 0.0F, 0.3491F, 0.4363F));

        PartDefinition armrot1 = arm1.addOrReplaceChild("armrot1", CubeListBuilder.create().texOffs(20, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition elbow1 = armrot1.addOrReplaceChild("elbow1", CubeListBuilder.create().texOffs(20, 29).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition hand1 = elbow1.addOrReplaceChild("hand1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 2.0944F, 0.0F, 0.0F));

        PartDefinition handrot1 = hand1.addOrReplaceChild("handrot1", CubeListBuilder.create().texOffs(18, 20).addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition handhalf1 = handrot1.addOrReplaceChild("handhalf1", CubeListBuilder.create().texOffs(18, 26).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 2.0071F, 0.0F, 0.0F));

        PartDefinition arm2 = body.addOrReplaceChild("arm2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -2.5F, 0.0F, 0.0F, -0.3491F, -0.4363F));

        PartDefinition armrot2 = arm2.addOrReplaceChild("armrot2", CubeListBuilder.create().texOffs(20, 20).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition elbow2 = armrot2.addOrReplaceChild("elbow2", CubeListBuilder.create().texOffs(20, 29).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition hand2 = elbow2.addOrReplaceChild("hand2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 2.0944F, 0.0F, 0.0F));

        PartDefinition handrot2 = hand2.addOrReplaceChild("handrot2", CubeListBuilder.create().texOffs(18, 20).mirror().addBox(-4.0F, 0.0F, -3.5F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition handhalf2 = handrot2.addOrReplaceChild("handhalf2", CubeListBuilder.create().texOffs(18, 26).mirror().addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 2.0071F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        ModelPart body = this.all.getChild("body");
        ModelPart head = body.getChild("neck").getChild("head");

        head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        head.xRot += (headPitch * ((float)Math.PI / 180F));

        float twitchamount = 125;
    }

    public void animateIdle(int fullTick) {
        int actualTick = fullTick % 80;
        float tick = ((float)actualTick + this.partialTick);
        float f3 = tick / 65.0F;

    }

    @Override
    public void prepareMobModel(T entity, float p_102615_, float p_102616_, float p_102617_) {
        this.partialTick = p_102617_;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public ModelPart getHead() {
        return this.all.getChild("body").getChild("neck").getChild("head");
    }

    @Override
    public void translateToHead(PoseStack stack) {
        ModelPart body = this.all.getChild("body");
        ModelPart neck = body.getChild("neck");
        ModelPart head = neck.getChild("head");

        this.root().translateAndRotate(stack);
        this.all.translateAndRotate(stack);
        body.translateAndRotate(stack);
        neck.translateAndRotate(stack);
        head.translateAndRotate(stack);
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
