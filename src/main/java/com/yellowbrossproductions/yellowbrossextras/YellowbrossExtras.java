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
    public static final CreativeModeTab YELLOWBROSSEXTRAS_GROUP = new YellowbrossExtrasGroup("yellowbrossextrastab");

    public YellowbrossExtras()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        YEItemsAndBlocks.itemInit();
        YEItemsAndBlocks.blockInit();
        YEEntityTypes.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        YEEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        YESoundEvents.SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());

        Config.loadConfig(Config.client_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-client.toml").toString());
        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("yellowbrossextras-common.toml").toString());

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, YellowbrossExtras::registerCommands);
        // modEventBus.addListener(YECapabilities::registerCaps);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        BunnyBlitzCommand.register(dispatcher);
    }

    public static boolean checkYExtrasMonsterSpawnRules(EntityType<? extends YExtrasMob> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_) {
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_) && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
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
            return YEItemsAndBlocks.ICON.get().getDefaultInstance();
        }
    }
}
