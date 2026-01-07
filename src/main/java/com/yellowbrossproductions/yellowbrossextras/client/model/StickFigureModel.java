package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.DefenderAnimation;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.StickFigureAnimation;
import com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourerEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class StickFigureModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "stick_figure"), "main");
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart spine_center;
    private final ModelPart spine1;
    private final ModelPart spine_end1;
    private final ModelPart head;
    private final ModelPart right_arm1;
    private final ModelPart right_arm2;
    private final ModelPart right_arm3;
    private final ModelPart right_arm_end2;
    private final ModelPart staff;
    private final ModelPart block;
    private final ModelPart right_arm_end1;
    private final ModelPart left_arm1;
    private final ModelPart left_arm2;
    private final ModelPart left_arm3;
    private final ModelPart left_arm_end2;
    private final ModelPart left_arm_end1;
    private final ModelPart spine2;
    private final ModelPart spine_end2;
    private final ModelPart right_leg1;
    private final ModelPart right_leg2;
    private final ModelPart right_leg3;
    private final ModelPart right_leg_end2;
    private final ModelPart right_leg_end1;
    private final ModelPart left_leg1;
    private final ModelPart left_leg2;
    private final ModelPart left_leg3;
    private final ModelPart left_leg_end2;
    private final ModelPart left_leg_end1;

    public StickFigureModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.spine_center = this.all.getChild("spine_center");
        this.spine1 = this.spine_center.getChild("spine1");
        this.spine_end1 = this.spine1.getChild("spine_end1");
        this.head = this.spine1.getChild("head");
        this.right_arm1 = this.spine1.getChild("right_arm1");
        this.right_arm2 = this.right_arm1.getChild("right_arm2");
        this.right_arm3 = this.right_arm2.getChild("right_arm3");
        this.right_arm_end2 = this.right_arm3.getChild("right_arm_end2");
        this.staff = this.right_arm_end2.getChild("staff");
        this.block = this.staff.getChild("block");
        this.right_arm_end1 = this.right_arm1.getChild("right_arm_end1");
        this.left_arm1 = this.spine1.getChild("left_arm1");
        this.left_arm2 = this.left_arm1.getChild("left_arm2");
        this.left_arm3 = this.left_arm2.getChild("left_arm3");
        this.left_arm_end2 = this.left_arm3.getChild("left_arm_end2");
        this.left_arm_end1 = this.left_arm1.getChild("left_arm_end1");
        this.spine2 = this.spine_center.getChild("spine2");
        this.spine_end2 = this.spine2.getChild("spine_end2");
        this.right_leg1 = this.spine2.getChild("right_leg1");
        this.right_leg2 = this.right_leg1.getChild("right_leg2");
        this.right_leg3 = this.right_leg2.getChild("right_leg3");
        this.right_leg_end2 = this.right_leg3.getChild("right_leg_end2");
        this.right_leg_end1 = this.right_leg1.getChild("right_leg_end1");
        this.left_leg1 = this.spine2.getChild("left_leg1");
        this.left_leg2 = this.left_leg1.getChild("left_leg2");
        this.left_leg3 = this.left_leg2.getChild("left_leg3");
        this.left_leg_end2 = this.left_leg3.getChild("left_leg_end2");
        this.left_leg_end1 = this.left_leg1.getChild("left_leg_end1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition spine_center = all.addOrReplaceChild("spine_center", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -29.0F, 0.0F));

        PartDefinition spine1 = spine_center.addOrReplaceChild("spine1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition spine_end1 = spine1.addOrReplaceChild("spine_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition head = spine1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition right_arm1 = spine1.addOrReplaceChild("right_arm1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition right_arm2 = right_arm1.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_arm3 = right_arm2.addOrReplaceChild("right_arm3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition right_arm_end2 = right_arm3.addOrReplaceChild("right_arm_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition staff = right_arm_end2.addOrReplaceChild("staff", CubeListBuilder.create().texOffs(112, 66).addBox(-2.0F, -30.0F, -2.0F, 4.0F, 58.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(82, 122).addBox(-2.0F, -32.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 98).addBox(5.0F, -46.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 90).addBox(-2.0F, -52.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(82, 116).addBox(-2.0F, -48.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition block = staff.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 96).addBox(-9.0F, -16.0F, -7.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-1.0F)), PartPose.offset(1.0F, -31.0F, -1.0F));

        PartDefinition right_arm_end1 = right_arm1.addOrReplaceChild("right_arm_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm1 = spine1.addOrReplaceChild("left_arm1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition left_arm2 = left_arm1.addOrReplaceChild("left_arm2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_arm3 = left_arm2.addOrReplaceChild("left_arm3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition left_arm_end2 = left_arm3.addOrReplaceChild("left_arm_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_arm_end1 = left_arm1.addOrReplaceChild("left_arm_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition spine2 = spine_center.addOrReplaceChild("spine2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition spine_end2 = spine2.addOrReplaceChild("spine_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition right_leg1 = spine2.addOrReplaceChild("right_leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition right_leg2 = right_leg1.addOrReplaceChild("right_leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition right_leg3 = right_leg2.addOrReplaceChild("right_leg3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition right_leg_end2 = right_leg3.addOrReplaceChild("right_leg_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition right_leg_end1 = right_leg1.addOrReplaceChild("right_leg_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg1 = spine2.addOrReplaceChild("left_leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition left_leg2 = left_leg1.addOrReplaceChild("left_leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition left_leg3 = left_leg2.addOrReplaceChild("left_leg3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition left_leg_end2 = left_leg3.addOrReplaceChild("left_leg_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition left_leg_end1 = left_leg1.addOrReplaceChild("left_leg_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof StickFigureEntity sticky) {
            this.animate(sticky.getAnimationState("base"), StickFigureAnimation.base, ageInTicks, sticky.getAnimationSpeed());
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
