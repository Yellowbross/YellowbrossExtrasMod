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

public class ParacreeperModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "paracreeper"), "main");
    private final ModelPart head;
    private final ModelPart sprout;

    public ParacreeperModel(ModelPart root) {
        this.head = root.getChild("head");
        this.sprout = root.getChild("sprout");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition leg1 = head.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 1.0F, 0.7854F, -0.7854F, 0.0F));

        PartDefinition leg2 = head.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 1.0F, 0.7854F, 0.7854F, 0.0F));

        PartDefinition leg3 = head.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, -1.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition leg4 = head.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, -1.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition sprout = partdefinition.addOrReplaceChild("sprout", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition leaves1 = sprout.addOrReplaceChild("leaves1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leaf1 = leaves1.addOrReplaceChild("leaf1", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leaves2 = sprout.addOrReplaceChild("leaves2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.25F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition leaf2 = leaves2.addOrReplaceChild("leaf2", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leaves3 = sprout.addOrReplaceChild("leaves3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition leaf3 = leaves3.addOrReplaceChild("leaf3", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leaves4 = sprout.addOrReplaceChild("leaves4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.25F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leaf4 = leaves4.addOrReplaceChild("leaf4", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition sprout2 = sprout.addOrReplaceChild("sprout2", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.sprout.yRot = (ageInTicks * 45) * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        sprout.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
