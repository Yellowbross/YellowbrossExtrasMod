package com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.oryctolins.ConverslinAnimation;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.CustomHeadedModel;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.ConverslinEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.Random;

public class ConverslinModel<T extends Entity> extends HierarchicalModel<T> implements CustomHeadedModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "converslin"), "main");
    private final ModelPart root;
    private float partialTick;
    private final ModelPart all;
    private final Random random = new Random();

    public ConverslinModel(ModelPart root) {
        this.root = root;

        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(80, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition part = body.addOrReplaceChild("part", CubeListBuilder.create().texOffs(82, 14).addBox(-2.0F, -1.5F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -0.75F, -2.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(40, 0).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(3.0F, -9.0F, 0.0F, -0.7854F, 0.6109F, 0.0F));

        PartDefinition ear_bend1 = ear1.addOrReplaceChild("ear_bend1", CubeListBuilder.create().texOffs(12, 20).mirror().addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(0, 20).addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-3.0F, -9.0F, 0.0F, -0.7854F, -0.6109F, 0.0F));

        PartDefinition ear_bend2 = ear2.addOrReplaceChild("ear_bend2", CubeListBuilder.create().texOffs(12, 20).addBox(-2.0F, -8.0F, -1.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-1.5F, -2.0F, 0.0F));

        PartDefinition rarm_rotated = right_arm.addOrReplaceChild("rarm_rotated", CubeListBuilder.create().texOffs(88, 86).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition right_elbow = rarm_rotated.addOrReplaceChild("right_elbow", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition relbow_rotated = right_elbow.addOrReplaceChild("relbow_rotated", CubeListBuilder.create().texOffs(80, 89).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

        PartDefinition right_hand = relbow_rotated.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition rhand_rotated = right_hand.addOrReplaceChild("rhand_rotated", CubeListBuilder.create().texOffs(80, 78).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, -0.2618F));

        PartDefinition staff = rhand_rotated.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition staff_center = staff.addOrReplaceChild("staff_center", CubeListBuilder.create().texOffs(40, 66).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 28.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 70).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 84).addBox(-3.0F, -18.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 72).addBox(-3.0F, -18.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 20).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 2.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(1.5F, -2.0F, 0.0F));

        PartDefinition larm_rotated = left_arm.addOrReplaceChild("larm_rotated", CubeListBuilder.create().texOffs(88, 86).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6109F, 0.0F, -0.6109F));

        PartDefinition left_elbow = larm_rotated.addOrReplaceChild("left_elbow", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition lelbow_rotated = left_elbow.addOrReplaceChild("lelbow_rotated", CubeListBuilder.create().texOffs(80, 89).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.3963F, -0.6109F, 0.0F));

        PartDefinition left_hand = lelbow_rotated.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lhand_rotated = left_hand.addOrReplaceChild("lhand_rotated", CubeListBuilder.create().texOffs(80, 78).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.6109F));

        PartDefinition lower_half = body.addOrReplaceChild("lower_half", CubeListBuilder.create().texOffs(0, 76).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition right_foot = lower_half.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(60, 0).addBox(-2.0F, 8.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition left_foot = lower_half.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(60, 0).mirror().addBox(-2.0F, 8.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        ModelPart body = this.all.getChild("body");
        ModelPart head = body.getChild("head");
        ModelPart leg1 = body.getChild("lower_half").getChild("right_foot");
        ModelPart leg2 = body.getChild("lower_half").getChild("left_foot");
        ModelPart arm1 = body.getChild("right_arm");
        ModelPart arm2 = body.getChild("left_arm");
        ModelPart elbow1 = arm1.getChild("rarm_rotated").getChild("right_elbow");
        ModelPart elbow2 = arm2.getChild("larm_rotated").getChild("left_elbow");

        head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        head.xRot += (headPitch * ((float)Math.PI / 180F));

        float twitchamount = 125;

        if (this.riding) {
            body.getChild("lower_half").xRot = -1.4137167F;
        }

        if (entity instanceof ConverslinEntity oryctolin) {
            this.animate(oryctolin.anim_celebrate, ConverslinAnimation.celebrate, ageInTicks, oryctolin.getAnimationSpeed());
            this.animate(oryctolin.anim_attack1, ConverslinAnimation.attack1, ageInTicks, oryctolin.getAnimationSpeed());

            if (oryctolin.getAnimationState().equals("none")) {
                float moveX = (float) (oryctolin.getX() - oryctolin.xo);
                float moveZ = (float) (oryctolin.getZ() - oryctolin.zo);
                float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
                leg1.xRot += Mth.cos(limbSwing * 2.6648F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.8F;
                leg2.xRot += Mth.cos(limbSwing * 2.6648F) * 1.4F * limbSwingAmount * 0.8F;
                if (speed > 0.2) {

                } else {
                    this.animateIdle(oryctolin.getFrame());
                }
                if (oryctolin.getHealth() < (oryctolin.getMaxHealth() / 2)) {
                    arm1.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    arm1.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    arm1.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;

                    arm2.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    arm2.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    arm2.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;

                    elbow1.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    elbow1.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    elbow1.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;

                    elbow2.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    elbow2.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                    elbow2.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
                }
            }
        }
    }

    public void animateIdle(int fullTick) {
        int actualTick = fullTick % 80;
        float tick = ((float)actualTick + this.partialTick);
        float f3 = tick / 65.0F;
        ModelPart body = this.all.getChild("body");
        ModelPart head = body.getChild("head");
        ModelPart lower_half = body.getChild("lower_half");

        float multiplier = 0.5F;
        body.x += (Mth.cos(f3 * 10) * multiplier / 2.0F);
        body.y += (Mth.cos(f3 * 20) * multiplier / 2.0F);
        head.x += (Mth.cos(f3 * 10) * (multiplier / 2.0F));
        head.y += (Mth.cos(f3 * 20) * (multiplier / 2.0F));
        lower_half.x += -(Mth.cos(f3 * 10) * multiplier / 2.0F);
        lower_half.y += -(Mth.cos(f3 * 20) * multiplier / 2.0F);
        lower_half.xScale += Math.abs((Mth.cos(f3 * 10) * multiplier / 2.0F) * 0.25F);
        lower_half.zScale += Math.abs((Mth.cos(f3 * 10) * multiplier / 2.0F) * 0.25F);
        lower_half.yScale += -Math.abs((Mth.cos(f3 * 10) * multiplier / 2.0F) * 0.25F);
        this.all.y += Math.abs((Mth.cos(f3 * 10) * multiplier / 2.0F) * 2.0F);
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
        return this.all.getChild("body").getChild("head");
    }

    @Override
    public void translateToHead(PoseStack stack) {
        this.root().translateAndRotate(stack);
        this.all.translateAndRotate(stack);
        this.all.getChild("body").translateAndRotate(stack);
        this.all.getChild("body").getChild("head").translateAndRotate(stack);
        stack.scale(1.2F, 1.2F, 1.2F);
    }
}
