package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.effect.CustomMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YEEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, YellowbrossExtras.MOD_ID);

    public static final RegistryObject<MobEffect> KNOCKED_OUT = EFFECTS.register("knocked_out", () -> new CustomMobEffect(MobEffectCategory.HARMFUL, 3484199, false).addAttributeModifier(Attributes.MOVEMENT_SPEED, "617D7064-EBBA-486E-3ABE-C2C23A6DD7A9", -1.0D, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> HUNTED = EFFECTS.register("hunted", () -> new CustomMobEffect(MobEffectCategory.HARMFUL, 3484199, false));
    public static final RegistryObject<MobEffect> SUPER_DUPER_POISON = EFFECTS.register("super_duper_poison", () -> new CustomMobEffect(MobEffectCategory.HARMFUL, 0x77B200, true));
}
