package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.block.FrozenLavaBlock;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.item.ItemBase;
import com.yellowbrossproductions.yellowbrossextras.item.MobRemoverItemBase;
import com.yellowbrossproductions.yellowbrossextras.item.TheFingerItemBase;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegisterer {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YellowbrossExtras.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YellowbrossExtras.MOD_ID);

    public static void itemInit() {ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());}
    public static void blockInit() {BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());}

    // Items
    public static final RegistryObject<Item> MOB_REMOVER = ITEMS.register("mob_remover", MobRemoverItemBase::new);
    public static final RegistryObject<Item> THE_FINGER = ITEMS.register("the_finger", TheFingerItemBase::new);

    // Spawn Eggs
    public static final RegistryObject<Item> DEFENDER_SPAWN_EGG = ITEMS.register("defender_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Defender, 6012148, 8115455, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> SENTRY_GUN_SPAWN_EGG = ITEMS.register("sentry_gun_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.SentryGun, 2840946, 8115455, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));

    public static final RegistryObject<Item> SNEAKER_SPAWN_EGG = ITEMS.register("sneaker_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Sneaker, 894731, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> PARACREEPER_SPAWN_EGG = ITEMS.register("paracreeper_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Paracreeper, 894731, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> CRAWLER_SPAWN_EGG = ITEMS.register("crawler_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Crawler, 894731, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> FREAKER_SPAWN_EGG = ITEMS.register("freaker_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Freaker, 894731, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> SPRAYER_SPAWN_EGG = ITEMS.register("sprayer_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Sprayer, 894731, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));

    public static final RegistryObject<Item> VILVGAVER_SPAWN_EGG = ITEMS.register("vilvgaver_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Vilvgaver, 3353642, 7958378, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));

    public static final RegistryObject<Item> CONVERSLIN_SPAWN_EGG = ITEMS.register("converslin_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.Converslin, 4412593, 16777215, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));

    public static final RegistryObject<Item> AMOEBIC_DEVOURER_SPAWN_EGG = ITEMS.register("amoebic_devourer_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.AmoebicDevourer, 6385441, 10266217, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));
    public static final RegistryObject<Item> HYPER_SNOW_GOLEM_SPAWN_EGG = ITEMS.register("hyper_snow_golem_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.HyperSnowGolem, 16777215, 16738816, (new Item.Properties()).tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP)));

    // Others
    public static final RegistryObject<Item> ICON = ITEMS.register("icon", ItemBase::new);
    public static final RegistryObject<Item> SHURIKEN = ITEMS.register("shuriken", ItemBase::new);
    public static final RegistryObject<Item> CONVERSLIN_BULLET = ITEMS.register("converslin_bullet", ItemBase::new);

    // Blocks
    public static final RegistryObject<Block> FROZEN_LAVA = BLOCKS.register("frozen_lava",
            () -> new FrozenLavaBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.0F, 10.0F).sound(SoundType.NETHERRACK).instabreak()));
}
