package com.yellowbrossproductions.yellowbrossextras;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import com.yellowbrossproductions.yellowbrossextras.config.Config;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.events.ServerEventHandler;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.util.*;
import com.yellowbrossproductions.yellowbrossextras.world.commands.BunnyBlitzCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.util.Locale;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;
import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(YellowbrossExtras.MOD_ID)
@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID)
public class YellowbrossExtras
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "yellowbrossextras";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ServerProxy PROXY;
    public static final CreativeModeTab YELLOWBROSSEXTRAS_GROUP = new YellowbrossExtrasGroup("yellowbrossextrastab");

    public YellowbrossExtras()
    {
        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        RegistryHandler.itemInit();
        RegistryHandler.blockInit();
        ModEntityTypes.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EffectRegisterer.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        YellowbrossExtrasSoundEvents.SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());

        Config.loadConfig(Config.client_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-client.toml").toString());
        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-common.toml").toString());

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, YellowbrossExtras::registerCommands);

        PROXY.init(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BunnyBlitzCommand.register(dispatcher);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());

        SpawnPlacements.register(ModEntityTypes.Defender.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.SentryGun.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.CreeperBullet.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);

        SpawnPlacements.register(ModEntityTypes.Sneaker.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.Paracreeper.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.Crawler.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.Freaker.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.Sprayer.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);

        SpawnPlacements.register(ModEntityTypes.Vilvgaver.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);

        SpawnPlacements.register(ModEntityTypes.Converslin.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.CarrotMinion.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);

        SpawnPlacements.register(ModEntityTypes.AmoebicDevourer.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.HyperSnowGolem.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.SkeletonSnap.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.Aimbot.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YellowbrossExtras::checkYExtrasMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.StickFigure.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);

        PacketHandler.init();
    }

    public static boolean checkYExtrasMonsterSpawnRules(EntityType<? extends YExtrasMob> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_) {
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_) && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onAttribute(final EntityAttributeCreationEvent event) {
            ModEntityTypes.onAttribute(event);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

    public static class YellowbrossExtrasGroup extends CreativeModeTab {

        public YellowbrossExtrasGroup(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            return RegistryHandler.ICON.get().getDefaultInstance();
        }
    }
}
