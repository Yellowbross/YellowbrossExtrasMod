package com.yellowbrossproductions.yellowbrossextras.util;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// borrowed from Meet Your Fight
@OnlyIn(Dist.CLIENT)
public class LoopingSound extends AbstractTickableSoundInstance {
    private final LivingEntity mob;
    private final float volumeMultiplier;

    public LoopingSound(LivingEntity mob, SoundEvent sound, float volume) {
        super(sound, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.mob = mob;
        x = mob.getX();
        y = mob.getY();
        z = mob.getZ();
        looping = true;
        volumeMultiplier = volume;
    }

    @Override
    public float getVolume() {
        return super.getVolume() * volumeMultiplier;
    }

    @Override
    public void tick() {
        if (mob.isAlive() && !mob.isRemoved() && !mob.isSilent()) {
            x = mob.getX();
            y = mob.getY();
            z = mob.getZ();
        } else stop();
    }
}
