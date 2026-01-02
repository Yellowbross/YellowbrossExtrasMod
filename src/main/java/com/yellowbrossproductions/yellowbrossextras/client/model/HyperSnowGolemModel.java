package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.HyperSnowGolemEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class HyperSnowGolemModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "hyper_snow_golem"), "main");
    private final ModelPart body_bottom;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart hat;
    private final ModelPart right_hand;
    private final ModelPart left_hand;
    private final ModelPart right_hand_standby;
    private final ModelPart elbow1;
    private final ModelPart left_hand_standby;
    private final ModelPart elbow2;
    private final ModelPart spinningarms;
    private final ModelPart spinningarms_right;
    private final ModelPart spin1;
    private final ModelPart spin2;
    private final ModelPart spin3;
    private final ModelPart spinningarms_left;
    private final ModelPart spin4;
    private final ModelPart spin5;
    private final ModelPart spin6;

    public HyperSnowGolemModel(ModelPart root) {
        this.body_bottom = root.getChild("body_bottom");
        this.body = this.body_bottom.getChild("body");
        this.head = this.body.getChild("head");
        this.headwear = this.head.getChild("headwear");
        this.hat = this.head.getChild("hat");
        this.right_hand = this.body.getChild("right_hand");
        this.left_hand = this.body.getChild("left_hand");
        this.right_hand_standby = this.body.getChild("right_hand_standby");
        this.elbow1 = this.right_hand_standby.getChild("elbow1");
        this.left_hand_standby = this.body.getChild("left_hand_standby");
        this.elbow2 = this.left_hand_standby.getChild("elbow2");
        this.spinningarms = this.body.getChild("spinningarms");
        this.spinningarms_right = this.spinningarms.getChild("spinningarms_right");
        this.spin1 = this.spinningarms_right.getChild("spin1");
        this.spin2 = this.spinningarms_right.getChild("spin2");
        this.spin3 = this.spinningarms_right.getChild("spin3");
        this.spinningarms_left = this.spinningarms.getChild("spinningarms_left");
        this.spin4 = this.spinningarms_left.getChild("spin4");
        this.spin5 = this.spinningarms_left.getChild("spin5");
        this.spin6 = this.spinningarms_left.getChild("spin6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body_bottom = partdefinition.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = body_bottom.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition headwear = head.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(64, 0).addBox(-12.0F, -16.0F, -4.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-3.0F)), PartPose.offset(4.0F, 3.0F, -4.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(80, 50).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(96, 34).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition right_hand = body.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(-4.0F, -7.0F, 0.0F));

        PartDefinition left_hand = body.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(32, 0).addBox(-0.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offset(4.0F, -7.0F, 0.0F));

        PartDefinition right_hand_standby = body.addOrReplaceChild("right_hand_standby", CubeListBuilder.create().texOffs(32, 4).mirror().addBox(-5.5F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition elbow1 = right_hand_standby.addOrReplaceChild("elbow1", CubeListBuilder.create().texOffs(32, 4).mirror().addBox(-5.5F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition left_hand_standby = body.addOrReplaceChild("left_hand_standby", CubeListBuilder.create().texOffs(32, 4).mirror().addBox(-0.5F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(4.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition elbow2 = left_hand_standby.addOrReplaceChild("elbow2", CubeListBuilder.create().texOffs(32, 4).mirror().addBox(-0.5F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition spinningarms = body.addOrReplaceChild("spinningarms", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition spinningarms_right = spinningarms.addOrReplaceChild("spinningarms_right", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition spin1 = spinningarms_right.addOrReplaceChild("spin1", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition spin2 = spinningarms_right.addOrReplaceChild("spin2", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition spin3 = spinningarms_right.addOrReplaceChild("spin3", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition spinningarms_left = spinningarms.addOrReplaceChild("spinningarms_left", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, -6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F, 1.5708F));

        PartDefinition spin4 = spinningarms_left.addOrReplaceChild("spin4", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition spin5 = spinningarms_left.addOrReplaceChild("spin5", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition spin6 = spinningarms_left.addOrReplaceChild("spin6", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-11.0F, -1.0F, 4.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.0F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof HyperSnowGolemEntity snowGolem) {
            this.left_hand_standby.visible = snowGolem.getTarget() == null;
            this.right_hand_standby.visible = snowGolem.getTarget() == null;
            this.spinningarms.visible = snowGolem.getTarget() != null;

            this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.head.xRot = headPitch * ((float)Math.PI / 180F);
            this.spinningarms.xRot = (ageInTicks * 150) * ((float)Math.PI / 180F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
