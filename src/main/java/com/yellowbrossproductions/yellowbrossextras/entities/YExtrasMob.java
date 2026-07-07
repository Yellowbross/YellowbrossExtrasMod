package com.yellowbrossproductions.yellowbrossextras.entities;

import com.yellowbrossproductions.yellowbrossextras.client.ClientAudioHandler;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class YExtrasMob extends PathfinderMob implements ICanBeAnimated, YextrasEntity {
    protected static final EntityDataAccessor<String> ANIMATION_STATE = SynchedEntityData.defineId(YExtrasMob.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> MUSIC_TO_PLAY = SynchedEntityData.defineId(YExtrasMob.class, EntityDataSerializers.INT);

    protected YExtrasMob(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_STATE, "none");
        this.entityData.define(MUSIC_TO_PLAY, 0);
    }

    public int getMusicType() {
        return this.entityData.get(MUSIC_TO_PLAY);
    }

    public void setMusicToPlay(int input) {
        this.entityData.set(MUSIC_TO_PLAY, input);
    }

    public String getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    public void setAnimationState(String input) {
        this.entityData.set(ANIMATION_STATE, input);
    }

    public void updateAnimations() {}

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (ANIMATION_STATE.equals(pKey)) {
            if (this.level.isClientSide) {
                this.updateAnimations();
            }
        }

        super.onSyncedDataUpdated(pKey);

        if (this.level.isClientSide && MUSIC_TO_PLAY.equals(pKey)) {
            if (this.getMusicType() > 0) {
                ClientAudioHandler.startBossMusic(this);
            }
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this instanceof Enemy;
    }

    @Nullable
    public SoundEvent getBossMusic() {
        return null;
    }

    public float getMusicVolume() {
        return YellowbrossExtrasConfig.bossMusicVolume.get().floatValue();
    }

    public boolean canPlayMusic() {
        return false;
    };
}
