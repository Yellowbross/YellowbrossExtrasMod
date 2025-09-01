package com.yellowbrossproductions.yellowbrossextras.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class KnockedOutEffect extends MobEffect {

    public static final UUID MODIFIER_UUID = UUID.fromString("617D7064-EBBA-486E-3ABE-C2C23A6DD7A9");

    public KnockedOutEffect(MobEffectCategory type, int liquidColor) {
        super(type, liquidColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, KnockedOutEffect.MODIFIER_UUID.toString(), -1.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    }

}
