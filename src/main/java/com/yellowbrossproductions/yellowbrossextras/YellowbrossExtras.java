package com.yellowbrossproductions.yellowbrossextras;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import com.yellowbrossproductions.yellowbrossextras.config.Config;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.init.*;
import com.yellowbrossproductions.yellowbrossextras.util.*;
import com.yellowbrossproductions.yellowbrossextras.world.commands.BunnyBlitzCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
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
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> YELLOWBROSSEXTRAS_GROUP = CREATIVE_MODE_TABS.register("yellowbrossextrastab", () -> CreativeModeTab.builder()
            .icon(() -> YEItemsAndBlocks.ICON.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.yellowbrossextrastab"))
            .displayItems((parameters, output) -> {
                // Items
                output.accept(YEItemsAndBlocks.MOB_REMOVER.get());
                output.accept(YEItemsAndBlocks.THE_FINGER.get());

                // Spawn Eggs
                output.accept(YEItemsAndBlocks.DEFENDER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.SENTRY_GUN_SPAWN_EGG.get());

                output.accept(YEItemsAndBlocks.SNEAKER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.PARACREEPER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.CRAWLER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.FREAKER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.SPRAYER_SPAWN_EGG.get());

                output.accept(YEItemsAndBlocks.VILVGAVER_SPAWN_EGG.get());

                output.accept(YEItemsAndBlocks.CONVERSLIN_SPAWN_EGG.get());

                output.accept(YEItemsAndBlocks.AMOEBIC_DEVOURER_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.HYPER_SNOW_GOLEM_SPAWN_EGG.get());
                output.accept(YEItemsAndBlocks.AIMBOT_SPAWN_EGG.get());

                output.accept(YEItemsAndBlocks.PVE_BLOCK_ITEM.get());
            })
            .build());

    public YellowbrossExtras()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        YEItemsAndBlocks.itemInit();
        YEItemsAndBlocks.blockInit();
        YEEntityTypes.ENTITY_TYPES.register(modEventBus);
        YEEffects.EFFECTS.register(modEventBus);
        YEParticleTypes.PARTICLE_TYPES.register(modEventBus);
        YESoundEvents.SOUND_EVENTS.register(modEventBus);

        Config.loadConfig(Config.client_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-client.toml").toString());
        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-common.toml").toString());

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, YellowbrossExtras::registerCommands);
        // modEventBus.addListener(YECapabilities::registerCaps);

        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BunnyBlitzCommand.register(dispatcher);
    }

    public static boolean checkYExtrasMonsterSpawnRules(EntityType<? extends YExtrasMob> entityType, ServerLevelAccessor server, MobSpawnType mobSpawnType, BlockPos pPos, RandomSource pRandom) {
        return server.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(server, pPos, pRandom) && checkMobSpawnRules(entityType, server, mobSpawnType, pPos, pRandom);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }
}
