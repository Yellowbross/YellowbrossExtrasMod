package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class WatchYoToneBuddyBoyModel<T extends Defender> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "watchyotonebuddyboy"), "main");
    private final ModelPart all;
    private final ModelPart body;
    private final ModelPart body2;
    private final ModelPart head;
    private final ModelPart eyeball1;
    private final ModelPart eyeball2;
    private final ModelPart arm1;
    private final ModelPart elbow1;
    private final ModelPart hand1;
    private final ModelPart arm2;
    private final ModelPart elbow2;
    private final ModelPart hand2;
    private final ModelPart left_leg;
    private final ModelPart left_foot;
    private final ModelPart left_leg2;
    private final ModelPart left_foot2;

    public WatchYoToneBuddyBoyModel(ModelPart root) {
        this.all = root.getChild("all");
        this.body = this.all.getChild("body");
        this.body2 = this.body.getChild("body2");
        this.head = this.body2.getChild("head");
        this.eyeball1 = this.head.getChild("eyeball1");
        this.eyeball2 = this.head.getChild("eyeball2");
        this.arm1 = this.all.getChild("arm1");
        this.elbow1 = this.arm1.getChild("elbow1");
        this.hand1 = this.arm1.getChild("hand1");
        this.arm2 = this.all.getChild("arm2");
        this.elbow2 = this.arm2.getChild("elbow2");
        this.hand2 = this.all.getChild("hand2");
        this.left_leg = this.all.getChild("left_leg");
        this.left_foot = this.left_leg.getChild("left_foot");
        this.left_leg2 = this.all.getChild("left_leg2");
        this.left_foot2 = this.left_leg2.getChild("left_foot2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -7.0F, -3.0F, 9.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -25.0F, -1.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(30, 0).addBox(-5.5F, -11.0F, -5.0F, 11.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition head = body2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 74).addBox(-16.0F, -30.0F, -13.0F, 28.0F, 30.0F, 24.0F, new CubeDeformation(6.0F))
                .texOffs(220, 116).addBox(-5.0F, -42.0F, -1.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, -7.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-23.0F, -20.0F, -1.0F, 24.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(25.0F, -33.0F, -20.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 42).addBox(-23.0F, -20.0F, -1.0F, 24.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -29.0F, -20.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(104, 62).addBox(-45.0F, -54.0F, -1.0F, 46.0F, 54.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, 19.0F, -34.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(220, 108).addBox(-5.0F, -2.0F, -1.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -37.0F, -6.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition eyeball1 = head.addOrReplaceChild("eyeball1", CubeListBuilder.create().texOffs(126, 34).addBox(-7.0F, -6.0F, -16.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-18.0F, -39.0F, -19.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition eyeball2 = head.addOrReplaceChild("eyeball2", CubeListBuilder.create().texOffs(126, 34).mirror().addBox(-7.0F, -6.0F, -16.0F, 14.0F, 12.0F, 16.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(13.0F, -39.0F, -19.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition arm1 = all.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(62, 0).addBox(-23.0F, -2.0F, -1.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -36.6933F, -9.3294F, 0.8116F, 0.0F, 0.0F));

        PartDefinition elbow1 = arm1.addOrReplaceChild("elbow1", CubeListBuilder.create().texOffs(62, 0).addBox(-23.0F, -2.0F, -1.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition hand1 = arm1.addOrReplaceChild("hand1", CubeListBuilder.create().texOffs(18, 28).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0F, 20.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r5 = hand1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -5.0F, -1.0F, 4.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.6933F, -0.6706F, 0.0F, 0.6981F, 0.0F));

        PartDefinition arm2 = all.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(114, 0).mirror().addBox(0.0F, -1.0F, 0.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, -38.0F, -10.0F, 0.0F, 0.6981F, 0.6981F));

        PartDefinition elbow2 = arm2.addOrReplaceChild("elbow2", CubeListBuilder.create().texOffs(114, 0).mirror().addBox(-2.0F, -1.0F, 0.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(32.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -1.309F));

        PartDefinition hand2 = all.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(60, 42).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(84, 29).addBox(-6.0F, -16.0F, -3.0F, 12.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(117, 10).addBox(-6.0F, -40.0F, 2.0F, 4.0F, 24.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(36.0F, -43.0F, -42.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition cube_r6 = hand2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(89, 21).addBox(-9.0F, -4.0F, -1.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(1.0F, -12.0F, -5.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition left_leg = all.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -14.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r7 = left_leg.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(248, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r8 = left_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(248, 0).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -0.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition left_foot = left_leg.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(208, 0).addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(228, 0).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition left_leg2 = all.addOrReplaceChild("left_leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -14.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r9 = left_leg2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(248, 0).mirror().addBox(-1.0F, -10.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r10 = left_leg2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(248, 0).mirror().addBox(-1.0F, -10.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, -0.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition left_foot2 = left_leg2.addOrReplaceChild("left_foot2", CubeListBuilder.create().texOffs(208, 0).mirror().addBox(-3.0F, 1.0F, -6.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(228, 0).mirror().addBox(-3.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 128);
    }

    @Override
    public void setupAnim(Defender entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.all.getAllParts().forEach(ModelPart::resetPose);
        this.eyeball1.xRot += headPitch * ((float)Math.PI / 180F);
        this.eyeball2.xRot += headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
