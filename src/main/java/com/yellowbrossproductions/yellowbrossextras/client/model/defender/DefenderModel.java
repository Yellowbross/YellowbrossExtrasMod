package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.defender.Phase1Animation;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.defender.Phase2Animation;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class DefenderModel<T extends Defender> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender"), "main");
    private final ModelPart root;
    private float partialTick;
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart hat1;
    private final ModelPart hat2;
    private final ModelPart hat2_flap1;
    private final ModelPart hat2_flap2;
    private final ModelPart hat3;
    private final ModelPart hat3_propeller;
    private final ModelPart hat4;
    private final ModelPart hat5;
    private final ModelPart hat6;
    private final ModelPart hat7;
    private final ModelPart hat8;
    private final ModelPart hat9;
    private final ModelPart hat10;
    private final ModelPart spike;
    private final ModelPart spike2;
    private final ModelPart right_arm;
    private final ModelPart right_elbow;
    private final ModelPart right_hand;
    private final ModelPart sword;
    private final ModelPart blade_angle;
    private final ModelPart boomerang;
    private final ModelPart shuriken_launcher;
    private final ModelPart claw1;
    private final ModelPart excalibur;
    private final ModelPart blade_angle3;
    private final ModelPart excalihandle3;
    private final ModelPart excalihandle4;
    private final ModelPart excalibur_speed;
    private final ModelPart ratatatabow;
    private final ModelPart wood1;
    private final ModelPart wood2;
    private final ModelPart wood3;
    private final ModelPart rope1;
    private final ModelPart rope_rotated1;
    private final ModelPart wood4;
    private final ModelPart wood5;
    private final ModelPart rope2;
    private final ModelPart rope_rotated2;
    private final ModelPart poisondarts;
    private final ModelPart forcegun;
    private final ModelPart handle;
    private final ModelPart sniper_rifle;
    private final ModelPart sniper_rifle_rotated;
    private final ModelPart creeper_gun;
    private final ModelPart creephead;
    private final ModelPart creepbody1;
    private final ModelPart creepbody2;
    private final ModelPart creepbody3;
    private final ModelPart creepbody4;
    private final ModelPart creepbody5;
    private final ModelPart creepleg;
    private final ModelPart wither_bazooka;
    private final ModelPart wbazooka_head;
    private final ModelPart left_arm;
    private final ModelPart left_elbow;
    private final ModelPart left_hand;
    private final ModelPart claw2;
    private final ModelPart buzzsaw;
    private final ModelPart quiver;
    private final ModelPart right_leg;
    private final ModelPart right_foot;
    private final ModelPart left_leg;
    private final ModelPart left_foot;
    private final ModelPart rock;
    private final ModelPart excalibur_fake;
    private final ModelPart blade_angle2;
    private final ModelPart excalihandle1;
    private final ModelPart excalihandle2;
    private final ModelPart chainsaw_handle;
    private final ModelPart pullstring;
    private final ModelPart pull;
    private final ModelPart string;

    public DefenderModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.head = this.body.getChild("head");
        this.hat1 = this.head.getChild("hat1");
        this.hat2 = this.head.getChild("hat2");
        this.hat2_flap1 = this.hat2.getChild("hat2_flap1");
        this.hat2_flap2 = this.hat2.getChild("hat2_flap2");
        this.hat3 = this.head.getChild("hat3");
        this.hat3_propeller = this.hat3.getChild("hat3_propeller");
        this.hat4 = this.head.getChild("hat4");
        this.hat5 = this.head.getChild("hat5");
        this.hat6 = this.head.getChild("hat6");
        this.hat7 = this.head.getChild("hat7");
        this.hat8 = this.head.getChild("hat8");
        this.hat9 = this.head.getChild("hat9");
        this.hat10 = this.head.getChild("hat10");
        this.spike = this.head.getChild("spike");
        this.spike2 = this.spike.getChild("spike2");
        this.right_arm = this.body.getChild("right_arm");
        this.right_elbow = this.right_arm.getChild("right_elbow");
        this.right_hand = this.right_elbow.getChild("right_hand");
        this.sword = this.right_hand.getChild("sword");
        this.blade_angle = this.sword.getChild("blade_angle");
        this.boomerang = this.right_hand.getChild("boomerang");
        this.shuriken_launcher = this.right_hand.getChild("shuriken_launcher");
        this.claw1 = this.right_hand.getChild("claw1");
        this.excalibur = this.right_hand.getChild("excalibur");
        this.blade_angle3 = this.excalibur.getChild("blade_angle3");
        this.excalihandle3 = this.excalibur.getChild("excalihandle3");
        this.excalihandle4 = this.excalibur.getChild("excalihandle4");
        this.excalibur_speed = this.excalibur.getChild("excalibur_speed");
        this.ratatatabow = this.right_hand.getChild("ratatatabow");
        this.wood1 = this.ratatatabow.getChild("wood1");
        this.wood2 = this.wood1.getChild("wood2");
        this.wood3 = this.wood2.getChild("wood3");
        this.rope1 = this.wood3.getChild("rope1");
        this.rope_rotated1 = this.rope1.getChild("rope_rotated1");
        this.wood4 = this.wood1.getChild("wood4");
        this.wood5 = this.wood4.getChild("wood5");
        this.rope2 = this.wood5.getChild("rope2");
        this.rope_rotated2 = this.rope2.getChild("rope_rotated2");
        this.poisondarts = this.right_hand.getChild("poisondarts");
        this.forcegun = this.right_hand.getChild("forcegun");
        this.handle = this.forcegun.getChild("handle");
        this.sniper_rifle = this.right_hand.getChild("sniper_rifle");
        this.sniper_rifle_rotated = this.sniper_rifle.getChild("sniper_rifle_rotated");
        this.creeper_gun = this.right_hand.getChild("creeper_gun");
        this.creephead = this.creeper_gun.getChild("creephead");
        this.creepbody1 = this.creeper_gun.getChild("creepbody1");
        this.creepbody2 = this.creeper_gun.getChild("creepbody2");
        this.creepbody3 = this.creeper_gun.getChild("creepbody3");
        this.creepbody4 = this.creeper_gun.getChild("creepbody4");
        this.creepbody5 = this.creeper_gun.getChild("creepbody5");
        this.creepleg = this.creeper_gun.getChild("creepleg");
        this.wither_bazooka = this.right_hand.getChild("wither_bazooka");
        this.wbazooka_head = this.wither_bazooka.getChild("wbazooka_head");
        this.left_arm = this.body.getChild("left_arm");
        this.left_elbow = this.left_arm.getChild("left_elbow");
        this.left_hand = this.left_elbow.getChild("left_hand");
        this.claw2 = this.left_hand.getChild("claw2");
        this.buzzsaw = this.body.getChild("buzzsaw");
        this.quiver = this.body.getChild("quiver");
        this.right_leg = this.all.getChild("right_leg");
        this.right_foot = this.right_leg.getChild("right_foot");
        this.left_leg = this.all.getChild("left_leg");
        this.left_foot = this.left_leg.getChild("left_foot");
        this.rock = root.getChild("rock");
        this.excalibur_fake = root.getChild("excalibur_fake");
        this.blade_angle2 = this.excalibur_fake.getChild("blade_angle2");
        this.excalihandle1 = this.excalibur_fake.getChild("excalihandle1");
        this.excalihandle2 = this.excalibur_fake.getChild("excalihandle2");
        this.chainsaw_handle = root.getChild("chainsaw_handle");
        this.pullstring = this.chainsaw_handle.getChild("pullstring");
        this.pull = this.pullstring.getChild("pull");
        this.string = this.pullstring.getChild("string");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 0).addBox(-4.5F, -6.0F, -3.0F, 9.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition hat1 = head.addOrReplaceChild("hat1", CubeListBuilder.create().texOffs(208, 0).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.25F))
                .texOffs(224, 17).addBox(-6.0F, 2.0F, -10.5F, 12.0F, 1.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition hat2 = head.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(212, 22).addBox(-7.0F, 2.0F, -8.0F, 14.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(208, 38).addBox(-6.0F, -3.25F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition hat2_flap1 = hat2.addOrReplaceChild("hat2_flap1", CubeListBuilder.create().texOffs(234, 55).addBox(0.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition hat2_flap2 = hat2.addOrReplaceChild("hat2_flap2", CubeListBuilder.create().texOffs(234, 55).mirror().addBox(-3.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition hat3 = head.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(224, 82).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(224, 82).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition hat3_propeller = hat3.addOrReplaceChild("hat3_propeller", CubeListBuilder.create().texOffs(228, 93).addBox(-6.0F, -0.5F, -1.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(228, 96).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.5F, 0.0F));

        PartDefinition hat4 = head.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(208, 109).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition hat5 = head.addOrReplaceChild("hat5", CubeListBuilder.create().texOffs(188, 133).addBox(-4.0F, 2.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(208, 133).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition hat6 = head.addOrReplaceChild("hat6", CubeListBuilder.create().texOffs(208, 153).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(234, 153).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(216, 153).addBox(-3.0F, 3.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(232, 160).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition hat7 = head.addOrReplaceChild("hat7", CubeListBuilder.create().texOffs(200, 172).addBox(-7.0F, 4.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.25F))
                .texOffs(208, 188).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -14.0F, 0.0F));

        PartDefinition hat8 = head.addOrReplaceChild("hat8", CubeListBuilder.create().texOffs(208, 212).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition hat9 = head.addOrReplaceChild("hat9", CubeListBuilder.create().texOffs(166, 151).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition hat10 = head.addOrReplaceChild("hat10", CubeListBuilder.create().texOffs(168, 175).addBox(-2.0F, -18.0F, -6.0F, 8.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -18.0F, 2.0F));

        PartDefinition spike = head.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(154, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition spike2 = spike.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(154, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, -4.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition right_elbow = right_arm.addOrReplaceChild("right_elbow", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition right_hand = right_elbow.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(13, 42).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 42).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -4.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition left_elbow = left_arm.addOrReplaceChild("left_elbow", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition left_hand = left_elbow.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(13, 42).addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition buzzsaw = body.addOrReplaceChild("buzzsaw", CubeListBuilder.create().texOffs(-32, 52).addBox(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(-32, 52).addBox(-16.0F, 4.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(-32, 52).addBox(-16.0F, -4.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition quiver = body.addOrReplaceChild("quiver", CubeListBuilder.create().texOffs(41, 42).addBox(-5.0F, 0.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(64, 60).addBox(-4.0F, -6.0F, 3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = all.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 18).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -14.0F, 0.0F));

        PartDefinition right_foot = right_leg.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 0).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition left_leg = all.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -14.0F, 0.0F));

        PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        createPhase1Weapons(partdefinition, right_hand, left_hand);
        createPhase2Weapons(partdefinition, right_hand, left_hand);

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    // Although I haven't run into the 'code too large' error with Defender's model yet, I'm making these methods just to be safe
    private static void createPhase1Weapons(PartDefinition partdefinition, PartDefinition right_hand, PartDefinition left_hand) {

        PartDefinition sword = right_hand.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(98, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(106, 0).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(68, 0).addBox(-6.0F, -4.0F, -1.5F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(68, 12).addBox(-4.0F, -36.0F, -0.5F, 8.0F, 32.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.0F));

        PartDefinition blade_angle = sword.addOrReplaceChild("blade_angle", CubeListBuilder.create().texOffs(86, 12).addBox(-3.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, -36.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition boomerang = right_hand.addOrReplaceChild("boomerang", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 18.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(132, 0).addBox(-1.0F, -1.5F, -16.5F, 2.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(132, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -3.5F, -3.5F));

        PartDefinition shuriken_launcher = right_hand.addOrReplaceChild("shuriken_launcher", CubeListBuilder.create().texOffs(178, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(168, 14).addBox(-3.0F, -10.0F, -11.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(168, 34).addBox(-5.0F, -12.0F, -13.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(168, 48).addBox(-4.0F, -11.0F, 2.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(186, 0).addBox(-3.5F, -20.0F, -5.0F, 7.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, -3.0F));

        PartDefinition claw1 = right_hand.addOrReplaceChild("claw1", CubeListBuilder.create().texOffs(92, 27).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(96, 19).mirror().addBox(-3.0F, 1.0F, -12.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition claw2 = left_hand.addOrReplaceChild("claw2", CubeListBuilder.create().texOffs(92, 27).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(96, 19).mirror().addBox(-3.0F, 1.0F, -12.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition excalibur = right_hand.addOrReplaceChild("excalibur", CubeListBuilder.create().texOffs(86, 37).addBox(-1.5F, -14.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(98, 53).addBox(-6.0F, -14.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(132, 53).addBox(-3.0F, 2.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(136, 98).addBox(-12.0F, -15.0F, -1.5F, 24.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(110, 81).addBox(-6.0F, -63.0F, -0.5F, 12.0F, 48.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(78, 78).addBox(-8.0F, -63.0F, 0.0F, 16.0F, 48.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, -3.0F));

        PartDefinition blade_angle3 = excalibur.addOrReplaceChild("blade_angle3", CubeListBuilder.create().texOffs(94, 69).addBox(-5.0F, -5.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(112, 69).addBox(-7.0F, -7.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -61.75F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition excalihandle3 = excalibur.addOrReplaceChild("excalihandle3", CubeListBuilder.create().texOffs(122, 53).mirror().addBox(-2.5F, -13.5F, 0.0F, 5.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.0F, -16.25F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition excalihandle4 = excalibur.addOrReplaceChild("excalihandle4", CubeListBuilder.create().texOffs(122, 53).addBox(-2.5F, -13.5F, 0.0F, 5.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -16.25F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition excalibur_speed = excalibur.addOrReplaceChild("excalibur_speed", CubeListBuilder.create().texOffs(30, 84).mirror().addBox(-23.0F, -48.0F, 0.0F, 24.0F, 48.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition rock = partdefinition.addOrReplaceChild("rock", CubeListBuilder.create().texOffs(136, 58).addBox(3.0F, -20.0F, -27.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(-17.0F, -20.0F, -21.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.1F))
                .texOffs(136, 58).addBox(13.0F, -20.0F, -7.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(-33.0F, -20.0F, -28.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(-28.0F, -20.0F, -10.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.2F))
                .texOffs(136, 58).addBox(-32.0F, -20.0F, 10.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(-14.0F, -20.0F, 5.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.1F))
                .texOffs(136, 58).addBox(-1.0F, -20.0F, -8.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.2F))
                .texOffs(136, 58).addBox(3.0F, -20.0F, 19.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(1.0F, -20.0F, 4.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.1F))
                .texOffs(136, 58).addBox(-15.0F, -20.0F, 21.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.1F))
                .texOffs(136, 58).addBox(-42.0F, -20.0F, -10.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.1F))
                .texOffs(136, 58).addBox(-15.0F, -20.0F, -33.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.2F))
                .texOffs(136, 58).addBox(-15.0F, -30.0F, 9.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(136, 58).addBox(-4.0F, -34.0F, -5.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.1F))
                .texOffs(136, 58).addBox(-24.0F, -34.0F, -5.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.1F))
                .texOffs(136, 58).addBox(-21.0F, -34.0F, -17.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.1F))
                .texOffs(136, 58).addBox(-7.0F, -29.0F, -17.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(-0.2F))
                .texOffs(136, 58).addBox(-15.0F, -43.0F, -9.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition excalibur_fake = partdefinition.addOrReplaceChild("excalibur_fake", CubeListBuilder.create().texOffs(86, 37).addBox(-1.5F, -9.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(98, 53).addBox(-6.0F, -9.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(132, 53).addBox(-3.0F, 7.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(136, 98).addBox(-12.0F, -10.0F, -1.5F, 24.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(110, 81).addBox(-6.0F, -58.0F, -0.5F, 12.0F, 48.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(78, 78).addBox(-8.0F, -58.0F, 0.0F, 16.0F, 48.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -47.0F, 2.0F, 3.1416F, 0.0F, 0.0873F));

        PartDefinition blade_angle2 = excalibur_fake.addOrReplaceChild("blade_angle2", CubeListBuilder.create().texOffs(94, 69).addBox(-5.0F, -5.0F, -0.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(112, 69).addBox(-7.0F, -7.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -56.75F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition excalihandle1 = excalibur_fake.addOrReplaceChild("excalihandle1", CubeListBuilder.create().texOffs(122, 53).mirror().addBox(-2.5F, -13.5F, 0.0F, 5.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.0F, -11.25F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition excalihandle2 = excalibur_fake.addOrReplaceChild("excalihandle2", CubeListBuilder.create().texOffs(122, 53).addBox(-2.5F, -13.5F, 0.0F, 5.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -11.25F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition chainsaw_handle = partdefinition.addOrReplaceChild("chainsaw_handle", CubeListBuilder.create().texOffs(104, 21).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition pullstring = chainsaw_handle.addOrReplaceChild("pullstring", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -2.0F, 2.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition pull = pullstring.addOrReplaceChild("pull", CubeListBuilder.create().texOffs(152, 27).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 1.0F, -3.0F));

        PartDefinition string = pullstring.addOrReplaceChild("string", CubeListBuilder.create().texOffs(152, 33).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

    }

    private static void createPhase2Weapons(PartDefinition partdefinition, PartDefinition right_hand, PartDefinition left_hand) {

        PartDefinition ratatatabow = right_hand.addOrReplaceChild("ratatatabow", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 1.0F, -4.0F, -1.5708F, -0.6109F, 1.5708F));

        PartDefinition wood1 = ratatatabow.addOrReplaceChild("wood1", CubeListBuilder.create().texOffs(0, 92).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.5F));

        PartDefinition wood2 = wood1.addOrReplaceChild("wood2", CubeListBuilder.create().texOffs(0, 86).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition wood3 = wood2.addOrReplaceChild("wood3", CubeListBuilder.create().texOffs(0, 98).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition rope1 = wood3.addOrReplaceChild("rope1", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -1.5F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition rope_rotated1 = rope1.addOrReplaceChild("rope_rotated1", CubeListBuilder.create().texOffs(0, 84).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition wood4 = wood1.addOrReplaceChild("wood4", CubeListBuilder.create().texOffs(0, 86).addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition wood5 = wood4.addOrReplaceChild("wood5", CubeListBuilder.create().texOffs(0, 98).mirror().addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition rope2 = wood5.addOrReplaceChild("rope2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -1.5F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition rope_rotated2 = rope2.addOrReplaceChild("rope_rotated2", CubeListBuilder.create().texOffs(0, 84).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition poisondarts = right_hand.addOrReplaceChild("poisondarts", CubeListBuilder.create().texOffs(196, 64).addBox(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition forcegun = right_hand.addOrReplaceChild("forcegun", CubeListBuilder.create().texOffs(0, 104).addBox(-3.0F, -9.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 120).addBox(-1.0F, -11.0F, -8.0F, 0.0F, 14.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 120).addBox(-3.0F, -9.0F, -8.0F, 4.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(-6.0F, -11.0F, -14.0F, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.0F, -3.5F));

        PartDefinition handle = forcegun.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(18, 86).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, -5.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition sniper_rifle = right_hand.addOrReplaceChild("sniper_rifle", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.0F, -4.0F));

        PartDefinition sniper_rifle_rotated = sniper_rifle.addOrReplaceChild("sniper_rifle_rotated", CubeListBuilder.create().texOffs(136, 103).addBox(-7.0F, -5.0F, -1.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(156, 103).addBox(1.0F, -5.0F, -1.0F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(184, 103).addBox(13.0F, -5.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(176, 108).addBox(-1.0F, -9.0F, -1.5F, 16.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(136, 110).addBox(0.0F, -6.0F, 0.0F, 20.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition creeper_gun = right_hand.addOrReplaceChild("creeper_gun", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 2.0F, -3.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition creephead = creeper_gun.addOrReplaceChild("creephead", CubeListBuilder.create().texOffs(56, 132).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -26.0F));

        PartDefinition creepbody1 = creeper_gun.addOrReplaceChild("creepbody1", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition creepbody2 = creeper_gun.addOrReplaceChild("creepbody2", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition creepbody3 = creeper_gun.addOrReplaceChild("creepbody3", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition creepbody4 = creeper_gun.addOrReplaceChild("creepbody4", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition creepbody5 = creeper_gun.addOrReplaceChild("creepbody5", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -10.0F));

        PartDefinition creepleg = creeper_gun.addOrReplaceChild("creepleg", CubeListBuilder.create().texOffs(88, 142).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(88, 142).addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition wither_bazooka = right_hand.addOrReplaceChild("wither_bazooka", CubeListBuilder.create().texOffs(0, 148).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 148).addBox(-1.5F, -10.0F, -19.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 161).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F))
                .texOffs(0, 161).mirror().addBox(-1.5F, -5.0F, -19.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(26, 166).addBox(-1.5F, -8.0F, -4.5F, 3.0F, 3.0F, 4.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 161).mirror().addBox(-1.5F, 1.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(80, 152).addBox(-2.5F, -21.5F, -18.0F, 5.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -3.5F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r1 = wither_bazooka.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(72, 148).addBox(-2.0F, -12.0F, -1.0F, 3.0F, 12.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.5F, -1.0F, -0.5F, 0.8727F, 0.0F, 0.0F));

        PartDefinition cube_r2 = wither_bazooka.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(36, 148).addBox(-11.0F, -6.0F, -1.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.0F, -7.0F, -1.0F, 1.5708F, 0.7854F, 1.5708F));

        PartDefinition cube_r3 = wither_bazooka.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 167).addBox(-7.0F, -5.0F, -1.0F, 8.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -6.5F, -14.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r4 = wither_bazooka.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 160).addBox(-11.0F, -3.0F, -1.0F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.5F, -2.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r5 = wither_bazooka.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(12, 148).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 1.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition wbazooka_head = wither_bazooka.addOrReplaceChild("wbazooka_head", CubeListBuilder.create().texOffs(40, 162).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -22.0F));

    }

    @Override
    public void setupAnim(Defender defender, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot += (headPitch * ((float)Math.PI / 180F));

        this.animate(defender, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.animatePhase1(defender, ageInTicks);
        this.animatePhase2(defender, ageInTicks);
    }

    private void animatePhase1(Defender defender, float ageInTicks) {
        this.animate(defender.anim_saws, Phase1Animation.saws, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_sword, Phase1Animation.sword, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_axes, Phase1Animation.axes, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_boomerang, Phase1Animation.boomerang, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_spikes, Phase1Animation.spikes, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_spikes_land, Phase1Animation.spikes_land, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_spikes_slam, Phase1Animation.spikes_slam, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_shurikens, Phase1Animation.shurikenlauncher, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_chainsaw, Phase1Animation.chainsaw, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_claws_start, Phase1Animation.claws_start, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_claws_continue, Phase1Animation.claws_continue, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_claws_end, Phase1Animation.claws_end, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_claws_punch, Phase1Animation.claws_punch, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_excalibur, Phase1Animation.excalibur, ageInTicks, defender.getAnimationSpeed());
    }

    private void animatePhase2(Defender defender, float ageInTicks) {
        this.animate(defender.anim_ratatatabow, Phase2Animation.ratatatabow, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_ratatatabow2, Phase2Animation.ratatatabow2, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_poisondarts, Phase2Animation.poisondarts, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_forcegun, Phase2Animation.forcegun, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_snipe, Phase2Animation.snipe, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_sentryguns, Phase2Animation.sentryguns, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_icethrower, Phase2Animation.icethrower, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_witherbazooka, Phase2Animation.witherbazooka, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_witherbazooka_land, Phase2Animation.witherbazooka_land, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_creepergun, Phase2Animation.creepergun, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_flamethrower, Phase2Animation.flamethrower, ageInTicks, defender.getAnimationSpeed());
    }

    public void animate(Defender defender, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animate(defender.anim_jump, Phase1Animation.jump, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_jump2, Phase1Animation.jump2, ageInTicks, defender.getAnimationSpeed());
        this.animate(defender.anim_defeated, Phase1Animation.defeated, ageInTicks, defender.getAnimationSpeed());

        this.hat1.visible = (defender.getPhase() == 1 || defender.getSecondHat() == 1) && !defender.shouldHideAllHats();
        this.hat2.visible = (defender.getPhase() == 2 || defender.getSecondHat() == 2) && !defender.shouldHideAllHats();
        this.hat3.visible = (defender.getPhase() == 3 || defender.getSecondHat() == 3) && !defender.shouldHideAllHats();
        this.hat4.visible = (defender.getPhase() == 4 || defender.getSecondHat() == 4) && !defender.shouldHideAllHats();
        this.hat5.visible = (defender.getPhase() == 5 || defender.getSecondHat() == 5) && !defender.shouldHideAllHats();
        this.hat6.visible = (defender.getPhase() == 6 || defender.getSecondHat() == 6) && !defender.shouldHideAllHats();
        this.hat7.visible = (defender.getPhase() == 7 || defender.getSecondHat() == 7) && !defender.shouldHideAllHats();
        this.hat8.visible = (defender.getPhase() == 8 || defender.getSecondHat() == 8) && !defender.shouldHideAllHats();
        this.hat9.visible = (defender.getPhase() == 9 || defender.getSecondHat() == 9) && !defender.shouldHideAllHats();
        this.hat10.visible = (defender.getPhase() == 10 || defender.getSecondHat() == 10) && !defender.shouldHideAllHats();

        this.buzzsaw.visible = defender.getWeaponToShow() == 1;
        this.sword.visible = defender.getWeaponToShow() == 2;
        this.boomerang.visible = defender.getWeaponToShow() == 3;
        this.spike.visible = defender.getWeaponToShow() == 4;
        this.shuriken_launcher.visible = defender.getWeaponToShow() == 5;
        this.chainsaw_handle.visible = defender.getWeaponToShow() == 6;
        this.claw1.visible = defender.getWeaponToShow() == 7;
        this.claw2.visible = defender.getWeaponToShow() == 7;
        this.rock.visible = defender.getWeaponToShow() == 8;
        this.excalibur_fake.visible = defender.getWeaponToShow() == 8;
        this.excalibur.visible = defender.getWeaponToShow() == 8;
        this.ratatatabow.visible = defender.getWeaponToShow() == 9;
        this.quiver.visible = defender.getWeaponToShow() == 9;
        this.poisondarts.visible = defender.getWeaponToShow() == 10;
        this.forcegun.visible = defender.getWeaponToShow() == 11;
        this.sniper_rifle.visible = defender.getWeaponToShow() == 12;
        this.wither_bazooka.visible = defender.getWeaponToShow() == 13;
        this.creeper_gun.visible = defender.getWeaponToShow() == 15;

        this.chainsaw_handle.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.chainsaw_handle.xRot += defender.getChainsawLookX() * ((float)Math.PI / 180F);

        if (Objects.equals(defender.getAnimationState(), "none")) {
            float moveX = (float) (defender.getX() - defender.xo);
            float moveZ = (float) (defender.getZ() - defender.zo);
            float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
            if (speed > 0.2) {
                this.left_leg.xRot += Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.8F;
                this.right_leg.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.8F;
                this.animateRun(ageInTicks);
            } else {
                this.animateIdle(defender.getFrame());
            }
        }

        if (defender.getWeaponToShow() != 1 &&
                defender.getWeaponToShow() != 4 &&
                !Objects.equals(defender.getAnimationState(), "jump2")) {
            this.left_leg.xRot += Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.right_leg.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        }

        if (Objects.equals(defender.getAnimationState(), "creepergun") && defender.isFreakingOutInModel()) {
            float tick = (defender.getFrame() + this.partialTick);
            if ((Mth.cos(tick * 4) >= 0)) {
                this.all.xRot -= (50.0F * ((float)Math.PI / 180F));
                this.head.xRot -= (30.0F * ((float)Math.PI / 180F));
                this.right_arm.xRot -= (60.0F * ((float)Math.PI / 180F));
            }
        }
    }

    public void animateIdle(int fullTick) {
        int actualTick = fullTick % 40;
        float tick = ((float)actualTick + this.partialTick);
        float f3 = tick / 65.0F;

        float multiplier = 0.5F;
        this.body.y += (Mth.cos(f3 * 40) * multiplier / 2.0F);
        this.body.x += (Mth.cos(f3 * 10F) * multiplier);
        this.head.x += (Mth.cos(f3 * 20) * (multiplier / 2.0F));
        this.right_arm.xRot = (Mth.cos(f3 * 20) * (multiplier / 2.0F));
        this.left_arm.xRot = -this.right_arm.xRot;
    }

    public void animateRun(float fullTick) {
        float f3 = fullTick / 60.0F;

        float multiplier = 0.5F;
        this.body.xRot = 0.26179F;
        this.head.xRot += -0.26179F + (Math.abs(this.left_leg.xRot * 0.1F));
        this.right_arm.xRot = 0.78539F + (Mth.cos(f3 * 8) * (multiplier / 2.0F));
        this.left_arm.xRot = right_arm.xRot;
        this.body.z += -2;

        this.all.y += -Math.abs(this.left_leg.xRot * 2.0F);
    }

    @Override
    public void prepareMobModel(T entity, float p_102615_, float p_102616_, float p_102617_) {
        this.partialTick = p_102617_;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rock.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        excalibur_fake.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        chainsaw_handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
