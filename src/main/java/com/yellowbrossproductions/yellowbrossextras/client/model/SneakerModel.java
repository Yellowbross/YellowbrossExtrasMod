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

public class SneakerModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "sneaker"), "main");
    private final ModelPart head;
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
    private final ModelPart head2;
    private final ModelPart head3;
    private final ModelPart head4;
    private final ModelPart head5;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tnt;

    public SneakerModel(ModelPart root) {
        this.head = root.getChild("head");
        this.sprout = this.head.getChild("sprout");
        this.leaves1 = this.sprout.getChild("leaves1");
        this.leaf1 = this.leaves1.getChild("leaf1");
        this.leaves2 = this.sprout.getChild("leaves2");
        this.leaf2 = this.leaves2.getChild("leaf2");
        this.leaves3 = this.sprout.getChild("leaves3");
        this.leaf3 = this.leaves3.getChild("leaf3");
        this.leaves4 = this.sprout.getChild("leaves4");
        this.leaf4 = this.leaves4.getChild("leaf4");
        this.sprout2 = this.sprout.getChild("sprout2");
        this.head2 = this.head.getChild("head2");
        this.head3 = this.head.getChild("head3");
        this.head4 = this.head.getChild("head4");
        this.head5 = this.head.getChild("head5");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tnt = root.getChild("tnt");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition sprout = head.addOrReplaceChild("sprout", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition leaves1 = sprout.addOrReplaceChild("leaves1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leaf1 = leaves1.addOrReplaceChild("leaf1", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves2 = sprout.addOrReplaceChild("leaves2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition leaf2 = leaves2.addOrReplaceChild("leaf2", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves3 = sprout.addOrReplaceChild("leaves3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

        PartDefinition leaf3 = leaves3.addOrReplaceChild("leaf3", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leaves4 = sprout.addOrReplaceChild("leaves4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leaf4 = leaves4.addOrReplaceChild("leaf4", CubeListBuilder.create().texOffs(28, 20).addBox(-11.5F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition sprout2 = sprout.addOrReplaceChild("sprout2", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head3 = head.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head4 = head.addOrReplaceChild("head4", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition head5 = head.addOrReplaceChild("head5", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, 4.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, 4.0F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 18.0F, -4.0F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, -4.0F));

        PartDefinition tnt = partdefinition.addOrReplaceChild("tnt", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -5.0F, -4.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(-0.25F)), PartPose.offset(-1.5F, 14.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.getAllParts().forEach(ModelPart::resetPose);

        if (entity instanceof AbstractCreeperEntity creeper) {
            this.head2.visible = creeper.getAbsorbedCreepers() >= 1;
            this.head3.visible = creeper.getAbsorbedCreepers() >= 2;
            this.head4.visible = creeper.getAbsorbedCreepers() >= 3;
            this.head5.visible = creeper.getAbsorbedCreepers() >= 4;
        }

        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        ModelPart leaf1 = this.head.getChild("sprout").getChild("leaves1").getChild("leaf1");
        ModelPart leaf2 = this.head.getChild("sprout").getChild("leaves2").getChild("leaf2");
        ModelPart leaf3 = this.head.getChild("sprout").getChild("leaves3").getChild("leaf3");
        ModelPart leaf4 = this.head.getChild("sprout").getChild("leaves4").getChild("leaf4");
        float f3 = ageInTicks / 60.0F;
        float multiplier = 0.5F;

        leaf1.zRot += (Mth.cos(f3 * 5)) * multiplier;
        leaf2.zRot += (Mth.cos(f3 * 5)) * multiplier;
        leaf3.zRot += (Mth.cos(f3 * 5)) * multiplier;
        leaf4.zRot += (Mth.cos(f3 * 5)) * multiplier;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tnt.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
