package com.yellowbrossproductions.yellowbrossextras.events;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        Random random = new Random();
        if (player != null) {
            if (YellowbrossExtrasConfig.cameraShakeMultiplier.get() > 0) {
                float shakeAmplitude = 0;
                for (CameraShakeEntity cameraShake : player.level.getEntitiesOfClass(CameraShakeEntity.class, player.getBoundingBox().inflate(100))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;

                // event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                // event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                // event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));

                event.setPitch((float) (event.getPitch() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
            }
        }
    }
}
