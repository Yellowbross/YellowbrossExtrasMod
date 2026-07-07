package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DeadlyArrowEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SniperRifleEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class DeadlyArrowModel<T extends DeadlyArrowEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "deadly_arrow"), "main");
    private final ModelPart arrow;
    private final ModelPart back;
    private final ModelPart cross_1;
    private final ModelPart cross_2;

    public DeadlyArrowModel(ModelPart root) {
        this.arrow = root.getChild("arrow");
        this.back = this.arrow.getChild("back");
        this.cross_1 = this.arrow.getChild("cross_1");
        this.cross_2 = this.arrow.getChild("cross_2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition arrow = partdefinition.addOrReplaceChild("arrow", CubeListBuilder.create(), PartPose.offset(0.0F, 20.5F, 0.0F));

        PartDefinition back = arrow.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -2.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.75F, 7.0F, -2.3562F, 1.5708F, 0.0F));

        PartDefinition cross_1 = arrow.addOrReplaceChild("cross_1", CubeListBuilder.create().texOffs(0, 0).addBox(-28.0F, -2.5F, 0.0F, 32.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -18.0F, -2.3562F, 1.5708F, 0.0F));

        PartDefinition cross_2 = arrow.addOrReplaceChild("cross_2", CubeListBuilder.create().texOffs(0, 0).addBox(-28.0F, -2.5F, 0.0F, 32.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -18.0F, 0.0F, 1.5708F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(DeadlyArrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        arrow.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
