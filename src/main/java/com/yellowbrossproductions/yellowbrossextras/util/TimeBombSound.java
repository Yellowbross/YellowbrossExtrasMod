package com.yellowbrossproductions.yellowbrossextras.util;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// borrowed from Meet Your Fight
@OnlyIn(Dist.CLIENT)
public class TimeBombSound extends AbstractTickableSoundInstance {
    private final Entity mob;
    private final float volumeMultiplier;
    private final int timer;

    public TimeBombSound(Entity mob, SoundEvent sound, float volume, int timer) {
        super(sound, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.mob = mob;
        x = mob.getX();
        y = mob.getY();
        z = mob.getZ();
        looping = true;
        volumeMultiplier = volume;
        this.timer = timer;
    }

    @Override
    public float getVolume() {
        return super.getVolume() * volumeMultiplier;
    }

    @Override
    public void tick() {
        if (mob != null && !mob.isRemoved() && !mob.isSilent()) {
            x = mob.getX();
            y = mob.getY();
            z = mob.getZ();
            this.pitch = 1 + Math.min((float) mob.tickCount / timer, 1.0F);
        } else stop();
    }
}
