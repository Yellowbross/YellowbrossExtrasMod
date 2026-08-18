package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class YEDamageSources {
    public static final ResourceKey<DamageType> SAW = register("saw");
    public static final ResourceKey<DamageType> EXCALIBUR = register("excalibur");
    public static final ResourceKey<DamageType> VILVGAVER = register("vilvgaver");

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(YellowbrossExtras.MOD_ID, name));
    }
}
