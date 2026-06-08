package com.yellowbrossproductions.yellowbrossextras.capability;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.init.YECapabilities;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Code adapted from Mowzie's Mobs
public class SuperDuperPoisonCapability {
    public static ResourceLocation ID = new ResourceLocation(YellowbrossExtras.MOD_ID, "superduperpoison_cap");

    public interface ISuperDuperPoisonCapability extends INBTSerializable<CompoundTag>
    {
        boolean hasSuperDuperPoison();

        void setSuperDuperPoison(boolean input);

        void tick(LivingEntity entity);
    }

    public static class SuperDuperPoisonCapabilityImp implements ISuperDuperPoisonCapability {
        public boolean hasTheEffect;

        @Override
        public void tick(LivingEntity entity) {
            this.setSuperDuperPoison(entity.hasEffect(YEEffects.SUPER_DUPER_POISON.get()));
        }

        @Override
        public boolean hasSuperDuperPoison() {
            return hasTheEffect;
        }

        @Override
        public void setSuperDuperPoison(boolean input) {
            hasTheEffect = input;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putBoolean("hasSuperDuperPoison", hasSuperDuperPoison());
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            setSuperDuperPoison(nbt.getBoolean("hasSuperDuperPoison"));
        }
    }

    public static class SuperDuperPoisonProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
    {
        private final LazyOptional<ISuperDuperPoisonCapability> instance = LazyOptional.of(SuperDuperPoisonCapability.SuperDuperPoisonCapabilityImp::new);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return YECapabilities.SUPERDUPERPOISON_CAPABILITY.orEmpty(cap, instance.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }
    }
}
