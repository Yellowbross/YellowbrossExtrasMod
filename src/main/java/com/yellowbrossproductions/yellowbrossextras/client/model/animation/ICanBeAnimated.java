package com.yellowbrossproductions.yellowbrossextras.client.model.animation;

import net.minecraft.world.entity.AnimationState;

public interface ICanBeAnimated {
    AnimationState getAnimationState(String input);

    default float getAnimationSpeed(String input) {
        return 1f;
    }

    default float getAnimationSpeed() {
        return 1f;
    }

    default int maxSwingTime() {
        return 1;
    }
}
