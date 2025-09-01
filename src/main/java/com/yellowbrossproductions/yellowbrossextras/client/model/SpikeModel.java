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
import net.minecraft.world.entity.Entity;

public class SpikeModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "spike"), "main");
    private final ModelPart spikes;

    public SpikeModel(ModelPart root) {
        this.spikes = root.getChild("spikes");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition spikes = partdefinition.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition spike = spikes.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, -8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition spike2 = spike.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition spike3 = spikes.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, -8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition spike4 = spike3.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition spike5 = spikes.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition spike6 = spike5.addOrReplaceChild("spike6", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition spike7 = spikes.addOrReplaceChild("spike7", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition spike8 = spike7.addOrReplaceChild("spike8", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, 0.0F, 12.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition spike9 = spikes.addOrReplaceChild("spike9", CubeListBuilder.create().texOffs(24, 0).addBox(-10.0F, -26.0F, 0.0F, 20.0F, 26.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition spike10 = spike9.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(24, 0).addBox(-10.0F, -26.0F, 0.0F, 20.0F, 26.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
