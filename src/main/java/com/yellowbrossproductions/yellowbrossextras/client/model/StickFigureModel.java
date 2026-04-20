package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.StickFigureAnimation;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.CustomHeadedModel;
import com.yellowbrossproductions.yellowbrossextras.entities.StickFigureEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class StickFigureModel<T extends Entity> extends HierarchicalModel<T> implements CustomHeadedModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "stick_figure"), "main");
    private static final float TEXTURE_WIDTH = 16;
    private static final float TEXTURE_HEIGHT = 16;
    private static final float START_RADIUS = -0.25f;

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
    private final ModelPart staff_rotated;
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
        this.head = this.spine_end1.getChild("head");
        this.right_arm1 = this.spine1.getChild("right_arm1");
        this.right_arm2 = this.right_arm1.getChild("right_arm2");
        this.right_arm3 = this.right_arm2.getChild("right_arm3");
        this.right_arm_end2 = this.right_arm3.getChild("right_arm_end2");
        this.staff = this.right_arm_end2.getChild("staff");
        this.staff_rotated = this.staff.getChild("staff_rotated");
        this.block = this.staff_rotated.getChild("block");
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

        PartDefinition cube_r1 = spine_center.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition cube_r2 = spine_center.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r3 = spine_center.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r4 = spine_center.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r5 = spine_center.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r6 = spine_center.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r7 = spine_center.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r8 = spine_center.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition spine1 = spine_center.addOrReplaceChild("spine1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition cube_r9 = spine1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition cube_r10 = spine1.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r11 = spine1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r12 = spine1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r13 = spine1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r14 = spine1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r15 = spine1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r16 = spine1.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition spine_end1 = spine1.addOrReplaceChild("spine_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition head = spine_end1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition right_arm1 = spine1.addOrReplaceChild("right_arm1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r17 = right_arm1.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r18 = right_arm1.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r19 = right_arm1.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r20 = right_arm1.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r21 = right_arm1.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r22 = right_arm1.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r23 = right_arm1.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r24 = right_arm1.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition right_arm2 = right_arm1.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition cube_r25 = right_arm2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r26 = right_arm2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r27 = right_arm2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r28 = right_arm2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r29 = right_arm2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r30 = right_arm2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r31 = right_arm2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r32 = right_arm2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition right_arm3 = right_arm2.addOrReplaceChild("right_arm3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r33 = right_arm3.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r34 = right_arm3.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r35 = right_arm3.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r36 = right_arm3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r37 = right_arm3.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r38 = right_arm3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r39 = right_arm3.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r40 = right_arm3.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition right_arm_end2 = right_arm3.addOrReplaceChild("right_arm_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition staff = right_arm_end2.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition staff_rotated = staff.addOrReplaceChild("staff_rotated", CubeListBuilder.create().texOffs(112, 66).addBox(-2.0F, -30.0F, -2.0F, 4.0F, 58.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(82, 122).addBox(-2.0F, -32.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 98).addBox(5.0F, -46.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(96, 90).addBox(-2.0F, -52.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(82, 116).addBox(-2.0F, -48.0F, -2.0F, 11.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition block = staff_rotated.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 96).addBox(-9.0F, -16.0F, -7.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-1.0F)), PartPose.offset(1.0F, -31.0F, -1.0F));

        PartDefinition right_arm_end1 = right_arm1.addOrReplaceChild("right_arm_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm1 = spine1.addOrReplaceChild("left_arm1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -5.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r41 = left_arm1.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r42 = left_arm1.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r43 = left_arm1.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r44 = left_arm1.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r45 = left_arm1.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r46 = left_arm1.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r47 = left_arm1.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r48 = left_arm1.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_arm2 = left_arm1.addOrReplaceChild("left_arm2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition cube_r49 = left_arm2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r50 = left_arm2.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r51 = left_arm2.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r52 = left_arm2.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r53 = left_arm2.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r54 = left_arm2.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r55 = left_arm2.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r56 = left_arm2.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_arm3 = left_arm2.addOrReplaceChild("left_arm3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r57 = left_arm3.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r58 = left_arm3.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r59 = left_arm3.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r60 = left_arm3.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r61 = left_arm3.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r62 = left_arm3.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r63 = left_arm3.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r64 = left_arm3.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_arm_end2 = left_arm3.addOrReplaceChild("left_arm_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_arm_end1 = left_arm1.addOrReplaceChild("left_arm_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition spine2 = spine_center.addOrReplaceChild("spine2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r65 = spine2.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition cube_r66 = spine2.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r67 = spine2.addOrReplaceChild("cube_r67", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r68 = spine2.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r69 = spine2.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r70 = spine2.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r71 = spine2.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r72 = spine2.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition spine_end2 = spine2.addOrReplaceChild("spine_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition right_leg1 = spine2.addOrReplaceChild("right_leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition cube_r73 = right_leg1.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition cube_r74 = right_leg1.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r75 = right_leg1.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r76 = right_leg1.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r77 = right_leg1.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r78 = right_leg1.addOrReplaceChild("cube_r78", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r79 = right_leg1.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r80 = right_leg1.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition right_leg2 = right_leg1.addOrReplaceChild("right_leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition cube_r81 = right_leg2.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r82 = right_leg2.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r83 = right_leg2.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r84 = right_leg2.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r85 = right_leg2.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r86 = right_leg2.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r87 = right_leg2.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r88 = right_leg2.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition right_leg3 = right_leg2.addOrReplaceChild("right_leg3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r89 = right_leg3.addOrReplaceChild("cube_r89", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r90 = right_leg3.addOrReplaceChild("cube_r90", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r91 = right_leg3.addOrReplaceChild("cube_r91", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r92 = right_leg3.addOrReplaceChild("cube_r92", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r93 = right_leg3.addOrReplaceChild("cube_r93", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r94 = right_leg3.addOrReplaceChild("cube_r94", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r95 = right_leg3.addOrReplaceChild("cube_r95", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r96 = right_leg3.addOrReplaceChild("cube_r96", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition right_leg_end2 = right_leg3.addOrReplaceChild("right_leg_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition right_leg_end1 = right_leg1.addOrReplaceChild("right_leg_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg1 = spine2.addOrReplaceChild("left_leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition cube_r97 = left_leg1.addOrReplaceChild("cube_r97", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r98 = left_leg1.addOrReplaceChild("cube_r98", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r99 = left_leg1.addOrReplaceChild("cube_r99", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r100 = left_leg1.addOrReplaceChild("cube_r100", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r101 = left_leg1.addOrReplaceChild("cube_r101", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r102 = left_leg1.addOrReplaceChild("cube_r102", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r103 = left_leg1.addOrReplaceChild("cube_r103", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r104 = left_leg1.addOrReplaceChild("cube_r104", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_leg2 = left_leg1.addOrReplaceChild("left_leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition cube_r105 = left_leg2.addOrReplaceChild("cube_r105", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r106 = left_leg2.addOrReplaceChild("cube_r106", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r107 = left_leg2.addOrReplaceChild("cube_r107", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r108 = left_leg2.addOrReplaceChild("cube_r108", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r109 = left_leg2.addOrReplaceChild("cube_r109", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r110 = left_leg2.addOrReplaceChild("cube_r110", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r111 = left_leg2.addOrReplaceChild("cube_r111", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r112 = left_leg2.addOrReplaceChild("cube_r112", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_leg3 = left_leg2.addOrReplaceChild("left_leg3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r113 = left_leg3.addOrReplaceChild("cube_r113", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.2217F, 0.0F));

        PartDefinition cube_r114 = left_leg3.addOrReplaceChild("cube_r114", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        PartDefinition cube_r115 = left_leg3.addOrReplaceChild("cube_r115", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r116 = left_leg3.addOrReplaceChild("cube_r116", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

        PartDefinition cube_r117 = left_leg3.addOrReplaceChild("cube_r117", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r118 = left_leg3.addOrReplaceChild("cube_r118", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

        PartDefinition cube_r119 = left_leg3.addOrReplaceChild("cube_r119", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r120 = left_leg3.addOrReplaceChild("cube_r120", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

        PartDefinition left_leg_end2 = left_leg3.addOrReplaceChild("left_leg_end2", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition left_leg_end1 = left_leg1.addOrReplaceChild("left_leg_end1", CubeListBuilder.create().texOffs(2, 1).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.visible = false;

        this.head.x -= (netHeadYaw * ((float)Math.PI / 180F)) * 0.5F;
        this.spine1.zRot -= (netHeadYaw * ((float)Math.PI / 180F)) * 0.1F;
        this.spine_end1.yRot += (netHeadYaw * ((float)Math.PI / 180F));

        this.spine_end1.xRot += (headPitch * ((float)Math.PI / 180F)) * 0.5F;
        this.spine1.xRot += (headPitch * ((float)Math.PI / 180F)) * 0.5F;

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

    @Override
    public void translateToHead(PoseStack stack) {
        this.root().translateAndRotate(stack);
        this.all.translateAndRotate(stack);
        this.spine_center.translateAndRotate(stack);
        this.spine1.translateAndRotate(stack);
        this.spine_end1.translateAndRotate(stack);
    }
}
