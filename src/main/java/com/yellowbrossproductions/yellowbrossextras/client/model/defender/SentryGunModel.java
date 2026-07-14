package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.defender.SentryGunAnimation;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGun;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SentryGunModel<T extends Entity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "sentry_gun"), "main");
    private final ModelPart root;
    private final ModelPart box;
    private final ModelPart flap1;
    private final ModelPart flap2;
    private final ModelPart flap3;
    private final ModelPart flap4;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart cannon;
    private final ModelPart hat;
    private final ModelPart left_arm;
    private final ModelPart left_hand;
    private final ModelPart right_arm;
    private final ModelPart right_hand;

    public SentryGunModel(ModelPart root) {
        this.root = root;
        this.box = root.getChild("box");
        this.flap1 = this.box.getChild("flap1");
        this.flap2 = this.box.getChild("flap2");
        this.flap3 = this.box.getChild("flap3");
        this.flap4 = this.box.getChild("flap4");
        this.body = this.box.getChild("body");
        this.head = this.body.getChild("head");
        this.cannon = this.head.getChild("cannon");
        this.hat = this.head.getChild("hat");
        this.left_arm = this.body.getChild("left_arm");
        this.left_hand = this.left_arm.getChild("left_hand");
        this.right_arm = this.body.getChild("right_arm");
        this.right_hand = this.right_arm.getChild("right_hand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition box = partdefinition.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.0F, -1.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition flap1 = box.addOrReplaceChild("flap1", CubeListBuilder.create().texOffs(32, 59).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -4.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition flap2 = box.addOrReplaceChild("flap2", CubeListBuilder.create().texOffs(32, 59).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, 1.309F, -1.5708F, 0.0F));

        PartDefinition flap3 = box.addOrReplaceChild("flap3", CubeListBuilder.create().texOffs(32, 59).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, 1.309F, 3.1416F, 0.0F));

        PartDefinition flap4 = box.addOrReplaceChild("flap4", CubeListBuilder.create().texOffs(32, 59).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 0.0F, 1.309F, 1.5708F, 0.0F));

        PartDefinition body = box.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition cannon = head.addOrReplaceChild("cannon", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-3.0F, -3.0F, -8.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 8).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(40, 19).addBox(-4.0F, -0.75F, -8.5F, 8.0F, 1.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.5F, 0.0F, -0.6981F, -0.5236F, 0.0F));

        PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -6.5F, 0.0F, -0.6981F, 0.5236F, 0.0F));

        PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot += (headPitch * ((float)Math.PI / 180F));

        if (entity instanceof SentryGun defender) {
            this.animate(defender.anim_shoot, SentryGunAnimation.shoot, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.anim_intro, SentryGunAnimation.intro, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.anim_flying, SentryGunAnimation.flying, ageInTicks, defender.getAnimationSpeed());
            this.animate(defender.anim_mitosis, SentryGunAnimation.mitosis, ageInTicks, defender.getAnimationSpeed());
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        box.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
