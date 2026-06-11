package com.yellowbrossproductions.yellowbrossextras.init;

import com.mojang.serialization.Codec;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public final class YEParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, YellowbrossExtras.MOD_ID);

    public static final RegistryObject<SimpleParticleType> SUPERDUPERPOISON_DRIP = PARTICLE_TYPES.register("superduperpoison_drip", () -> new SimpleParticleType(false));

    private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(String pKey, boolean pOverrideLimiter, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        return PARTICLE_TYPES.register(pKey, () -> new ParticleType<>(pOverrideLimiter, pDeserializer) {
            @Override
            public Codec<T> codec() {
                return pCodecFactory.apply(this);
            }
        });
    }
}
