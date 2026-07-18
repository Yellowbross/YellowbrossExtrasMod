package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class YEEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, YellowbrossExtras.MOD_ID);

    public static final RegistryObject<MobEffect> KNOCKED_OUT = EFFECTS.register("knocked_out", KnockedOutEffect::new);
    public static final RegistryObject<MobEffect> HUNTED = EFFECTS.register("hunted", HuntedEffect::new);
    public static final RegistryObject<MobEffect> SUPER_DUPER_POISON = EFFECTS.register("super_duper_poison", SuperDuperPoisonEffect::new);
    public static final RegistryObject<MobEffect> FROZEN = EFFECTS.register("frozen", FrozenEffect::new);
}
