package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.defender.CreeperBulletAnimation;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class CreeperBulletModel<T extends Entity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "creeper_bullet"), "main");

    private final ModelPart root;
    private final ModelPart aim;
    private final ModelPart creepbody1;
    private final ModelPart creepleg1;
    private final ModelPart creepleg2;
    private final ModelPart creepleg3;
    private final ModelPart creepleg4;
    private final ModelPart creephead;
    private final boolean charged;

    public CreeperBulletModel(ModelPart root, boolean charged) {
        this.root = root;
        this.aim = root.getChild("aim");
        this.creepbody1 = this.aim.getChild("creepbody1");
        this.creepleg1 = this.creepbody1.getChild("creepleg1");
        this.creepleg2 = this.creepbody1.getChild("creepleg2");
        this.creepleg3 = this.creepbody1.getChild("creepleg3");
        this.creepleg4 = this.creepbody1.getChild("creepleg4");
        this.creephead = this.creepbody1.getChild("creephead");
        this.charged = charged;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition aim = partdefinition.addOrReplaceChild("aim", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

        PartDefinition creepbody1 = aim.addOrReplaceChild("creepbody1", CubeListBuilder.create().texOffs(88, 126).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 1.0F));

        PartDefinition creepleg1 = creepbody1.addOrReplaceChild("creepleg1", CubeListBuilder.create().texOffs(88, 142).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 2.0F));

        PartDefinition creepleg2 = creepbody1.addOrReplaceChild("creepleg2", CubeListBuilder.create().texOffs(88, 142).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 2.0F));

        PartDefinition creepleg3 = creepbody1.addOrReplaceChild("creepleg3", CubeListBuilder.create().texOffs(88, 142).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, -2.0F));

        PartDefinition creepleg4 = creepbody1.addOrReplaceChild("creepleg4", CubeListBuilder.create().texOffs(88, 142).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -2.0F));

        PartDefinition creephead = creepbody1.addOrReplaceChild("creephead", CubeListBuilder.create().texOffs(56, 132).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.creephead.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.creephead.xRot += (headPitch * ((float)Math.PI / 180F));

        this.creepleg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.creepleg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.creepleg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.creepleg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if (entity instanceof CreeperBullet creeper) {
            if (creeper.getAnimationState().equals("fly")) {
                this.aim.yRot += creeper.getShootY() * ((float)Math.PI / 180F);
                this.aim.xRot += creeper.getShootX() * ((float)Math.PI / 180F);
            }

            this.animate(creeper.anim_fly, CreeperBulletAnimation.fly, ageInTicks, creeper.getAnimationSpeed());
            this.animate(creeper.anim_getup, CreeperBulletAnimation.getup, ageInTicks, creeper.getAnimationSpeed());
            this.animate(creeper.anim_falling, CreeperBulletAnimation.falling, ageInTicks, creeper.getAnimationSpeed());
        }

        if (this.charged) {
            Vector3f scale = new Vector3f(0.1f, 0.1f, 0.1f);
            this.aim.getAllParts().forEach(modelPart -> modelPart.offsetScale(scale));
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        aim.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

}
