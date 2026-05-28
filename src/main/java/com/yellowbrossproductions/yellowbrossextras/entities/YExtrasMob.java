package com.yellowbrossproductions.yellowbrossextras.entities;

import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class YExtrasMob extends PathfinderMob implements ICanBeAnimated, YextrasEntity {
    protected static final EntityDataAccessor<String> ANIMATION_STATE = SynchedEntityData.defineId(YExtrasMob.class, EntityDataSerializers.STRING);

    protected YExtrasMob(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_STATE, "none");
    }

    public String getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    public void setAnimationState(String input) {
        this.entityData.set(ANIMATION_STATE, input);
    }

    public void updateAnimations() {}

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ANIMATION_STATE.equals(p_21104_)) {
            if (this.level.isClientSide) {
                this.updateAnimations();
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this instanceof Enemy;
    }
}
