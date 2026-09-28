package com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.oryctolins.ToymaklinAnimation;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.CustomHeadedModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.bosses.Toymaklin;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ToymaklinModel<T extends Toymaklin> extends HierarchicalModel<T> implements CustomHeadedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "toymaklin"), "main");
    private final ModelPart root;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart part;
    private final ModelPart part2;
    private final ModelPart part3;
    private final ModelPart head;
    private final ModelPart ear1;
    private final ModelPart ear_bend1;
    private final ModelPart ear2;
    private final ModelPart ear_bend2;
    private final ModelPart jesterhat1;
    private final ModelPart jesterhatt1;
    private final ModelPart jesterhattt1;
    private final ModelPart jesterhatttt1;
    private final ModelPart jesterhat2;
    private final ModelPart jesterhatt2;
    private final ModelPart jesterhattt2;
    private final ModelPart jesterhatttt2;
    private final ModelPart jesterhat3_rot;
    private final ModelPart jesterhat3;
    private final ModelPart jesterhatt3;
    private final ModelPart jesterhattt3;
    private final ModelPart jesterhatttt3;
    private final ModelPart right_arm;
    private final ModelPart rarm_rotated;
    private final ModelPart right_elbow;
    private final ModelPart relbow_rotated;
    private final ModelPart right_hand;
    private final ModelPart rhand_rotated;
    private final ModelPart staff;
    private final ModelPart staff_center;
    private final ModelPart staff_head;
    private final ModelPart staff_jester1;
    private final ModelPart staff_jester2;
    private final ModelPart staff_jester3;
    private final ModelPart staff_jester4;
    private final ModelPart tail;
    private final ModelPart left_arm;
    private final ModelPart larm_rotated;
    private final ModelPart left_elbow;
    private final ModelPart lelbow_rotated;
    private final ModelPart left_hand;
    private final ModelPart lhand_rotated;
    private final ModelPart right_leg;
    private final ModelPart right_foot;
    private final ModelPart right_foot_curve;
    private final ModelPart left_leg;
    private final ModelPart left_foot;
    private final ModelPart left_foot_curve;

    public ToymaklinModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.part = this.body.getChild("part");
        this.part2 = this.body.getChild("part2");
        this.part3 = this.body.getChild("part3");
        this.head = this.body.getChild("head");
        this.ear1 = this.head.getChild("ear1");
        this.ear_bend1 = this.ear1.getChild("ear_bend1");
        this.ear2 = this.head.getChild("ear2");
        this.ear_bend2 = this.ear2.getChild("ear_bend2");
        this.jesterhat1 = this.head.getChild("jesterhat1");
        this.jesterhatt1 = this.jesterhat1.getChild("jesterhatt1");
        this.jesterhattt1 = this.jesterhatt1.getChild("jesterhattt1");
        this.jesterhatttt1 = this.jesterhattt1.getChild("jesterhatttt1");
        this.jesterhat2 = this.head.getChild("jesterhat2");
        this.jesterhatt2 = this.jesterhat2.getChild("jesterhatt2");
        this.jesterhattt2 = this.jesterhatt2.getChild("jesterhattt2");
        this.jesterhatttt2 = this.jesterhattt2.getChild("jesterhatttt2");
        this.jesterhat3_rot = this.head.getChild("jesterhat3_rot");
        this.jesterhat3 = this.jesterhat3_rot.getChild("jesterhat3");
        this.jesterhatt3 = this.jesterhat3.getChild("jesterhatt3");
        this.jesterhattt3 = this.jesterhatt3.getChild("jesterhattt3");
        this.jesterhatttt3 = this.jesterhattt3.getChild("jesterhatttt3");
        this.right_arm = this.body.getChild("right_arm");
        this.rarm_rotated = this.right_arm.getChild("rarm_rotated");
        this.right_elbow = this.rarm_rotated.getChild("right_elbow");
        this.relbow_rotated = this.right_elbow.getChild("relbow_rotated");
        this.right_hand = this.relbow_rotated.getChild("right_hand");
        this.rhand_rotated = this.right_hand.getChild("rhand_rotated");
        this.staff = this.rhand_rotated.getChild("staff");
        this.staff_center = this.staff.getChild("staff_center");
        this.staff_head = this.staff_center.getChild("staff_head");
        this.staff_jester1 = this.staff_head.getChild("staff_jester1");
        this.staff_jester2 = this.staff_jester1.getChild("staff_jester2");
        this.staff_jester3 = this.staff_head.getChild("staff_jester3");
        this.staff_jester4 = this.staff_jester3.getChild("staff_jester4");
        this.tail = this.body.getChild("tail");
        this.left_arm = this.body.getChild("left_arm");
        this.larm_rotated = this.left_arm.getChild("larm_rotated");
        this.left_elbow = this.larm_rotated.getChild("left_elbow");
        this.lelbow_rotated = this.left_elbow.getChild("lelbow_rotated");
        this.left_hand = this.lelbow_rotated.getChild("left_hand");
        this.lhand_rotated = this.left_hand.getChild("lhand_rotated");
        this.right_leg = this.all.getChild("right_leg");
        this.right_foot = this.right_leg.getChild("right_foot");
        this.right_foot_curve = this.right_foot.getChild("right_foot_curve");
        this.left_leg = this.all.getChild("left_leg");
        this.left_foot = this.left_leg.getChild("left_foot");
        this.left_foot_curve = this.left_foot.getChild("left_foot_curve");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(112, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(108, 14).addBox(-2.5F, 6.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(104, 21).addBox(-4.0F, -3.1F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition part = body.addOrReplaceChild("part", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-2.0F, -1.5F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -0.75F, -2.0F, 0.4363F, 0.3491F, 0.1745F));

        PartDefinition part2 = body.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(44, 0).addBox(-2.0F, -1.5F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(1.5F, -0.75F, -2.0F, 0.4363F, -0.3491F, -0.1745F));

        PartDefinition part3 = body.addOrReplaceChild("part3", CubeListBuilder.create().texOffs(68, 8).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -1.5F, -3.5F, -1.0472F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(3.0F, -7.0F, 0.0F, -0.4363F, 0.6109F, 0.0F));

        PartDefinition ear_bend1 = ear1.addOrReplaceChild("ear_bend1", CubeListBuilder.create().texOffs(12, 30).mirror().addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-3.0F, -7.0F, 0.0F, -0.4363F, -0.6109F, 0.0F));

        PartDefinition ear_bend2 = ear2.addOrReplaceChild("ear_bend2", CubeListBuilder.create().texOffs(12, 20).addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition jesterhat1 = head.addOrReplaceChild("jesterhat1", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -3.0F, -4.0F, 14.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.0F, 1.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition jesterhatt1 = jesterhat1.addOrReplaceChild("jesterhatt1", CubeListBuilder.create().texOffs(40, 40).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.0F, -1.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition jesterhattt1 = jesterhatt1.addOrReplaceChild("jesterhattt1", CubeListBuilder.create().texOffs(56, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition jesterhatttt1 = jesterhattt1.addOrReplaceChild("jesterhatttt1", CubeListBuilder.create().texOffs(68, 8).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition jesterhat2 = head.addOrReplaceChild("jesterhat2", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-13.0F, -3.0F, -4.0F, 14.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -6.0F, 1.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition jesterhatt2 = jesterhat2.addOrReplaceChild("jesterhatt2", CubeListBuilder.create().texOffs(40, 52).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-10.5F, 2.0F, -1.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition jesterhattt2 = jesterhatt2.addOrReplaceChild("jesterhattt2", CubeListBuilder.create().texOffs(56, 54).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition jesterhatttt2 = jesterhattt2.addOrReplaceChild("jesterhatttt2", CubeListBuilder.create().texOffs(68, 8).mirror().addBox(-1.0F, 0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition jesterhat3_rot = head.addOrReplaceChild("jesterhat3_rot", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -6.0F, 2.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition jesterhat3 = jesterhat3_rot.addOrReplaceChild("jesterhat3", CubeListBuilder.create().texOffs(0, 64).addBox(-1.0F, -3.0F, -4.0F, 14.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition jesterhatt3 = jesterhat3.addOrReplaceChild("jesterhatt3", CubeListBuilder.create().texOffs(40, 64).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.0F, -1.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition jesterhattt3 = jesterhatt3.addOrReplaceChild("jesterhattt3", CubeListBuilder.create().texOffs(56, 66).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition jesterhatttt3 = jesterhattt3.addOrReplaceChild("jesterhatttt3", CubeListBuilder.create().texOffs(68, 8).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-1.5F, -2.0F, 0.0F));

        PartDefinition rarm_rotated = right_arm.addOrReplaceChild("rarm_rotated", CubeListBuilder.create().texOffs(84, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition right_elbow = rarm_rotated.addOrReplaceChild("right_elbow", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition relbow_rotated = right_elbow.addOrReplaceChild("relbow_rotated", CubeListBuilder.create().texOffs(76, 19).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition right_hand = relbow_rotated.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition rhand_rotated = right_hand.addOrReplaceChild("rhand_rotated", CubeListBuilder.create().texOffs(76, 8).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition staff = rhand_rotated.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition staff_center = staff.addOrReplaceChild("staff_center", CubeListBuilder.create().texOffs(0, 111).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 122).addBox(-1.5F, 14.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition staff_head = staff_center.addOrReplaceChild("staff_head", CubeListBuilder.create().texOffs(4, 116).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(22, 114).addBox(-1.5F, -11.0F, 0.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(68, 8).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition cube_r1 = staff_head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 114).addBox(-1.0F, -8.0F, 0.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -0.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition staff_jester1 = staff_head.addOrReplaceChild("staff_jester1", CubeListBuilder.create().texOffs(40, 122).addBox(-1.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition staff_jester2 = staff_jester1.addOrReplaceChild("staff_jester2", CubeListBuilder.create().texOffs(30, 118).addBox(-0.75F, -1.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(68, 8).addBox(5.25F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.85F, 1.5F, 0.0F, 0.0F, 0.0F, 2.0071F));

        PartDefinition staff_jester3 = staff_head.addOrReplaceChild("staff_jester3", CubeListBuilder.create().texOffs(56, 122).addBox(-4.0F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition staff_jester4 = staff_jester3.addOrReplaceChild("staff_jester4", CubeListBuilder.create().texOffs(44, 118).addBox(-0.75F, -1.0F, 0.0F, 7.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(68, 8).addBox(5.45F, 1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.15F, 0.5F, 0.0F, 0.0F, 0.0F, 1.1345F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 20).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 2.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(1.5F, -2.0F, 0.0F));

        PartDefinition larm_rotated = left_arm.addOrReplaceChild("larm_rotated", CubeListBuilder.create().texOffs(84, 34).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition left_elbow = larm_rotated.addOrReplaceChild("left_elbow", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition lelbow_rotated = left_elbow.addOrReplaceChild("lelbow_rotated", CubeListBuilder.create().texOffs(76, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition left_hand = lelbow_rotated.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lhand_rotated = left_hand.addOrReplaceChild("lhand_rotated", CubeListBuilder.create().texOffs(76, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = all.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -14.0F, 0.0F));

        PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(92, 10).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(92, 0).mirror().addBox(-2.0F, 1.0F, -4.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition right_foot_curve = right_foot.addOrReplaceChild("right_foot_curve", CubeListBuilder.create().texOffs(92, 0).mirror().addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(-0.6F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition left_leg = all.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -14.0F, 0.0F));

        PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(92, 10).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(92, 16).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition left_foot_curve = left_foot.addOrReplaceChild("left_foot_curve", CubeListBuilder.create().texOffs(92, 16).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(-0.6F)), PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Toymaklin oryctolin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot += (headPitch * ((float)Math.PI / 180F));

        float moveX = (float) (oryctolin.getX() - oryctolin.xo);
        float moveZ = (float) (oryctolin.getZ() - oryctolin.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        this.right_leg.xRot += Mth.cos(limbSwing * 2.6648F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.8F;
        this.left_leg.xRot += Mth.cos(limbSwing * 2.6648F) * 1.4F * limbSwingAmount * 0.8F;

        this.animate(oryctolin.anim_idle, ToymaklinAnimation.idle, ageInTicks, oryctolin.getAnimationSpeed());
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
        return this.head;
    }

    @Override
    public void translateToHead(PoseStack stack) {
        this.root().translateAndRotate(stack);
        this.all.translateAndRotate(stack);
        this.body.translateAndRotate(stack);
        this.head.translateAndRotate(stack);
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
