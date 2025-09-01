package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.HumanoidArm;

public interface CustomHeadedModel {
	void translateToHead(PoseStack stack);
}
