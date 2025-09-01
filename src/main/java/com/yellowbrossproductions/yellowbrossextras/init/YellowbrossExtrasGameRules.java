package com.yellowbrossproductions.yellowbrossextras.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class YellowbrossExtrasGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> VILVGAVERCHALLENGE = GameRules.register("vilvgaverChallenge",
            GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.BooleanValue> ENABLE_CREEPER_INFECTION = GameRules.register("enableCreeperInfection",
            GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> CONVERSLIN_CONVERSION = GameRules.register("converslinConversion",
            GameRules.Category.MISC, GameRules.BooleanValue.create(true));
}
