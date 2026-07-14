package com.yellowbrossproductions.yellowbrossextras.client.model.defender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SniperRifle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SniperRifleModel<T extends SniperRifle> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(YellowbrossExtras.MOD_ID, "sniper_rifle"), "main");
    private final ModelPart sniper_rifle;
    private final ModelPart sniper_rifle_rotated;

    public SniperRifleModel(ModelPart root) {
        this.sniper_rifle = root.getChild("sniper_rifle");
        this.sniper_rifle_rotated = this.sniper_rifle.getChild("sniper_rifle_rotated");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition sniper_rifle = partdefinition.addOrReplaceChild("sniper_rifle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 21.0F, 12.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition sniper_rifle_rotated = sniper_rifle.addOrReplaceChild("sniper_rifle_rotated", CubeListBuilder.create().texOffs(136, 103).addBox(-7.0F, -5.0F, -1.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(156, 103).addBox(1.0F, -5.0F, -1.0F, 12.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(184, 103).addBox(13.0F, -5.0F, -1.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(176, 108).addBox(-1.0F, -9.0F, -1.5F, 16.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(136, 110).addBox(0.0F, -6.0F, 0.0F, 20.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SniperRifle entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        sniper_rifle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
