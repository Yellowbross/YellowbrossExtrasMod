package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BossMusic extends AbstractTickableSoundInstance {
    private final YExtrasMob boss;
    private final int musicType;

    private boolean shouldPlay;

    public BossMusic(YExtrasMob boss, SoundEvent sound, int musicType) {
        super(sound, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.boss = boss;
        this.musicType = musicType;
        this.delay = 0;
        x = boss.getX();
        y = boss.getY();
        z = boss.getZ();
        this.volume = boss.getMusicVolume();
        if (musicType == 2) this.looping = true;
        this.relative = true;

        shouldPlay = true;
    }

    public int getMusicType() {
        return this.musicType;
    }

    @Override
    public void tick() {
        if (this.boss == null || this.boss.isRemoved()) {
            this.stop();
            return;
        }

        shouldPlay = boss.canPlayMusic() && boss.getMusicType() == musicType;

        if (shouldPlay) {
            x = boss.getX();
            y = boss.getY();
            z = boss.getZ();
        } else {
            volume -= 0.01f;
            if (boss.getMusicType() > 0) volume -= 0.05f;
        }

        if (volume < 0.025) stop();
    }
}
