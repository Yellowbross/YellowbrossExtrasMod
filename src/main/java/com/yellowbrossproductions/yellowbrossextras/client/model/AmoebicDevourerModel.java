package com.yellowbrossproductions.yellowbrossextras.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class AmoebicDevourerModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "amoebic_devourer"), "main");
    private final ModelPart root;
    private final ModelPart lowest_body;
    private final ModelPart lower_body;
    private final ModelPart body;
    private final ModelPart belly;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart right_hairbun;
    private final ModelPart right_hang;
    private final ModelPart right_hair1;
    private final ModelPart right_hair2;
    private final ModelPart right_hair3;
    private final ModelPart left_hairbun;
    private final ModelPart left_hang;
    private final ModelPart left_hair1;
    private final ModelPart left_hair2;
    private final ModelPart left_hair3;
    private final ModelPart right_arm;
    private final ModelPart left_arm;

    public AmoebicDevourerModel(ModelPart root) {
        this.root = root;
        this.lowest_body = root.getChild("lowest_body");
        this.lower_body = this.lowest_body.getChild("lower_body");
        this.body = this.lower_body.getChild("body");
        this.belly = this.body.getChild("belly");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.right_hairbun = this.neck.getChild("right_hairbun");
        this.right_hang = this.right_hairbun.getChild("right_hang");
        this.right_hair1 = this.right_hang.getChild("right_hair1");
        this.right_hair2 = this.right_hair1.getChild("right_hair2");
        this.right_hair3 = this.right_hair1.getChild("right_hair3");
        this.left_hairbun = this.neck.getChild("left_hairbun");
        this.left_hang = this.left_hairbun.getChild("left_hang");
        this.left_hair1 = this.left_hang.getChild("left_hair1");
        this.left_hair2 = this.left_hair1.getChild("left_hair2");
        this.left_hair3 = this.left_hair1.getChild("left_hair3");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lowest_body = partdefinition.addOrReplaceChild("lowest_body", CubeListBuilder.create().texOffs(8, 90).addBox(-15.0F, -8.0F, -15.0F, 30.0F, 8.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition lower_body = lowest_body.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(48, 58).addBox(-10.0F, -16.0F, -10.0F, 20.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = lower_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(100, 98).addBox(-4.0F, -16.0F, -3.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition belly = body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 50).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(42, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition right_hairbun = neck.addOrReplaceChild("right_hairbun", CubeListBuilder.create().texOffs(96, 0).mirror().addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -14.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition right_hang = right_hairbun.addOrReplaceChild("right_hang", CubeListBuilder.create().texOffs(96, 24).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(7.0F, -8.5F, 0.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition right_hair1 = right_hang.addOrReplaceChild("right_hair1", CubeListBuilder.create().texOffs(112, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -0.5F, 0.0F));

        PartDefinition right_hair2 = right_hair1.addOrReplaceChild("right_hair2", CubeListBuilder.create().texOffs(112, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_hair3 = right_hair1.addOrReplaceChild("right_hair3", CubeListBuilder.create().texOffs(96, 24).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_hairbun = neck.addOrReplaceChild("left_hairbun", CubeListBuilder.create().texOffs(96, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -14.0F, 0.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition left_hang = left_hairbun.addOrReplaceChild("left_hang", CubeListBuilder.create().texOffs(96, 24).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(-8.0F, -7.5F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition left_hair1 = left_hang.addOrReplaceChild("left_hair1", CubeListBuilder.create().texOffs(112, 62).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -0.5F, 0.0F));

        PartDefinition left_hair2 = left_hair1.addOrReplaceChild("left_hair2", CubeListBuilder.create().texOffs(112, 62).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition left_hair3 = left_hair1.addOrReplaceChild("left_hair3", CubeListBuilder.create().texOffs(96, 24).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(22, 104).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -14.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(22, 104).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -14.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity instanceof AmoebicDevourerEntity aaron) {
            this.head.yRot += netHeadYaw * ((float)Math.PI / 180F);
            this.head.xRot += (headPitch * ((float)Math.PI / 180F));

            
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lowest_body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
