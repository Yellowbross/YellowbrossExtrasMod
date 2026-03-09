package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.defender.DefenderAnimation;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DefenderModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender"), "main");
    private final ModelPart root;
    private float partialTick;
    private final ModelPart all;
    private final ModelPart rock;
    private final ModelPart excalibur_fake;
    private final ModelPart chainsaw_handle;

    public DefenderModel(ModelPart root) {
        this.root = root;
        this.all = root.getChild("all");
        this.rock = root.getChild("rock");
        this.excalibur_fake = root.getChild("excalibur_fake");
        this.chainsaw_handle = root.getChild("chainsaw_handle");
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

        PartDefinition arm1 = body.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, -4.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition elbow1 = arm1.addOrReplaceChild("elbow1", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition hand1 = elbow1.addOrReplaceChild("hand1", CubeListBuilder.create().texOffs(13, 42).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 42).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition sword = hand1.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(98, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(106, 0).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(68, 0).addBox(-6.0F, -4.0F, -1.5F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(68, 12).addBox(-4.0F, -36.0F, -0.5F, 8.0F, 32.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -4.0F));

        PartDefinition blade_angle = sword.addOrReplaceChild("blade_angle", CubeListBuilder.create().texOffs(86, 12).addBox(-3.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.0F, -36.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition boomerang = hand1.addOrReplaceChild("boomerang", CubeListBuilder.create().texOffs(122, 0).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 18.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(132, 0).addBox(-1.0F, -1.5F, -16.5F, 2.0F, 3.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(132, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -3.5F, -3.5F));

        PartDefinition shuriken_launcher = hand1.addOrReplaceChild("shuriken_launcher", CubeListBuilder.create().texOffs(178, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(168, 14).addBox(-3.0F, -10.0F, -11.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(168, 34).addBox(-5.0F, -12.0F, -13.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(168, 48).addBox(-4.0F, -11.0F, 2.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(186, 0).addBox(-3.5F, -20.0F, -5.0F, 7.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, -3.0F));

        PartDefinition claw1 = hand1.addOrReplaceChild("claw1", CubeListBuilder.create().texOffs(92, 27).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(96, 19).mirror().addBox(-3.0F, 1.0F, -12.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition excalibur = hand1.addOrReplaceChild("excalibur", CubeListBuilder.create().texOffs(86, 37).addBox(-1.5F, -14.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
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

        PartDefinition ratatatabow = hand1.addOrReplaceChild("ratatatabow", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 1.0F, -4.0F, -1.5708F, -0.6109F, 1.5708F));

        PartDefinition wood1 = ratatatabow.addOrReplaceChild("wood1", CubeListBuilder.create().texOffs(0, 92).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.5F));

        PartDefinition wood2 = wood1.addOrReplaceChild("wood2", CubeListBuilder.create().texOffs(0, 86).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition wood3 = wood2.addOrReplaceChild("wood3", CubeListBuilder.create().texOffs(0, 98).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition rope1 = wood3.addOrReplaceChild("rope1", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -1.5F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition rope_rotated1 = rope1.addOrReplaceChild("rope_rotated1", CubeListBuilder.create().texOffs(0, 84).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition wood4 = wood1.addOrReplaceChild("wood4", CubeListBuilder.create().texOffs(0, 86).addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition wood5 = wood4.addOrReplaceChild("wood5", CubeListBuilder.create().texOffs(0, 98).mirror().addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition rope2 = wood5.addOrReplaceChild("rope2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -1.5F, 0.0F, 0.0F, 0.0F, -0.6981F));

        PartDefinition rope_rotated2 = rope2.addOrReplaceChild("rope_rotated2", CubeListBuilder.create().texOffs(0, 84).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition arm2 = body.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, -4.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition elbow2 = arm2.addOrReplaceChild("elbow2", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition hand2 = elbow2.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(13, 42).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 42).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition claw2 = hand2.addOrReplaceChild("claw2", CubeListBuilder.create().texOffs(92, 27).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(96, 19).mirror().addBox(-3.0F, 1.0F, -12.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition buzzsaw = body.addOrReplaceChild("buzzsaw", CubeListBuilder.create().texOffs(-32, 52).addBox(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(-32, 52).addBox(-16.0F, 4.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(-32, 52).addBox(-16.0F, -4.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition quiver = body.addOrReplaceChild("quiver", CubeListBuilder.create().texOffs(41, 42).addBox(-5.0F, 0.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(64, 60).addBox(-4.0F, -6.0F, 3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg2 = all.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(48, 18).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -14.0F, 0.0F));

        PartDefinition foot2 = leg2.addOrReplaceChild("foot2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 0).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition leg1 = all.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(48, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -14.0F, 0.0F));

        PartDefinition foot1 = leg1.addOrReplaceChild("foot1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

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

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void animate(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ModelPart body = this.all.getChild("body");
        ModelPart leg1 = this.all.getChild("leg1");
        ModelPart leg2 = this.all.getChild("leg2");

        ModelPart head = body.getChild("head");
        ModelPart hand1 = body.getChild("arm1").getChild("elbow1").getChild("hand1");
        ModelPart hand2 = body.getChild("arm2").getChild("elbow2").getChild("hand2");
        ModelPart hat1 = head.getChild("hat1");
        ModelPart hat2 = head.getChild("hat2");
        ModelPart hat3 = head.getChild("hat3");
        ModelPart hat4 = head.getChild("hat4");
        ModelPart hat5 = head.getChild("hat5");
        ModelPart hat6 = head.getChild("hat6");
        ModelPart hat7 = head.getChild("hat7");
        ModelPart hat8 = head.getChild("hat8");
        ModelPart hat9 = head.getChild("hat9");
        ModelPart hat10 = head.getChild("hat10");

        ModelPart buzzsaw = body.getChild("buzzsaw");
        ModelPart sword = hand1.getChild("sword");
        ModelPart boomerang = hand1.getChild("boomerang");
        ModelPart spike = head.getChild("spike");
        ModelPart shuriken_launcher = hand1.getChild("shuriken_launcher");
        ModelPart chainsaw_handle = this.chainsaw_handle;
        ModelPart claw1 = hand1.getChild("claw1");
        ModelPart claw2 = hand2.getChild("claw2");
        ModelPart excalibur = hand1.getChild("excalibur");
        ModelPart ratatatabow = hand1.getChild("ratatatabow");
        ModelPart quiver = body.getChild("quiver");

        head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        head.xRot += (headPitch * ((float)Math.PI / 180F));

        if (entity instanceof DefenderEntity defender) {
            this.animate(defender.getAnimationState("jump"), DefenderAnimation.JUMP, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("defeated"), DefenderAnimation.DEFEATED, ageInTicks, defender.getAnimationSpeed());

            this.animate(defender.getAnimationState("saws"), DefenderAnimation.SAWS, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("sword"), DefenderAnimation.SWORD, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("axes"), DefenderAnimation.AXES, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("boomerang"), DefenderAnimation.BOOMERANG, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("spikes"), DefenderAnimation.SPIKES, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("spikes_land"), DefenderAnimation.SPIKES_LAND, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("spikes_slam"), DefenderAnimation.SPIKES_SLAM, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("shuriken_launcher"), DefenderAnimation.SHURIKENLAUNCHER, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("chainsaw"), DefenderAnimation.CHAINSAW, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("claws_start"), DefenderAnimation.CLAWS_START, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("claws_continue"), DefenderAnimation.CLAWS_CONTINUE, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("claws_end"), DefenderAnimation.CLAWS_END, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("claws_punch"), DefenderAnimation.CLAWS_PUNCH, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("excalibur"), DefenderAnimation.EXCALIBUR, ageInTicks, defender.getAnimationSpeed());

            this.animate(defender.getAnimationState("ratatatabow"), DefenderAnimation.RATATATABOW, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.getAnimationState("ratatatabow2"), DefenderAnimation.RATATATABOW2, ageInTicks, defender.getAnimationSpeed());

            hat1.visible = (defender.getPhase() == 1 || defender.getSecondHat() == 1) && !defender.shouldHideAllHats();
            hat2.visible = (defender.getPhase() == 2 || defender.getSecondHat() == 2) && !defender.shouldHideAllHats();
            hat3.visible = (defender.getPhase() == 3 || defender.getSecondHat() == 3) && !defender.shouldHideAllHats();
            hat4.visible = (defender.getPhase() == 4 || defender.getSecondHat() == 4) && !defender.shouldHideAllHats();
            hat5.visible = (defender.getPhase() == 5 || defender.getSecondHat() == 5) && !defender.shouldHideAllHats();
            hat6.visible = (defender.getPhase() == 6 || defender.getSecondHat() == 6) && !defender.shouldHideAllHats();
            hat7.visible = (defender.getPhase() == 7 || defender.getSecondHat() == 7) && !defender.shouldHideAllHats();
            hat8.visible = (defender.getPhase() == 8 || defender.getSecondHat() == 8) && !defender.shouldHideAllHats();
            hat9.visible = (defender.getPhase() == 9 || defender.getSecondHat() == 9) && !defender.shouldHideAllHats();
            hat10.visible = (defender.getPhase() == 10 || defender.getSecondHat() == 10) && !defender.shouldHideAllHats();

            buzzsaw.visible = defender.getWeaponToShow() == 1;
            sword.visible = defender.getWeaponToShow() == 2;
            boomerang.visible = defender.getWeaponToShow() == 3;
            spike.visible = defender.getWeaponToShow() == 4;
            shuriken_launcher.visible = defender.getWeaponToShow() == 5;
            chainsaw_handle.visible = defender.getWeaponToShow() == 6;
            claw1.visible = defender.getWeaponToShow() == 7;
            claw2.visible = defender.getWeaponToShow() == 7;
            this.rock.visible = defender.getWeaponToShow() == 8;
            this.excalibur_fake.visible = defender.getWeaponToShow() == 8;
            excalibur.visible = defender.getWeaponToShow() == 8;
            ratatatabow.visible = defender.getWeaponToShow() == 9;
            quiver.visible = defender.getWeaponToShow() == 9;

            chainsaw_handle.yRot += netHeadYaw * ((float)Math.PI / 180F);
            chainsaw_handle.xRot += defender.getChainsawLookX() * ((float)Math.PI / 180F);

            if (defender.getAnimationState() == 0) {
                float moveX = (float) (defender.getX() - defender.xo);
                float moveZ = (float) (defender.getZ() - defender.zo);
                float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
                if (speed > 0.2) {
                    leg1.xRot += Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.8F;
                    leg2.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.8F;
                    this.animateRun(ageInTicks);
                } else {
                    this.animateIdle(defender.getFrame());
                }
            }

            if (defender.getWeaponToShow() != 1 &&
                    defender.getWeaponToShow() != 4) {
                leg1.xRot += Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
                leg2.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            }
        }
    }

    public void animateIdle(int fullTick) {
        int actualTick = fullTick % 40;
        float tick = ((float)actualTick + this.partialTick);
        float f3 = tick / 65.0F;
        ModelPart body = this.all.getChild("body");
        ModelPart leg1 = this.all.getChild("leg1");
        ModelPart leg2 = this.all.getChild("leg2");
        ModelPart head = body.getChild("head");
        ModelPart right_arm = body.getChild("arm1");
        ModelPart left_arm = body.getChild("arm2");

        float multiplier = 0.5F;
        body.y += (Mth.cos(f3 * 40) * multiplier / 2.0F);
        body.x += (Mth.cos(f3 * 10F) * multiplier);
        head.x += (Mth.cos(f3 * 20) * (multiplier / 2.0F));
        right_arm.xRot = (Mth.cos(f3 * 20) * (multiplier / 2.0F));
        left_arm.xRot = -right_arm.xRot;
    }

    public void animateRun(float fullTick) {
        float f3 = fullTick / 60.0F;
        ModelPart body = this.all.getChild("body");
        ModelPart leg1 = this.all.getChild("leg1");
        ModelPart leg2 = this.all.getChild("leg2");
        ModelPart head = body.getChild("head");
        ModelPart right_arm = body.getChild("arm1");
        ModelPart left_arm = body.getChild("arm2");

        float multiplier = 0.5F;
        body.xRot = 0.26179F;
        head.xRot += -0.26179F + (Math.abs(leg1.xRot * 0.1F));
        right_arm.xRot = 0.78539F + (Mth.cos(f3 * 8) * (multiplier / 2.0F));
        left_arm.xRot = right_arm.xRot;
        body.z += -2;

        this.all.y += -Math.abs(leg1.xRot * 2.0F);
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
