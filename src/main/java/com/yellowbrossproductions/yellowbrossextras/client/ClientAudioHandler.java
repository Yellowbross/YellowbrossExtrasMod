package com.yellowbrossproductions.yellowbrossextras.client;

import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.util.BossMusic;
import net.minecraft.client.Minecraft;

public class ClientAudioHandler {
    private static BossMusic activeMusicInstance = null;

    public static void startBossMusic(YExtrasMob boss) {
        Minecraft mc = Minecraft.getInstance();

        if (activeMusicInstance != null && mc.getSoundManager().isActive(activeMusicInstance) && activeMusicInstance.getMusicType() == boss.getMusicType()) {
            return;
        }

        if (boss.getBossMusic() == null) return;

        activeMusicInstance = new BossMusic(boss, boss.getBossMusic(), boss.getMusicType());
        mc.getSoundManager().play(activeMusicInstance);
    }
}
