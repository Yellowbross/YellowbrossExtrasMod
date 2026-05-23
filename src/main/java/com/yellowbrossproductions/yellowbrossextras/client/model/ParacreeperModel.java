package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.AbstractCreeperEntity;
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
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart head2;
    private final ModelPart sprout;
    private final ModelPart leaves1;
    private final ModelPart leaf1;
    private final ModelPart leaves2;
    private final ModelPart leaf2;
    private final ModelPart leaves3;
    private final ModelPart leaf3;
    private final ModelPart leaves4;
    private final ModelPart leaf4;
    private final ModelPart sprout2;

    public ParacreeperModel(ModelPart root) {
        this.head = root.getChild("head");
        this.leg1 = this.head.getChild("leg1");
        this.leg2 = this.head.getChild("leg2");
        this.leg3 = this.head.getChild("leg3");
        this.leg4 = this.head.getChild("leg4");
        this.head2 = this.head.getChild("head2");
        this.sprout = root.getChild("sprout");
        this.leaves1 = this.sprout.getChild("leaves1");
        this.leaf1 = this.leaves1.getChild("leaf1");
        this.leaves2 = this.sprout.getChild("leaves2");
        this.leaf2 = this.leaves2.getChild("leaf2");
        this.leaves3 = this.sprout.getChild("leaves3");
        this.leaf3 = this.leaves3.getChild("leaf3");
        this.leaves4 = this.sprout.getChild("leaves4");
        this.leaf4 = this.leaves4.getChild("leaf4");
        this.sprout2 = this.sprout.getChild("sprout2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition leg1 = head.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 1.0F, 0.7854F, -0.7854F, 0.0F));

        PartDefinition leg2 = head.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, 1.0F, 0.7854F, 0.7854F, 0.0F));

        PartDefinition leg3 = head.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, -1.0F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition leg4 = head.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 3.0F, -1.0F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

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
        if (entity instanceof AbstractCreeperEntity creeper) this.head2.visible = creeper.getAbsorbedCreepers() >= 1;

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
