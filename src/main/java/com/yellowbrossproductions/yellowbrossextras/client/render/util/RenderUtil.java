package com.yellowbrossproductions.yellowbrossextras.client.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {
    public static void translatePoseStackToPart(HierarchicalModel<? extends Entity> model, PoseStack poseStack, String name) {
        model.getAnyDescendantWithName(name).ifPresent(part -> {
            List<ModelPart> parts = new ArrayList<>();
            model.root().getAllParts().forEach(part1 -> getPart(model, parts, part1, name));
            for (int i = parts.size() - 1; i >= 0; --i) {
                ModelPart part1 = parts.get(i);
                part1.translateAndRotate(poseStack);
            }
            part.translateAndRotate(poseStack);
        });
    }

    private static void getPart(HierarchicalModel<? extends Entity> model, List<ModelPart> parts, ModelPart part, String name) {
        if (part.hasChild(name)) {
            parts.add(part);
            model.root().getAllParts().forEach(part1 -> {
                for (Map.Entry<String, ModelPart> entry : part1.children.entrySet()) {
                    if (entry.getValue() == part) {
                        getPart(model, parts, part1, entry.getKey());
                        break;
                    }
                }
            });
        }
    }

    public static float getDefaultBodyRot(Entity entity, float partialTick) {
        float bodyRot;
        float headRot;

        if (entity instanceof LivingEntity living) {
            bodyRot = Mth.rotLerp(partialTick, living.yBodyRotO, living.yBodyRot);
            headRot = Mth.rotLerp(partialTick, living.yHeadRotO, living.yHeadRot);
        } else {
            bodyRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
            headRot = bodyRot;
        }

        if (entity.isPassenger() && entity.getVehicle() instanceof LivingEntity vehicle && entity.getVehicle().shouldRiderSit()) {
            bodyRot = Mth.rotLerp(partialTick, vehicle.yBodyRotO, vehicle.yBodyRot);
            float delta = Mth.wrapDegrees(headRot - bodyRot);
            delta = Mth.clamp(delta, -85, 85);
            bodyRot = headRot - delta;
            if (delta * delta > 2500) bodyRot += delta * 0.2F;
        }

        return bodyRot;
    }
}
