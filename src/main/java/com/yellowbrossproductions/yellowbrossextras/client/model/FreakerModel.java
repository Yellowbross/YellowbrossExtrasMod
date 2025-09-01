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

import java.util.Random;

public class FreakerModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "freaker"), "main");
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart center;
    private final ModelPart neck1;
    private final Random random = new Random();

    public FreakerModel(ModelPart root) {
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.center = root.getChild("center");
        this.neck1 = root.getChild("neck1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition legrotated1 = leg1.addOrReplaceChild("legrotated1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition knee1 = legrotated1.addOrReplaceChild("knee1", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition knee1_r1 = knee1.addOrReplaceChild("knee1_r1", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition legrotated2 = leg2.addOrReplaceChild("legrotated2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition knee2 = legrotated2.addOrReplaceChild("knee2", CubeListBuilder.create().texOffs(52, 12).addBox(-4.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition legrotated3 = leg3.addOrReplaceChild("legrotated3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition knee3 = legrotated3.addOrReplaceChild("knee3", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        PartDefinition knee3_r1 = knee3.addOrReplaceChild("knee3_r1", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition legrotated4 = leg4.addOrReplaceChild("legrotated4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -3.0F, 30.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition knee4 = legrotated4.addOrReplaceChild("knee4", CubeListBuilder.create(), PartPose.offsetAndRotation(29.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition knee4_r1 = knee4.addOrReplaceChild("knee4_r1", CubeListBuilder.create().texOffs(52, 12).addBox(-2.0F, -4.0F, -4.0F, 30.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition center = partdefinition.addOrReplaceChild("center", CubeListBuilder.create().texOffs(0, 12).addBox(-5.0F, -17.0F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition neck1 = partdefinition.addOrReplaceChild("neck1", CubeListBuilder.create().texOffs(0, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, -0.9599F, 0.0F, 0.0F));

        PartDefinition neck2 = neck1.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 25.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition neck3 = neck2.addOrReplaceChild("neck3", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 25.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition neck4 = neck3.addOrReplaceChild("neck4", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 25.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition neck5 = neck4.addOrReplaceChild("neck5", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 25.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -19.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition neck6 = neck5.addOrReplaceChild("neck6", CubeListBuilder.create().texOffs(40, 28).addBox(-5.0F, -20.0F, -5.0F, 10.0F, 25.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -19.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition head = neck6.addOrReplaceChild("head", CubeListBuilder.create().texOffs(48, 83).addBox(-15.0F, -20.0F, -5.0F, 20.0F, 25.0F, 20.0F, new CubeDeformation(-2.0F)), PartPose.offset(5.0F, -20.0F, -5.0F));

        PartDefinition randompiece8 = neck5.addOrReplaceChild("randompiece8", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -8.0F, -5.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition randompiece6 = neck4.addOrReplaceChild("randompiece6", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -8.0F, 4.0F, -0.2618F, 1.5708F, 0.0F));

        PartDefinition randompiece9 = neck4.addOrReplaceChild("randompiece9", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -18.0F, 6.0F, -0.2618F, 3.1416F, 0.0F));

        PartDefinition randompiece5 = neck3.addOrReplaceChild("randompiece5", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -10.0F, -3.0F, -0.2618F, -1.5708F, 0.0F));

        PartDefinition randompiece7 = neck3.addOrReplaceChild("randompiece7", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -12.0F, -1.0F, -0.2618F, 1.5708F, 0.0F));

        PartDefinition randompiece1 = neck2.addOrReplaceChild("randompiece1", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, -3.0F, -0.2618F, -1.5708F, 0.0F));

        PartDefinition randompiece2 = neck2.addOrReplaceChild("randompiece2", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -10.0F, 2.0F, -0.2618F, 1.5708F, 0.0F));

        PartDefinition randompiece3 = neck2.addOrReplaceChild("randompiece3", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -17.0F, -5.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition randompiece4 = neck2.addOrReplaceChild("randompiece4", CubeListBuilder.create().texOffs(0, 118).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -13.0F, 6.0F, -0.2618F, 3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.neck1.getAllParts().forEach(ModelPart::resetPose);
        this.leg1.resetPose();
        this.leg2.resetPose();
        this.leg3.resetPose();
        this.leg4.resetPose();

        float twitchamount = 150;

        ModelPart neck2 = this.neck1.getChild("neck2");
        ModelPart neck3 = this.neck1.getChild("neck2").getChild("neck3");
        ModelPart neck4 = this.neck1.getChild("neck2").getChild("neck3").getChild("neck4");
        ModelPart neck5 = this.neck1.getChild("neck2").getChild("neck3").getChild("neck4").getChild("neck5");
        ModelPart neck6 = this.neck1.getChild("neck2").getChild("neck3").getChild("neck4").getChild("neck5").getChild("neck6");
        ModelPart head = this.neck1.getChild("neck2").getChild("neck3").getChild("neck4").getChild("neck5").getChild("neck6").getChild("head");

        this.neck1.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck2.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck3.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck4.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck5.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck6.xRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        this.neck1.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck2.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck3.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck4.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck5.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck6.yRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        this.neck1.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck2.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck3.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck4.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck5.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;
        neck6.zRot += (-0.5F + this.random.nextFloat()) / twitchamount;

        head.zRot -= netHeadYaw * ((float)Math.PI / 180F);
        head.xRot += headPitch * ((float)Math.PI / 180F);

        this.leg1.yRot = ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) / 2) + -0.7854F;
        this.leg2.yRot = ((Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount) / 2) + 0.7854F;
        this.leg3.yRot = ((Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount) / 2) + -2.3562F;
        this.leg4.yRot = ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) / 2) + 2.3562F;
        if ((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) >= 0) {
            this.leg1.xRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1;
            this.leg1.zRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1;
            this.leg4.xRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1;
            this.leg4.zRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount));
        } else {
            this.leg2.xRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount));
            this.leg2.zRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount)) * -1;
            this.leg3.xRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount));
            this.leg3.zRot = Math.abs((Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount));
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        center.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        neck1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
