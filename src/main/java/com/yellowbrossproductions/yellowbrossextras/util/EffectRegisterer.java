package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.effect.HuntedEffect;
import com.yellowbrossproductions.yellowbrossextras.effect.KnockedOutEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegisterer {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, YellowbrossExtras.MOD_ID);

    public static final RegistryObject<MobEffect> KNOCKED_OUT = EFFECTS.register("knocked_out", () -> new KnockedOutEffect(MobEffectCategory.HARMFUL, 3484199));
    public static final RegistryObject<MobEffect> HUNTED = EFFECTS.register("hunted", () -> new HuntedEffect(MobEffectCategory.HARMFUL, 3484199));
}
