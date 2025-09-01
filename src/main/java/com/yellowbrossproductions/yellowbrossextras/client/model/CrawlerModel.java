package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class CrawlerModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "crawler"), "main");
    private final ModelPart center;

    public CrawlerModel(ModelPart root) {
        this.center = root.getChild("center");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition center = partdefinition.addOrReplaceChild("center", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition leg1 = center.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition legrotated1 = leg1.addOrReplaceChild("legrotated1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition knee1 = legrotated1.addOrReplaceChild("knee1", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6581F));

        PartDefinition knee1_r1 = knee1.addOrReplaceChild("knee1_r1", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg2 = center.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition legrotated2 = leg2.addOrReplaceChild("legrotated2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition knee2 = legrotated2.addOrReplaceChild("knee2", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6581F));

        PartDefinition knee1_r2 = knee2.addOrReplaceChild("knee1_r2", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg3 = center.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition legrotated3 = leg3.addOrReplaceChild("legrotated3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition knee3 = legrotated3.addOrReplaceChild("knee3", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6581F));

        PartDefinition knee1_r3 = knee3.addOrReplaceChild("knee1_r3", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg4 = center.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition legrotated4 = leg4.addOrReplaceChild("legrotated4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition knee4 = legrotated4.addOrReplaceChild("knee4", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6581F));

        PartDefinition knee1_r4 = knee4.addOrReplaceChild("knee1_r4", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = center.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).addBox(-12.0F, -16.0F, -12.0F, 24.0F, 24.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.center.getAllParts().forEach(ModelPart::resetPose);
        ModelPart head = this.center.getChild("head");
        ModelPart leg1 = this.center.getChild("leg1");
        ModelPart leg2 = this.center.getChild("leg2");
        ModelPart leg3 = this.center.getChild("leg3");
        ModelPart leg4 = this.center.getChild("leg4");

        head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot = headPitch * ((float)Math.PI / 180F);

        float mult = 0.2F;
        leg1.yRot += ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) / 2);
        leg3.yRot += ((Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount) / 2);
        leg2.yRot += ((Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount) / 2);
        leg4.yRot += ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) / 2);
        if ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) >= 0) {
            leg1.xRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1) * mult;
            leg1.zRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1) * mult;
            leg4.xRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1) * mult;
            leg4.zRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount))) * mult;
        } else {
            leg2.xRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount))) * mult;
            leg2.zRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount))) * mult;
            leg3.xRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount))) * mult;
            leg3.zRot = (Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1) * mult;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
