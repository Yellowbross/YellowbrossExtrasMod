package com.yellowbrossproductions.yellowbrossextras.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class YellowbrossExtrasConfig {
    public static ForgeConfigSpec.BooleanValue cameraShakesAllowed;
    public static ForgeConfigSpec.IntValue vilvgaverTotalFrames;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> aiChangesNotAllowed;

    public static ForgeConfigSpec.BooleanValue defender_distractEnemies;
    public static ForgeConfigSpec.IntValue defender_sentryGun_mitosisTimer;
    public static ForgeConfigSpec.IntValue defender_sentryGun_mitosisCap;
    public static ForgeConfigSpec.IntValue defender_sentryGun_lifetimeCap;

    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_defaultSpeed;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_glowTime;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_delayTime;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_jumpDelay;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_escapeTime;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_lavaFreezeRadius;
    public static ForgeConfigSpec.BooleanValue vilvgaverChallenge_restoreHunger;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> vilvgaverChallenge_blockInstabreaks;
    public static ForgeConfigSpec.DoubleValue vilvgaver_ambienceVolume;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_snowballPush;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_pushLimit;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_speedBuff;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> vilvgaverChallenge_mustNotSpawnNear;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_netherSpeed;
    public static ForgeConfigSpec.IntValue vilvgaverChallenge_randomSpawnChance;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> vilvgaverChallenge_blocklist;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_moblistRadius;
    public static ForgeConfigSpec.DoubleValue vilvgaverChallenge_blocklistRadius;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> vilvgaverChallenge_smashBlacklist;
    public static ForgeConfigSpec.BooleanValue vilvgaverChallenge_fallDamageAllowed;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> creeperInfection_turnTo;

    public static ForgeConfigSpec.BooleanValue oryctolin_victoryDance;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> bunnyBlitz_raiders;

    public static void init(ForgeConfigSpec.Builder common, ForgeConfigSpec.Builder client) {
        client.push("Client Settings");

        cameraShakesAllowed = client
                .comment("Setting this to false will disable camera shakes.")
                .define("cameraShakesAllowed", true);

        vilvgaverTotalFrames = client
                .comment(" ")
                .comment("Currently, while Vilvgaver can be animated, you must manually set the total frames of his animation yourself if you wish to use a resource pack.",
                        "Default = 1")
                .defineInRange("vilvgaverTotalFrames", 1, 1, Integer.MAX_VALUE);

        client.pop();

        common.push("Settings");

        aiChangesNotAllowed = common
                .comment(" ")
                .comment("Mobs put in this list cannot have their AI modified by this mod. Use this to manually fix otherwise unresolved compatibility issues.",
                        "Format must be like 'examplemod:entity'. Example: \"minecraft:zombie\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Please note that if you do find an incompatibility, you should still report it to the mod developers.",
                        "Requires game restart")
                .defineList("aiChangesNotAllowed", Arrays.asList(
                        "blue_skies:arachnarch"
                ), String.class::isInstance);

        common.push("Defender");

        defender_distractEnemies = common
                .comment(" ")
                .comment("Determines if mobs should stop to stare at Defender's ultimate attacks.",
                        "Default = true")
                .define("defender_distractEnemies", true);

        defender_sentryGun_mitosisTimer = common
                .comment(" ")
                .comment("Determines the timer, in seconds, which Defender's Sentry Guns (phase 2) can stay alive before they multiply.",
                        "Default = 20")
                .defineInRange("defender_sentryGun_mitosisTimer", 20, 0, Integer.MAX_VALUE);

        defender_sentryGun_mitosisCap = common
                .comment(" ")
                .comment("Determines how many Sentry Guns can be near each other before they stop multiplying. Don't get carried away.",
                        "Default = 25")
                .defineInRange("defender_sentryGun_mitosisCap", 25, 1, Integer.MAX_VALUE);

        defender_sentryGun_lifetimeCap = common
                .comment(" ")
                .comment("Determines how long Sentry Guns can live beyond their mitosis cap for before instantly killing themselves.",
                        "Set to 0 to disable",
                        "Default = 35")
                .defineInRange("defender_sentryGun_lifetimeCap", 35, 0, Integer.MAX_VALUE);

        common.pop();

        common.push("Creeper Infection");

        creeperInfection_turnTo = common
                .comment(" ")
                .comment("Creeper Infection custom transformations.",
                        "Format MUST be the entity name, followed by the creeper you want the mob to turn into. Example: \"minecraft:zombie,yellowbrossextras:freaker\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Requires game restart")
                .defineList("creeperInfection_turnTo", Arrays.asList(
                ), String.class::isInstance);

        common.pop();

        common.push("Vilvgaver");

        vilvgaverChallenge_defaultSpeed = common
                .comment(" ")
                .comment("The modified movement speed multiplier that Vilvgavers in the Vilvgaver Challenge will move at",
                        "Default = 0.6")
                .defineInRange("vilvgaverChallenge_defaultSpeed", 0.6, 0, Double.MAX_VALUE);

        vilvgaverChallenge_glowTime = common
                .comment(" ")
                .comment("The number of seconds that Vilvgavers in the Vilvgaver Challenge have the glowing effect for",
                        "Default = 30")
                .defineInRange("vilvgaverChallenge_glowTime", 30, 1, Integer.MAX_VALUE);

        vilvgaverChallenge_delayTime = common
                .comment(" ")
                .comment("The delay in seconds that Vilvgavers in the Vilvgaver Challenge have to wait before acting",
                        "Default = 10")
                .defineInRange("vilvgaverChallenge_delayTime", 10, 1, Integer.MAX_VALUE);

        vilvgaverChallenge_jumpDelay = common
                .comment(" ")
                .comment("The delay in seconds before jumping for Vilvgavers in the Vilvgaver Challenge",
                        "Default = 1")
                .defineInRange("vilvgaverChallenge_jumpDelay", 1, 0, Integer.MAX_VALUE);

        vilvgaverChallenge_escapeTime = common
                .comment(" ")
                .comment("The breathing time in seconds the player has after escaping a Vilvgaver in the Vilvgaver Challenge",
                        "Default = 60")
                .defineInRange("vilvgaverChallenge_escapeTime", 60, 1, Integer.MAX_VALUE);

        vilvgaverChallenge_lavaFreezeRadius = common
                .comment(" ")
                .comment("The radius in which lava in the Nether will freeze when walked over by the player in the Vilvgaver Challenge",
                        "Default = 3.0")
                .defineInRange("vilvgaverChallenge_lavaFreezeRadius", 3.0, 0, Double.MAX_VALUE);

        vilvgaverChallenge_restoreHunger = common
                .comment(" ")
                .comment("Determines if the player's hunger bar will remain always full in the Vilvgaver Challenge",
                        "Default = true")
                .define("vilvgaverChallenge_restoreHunger", true);

        vilvgaverChallenge_blockInstabreaks = common
                .comment(" ")
                .comment("Blocks put in this list are ones the player can instabreak in the Vilvgaver Challenge.",
                        "Mainly used to make the Nether section of the Vilvgaver Challenge possible",
                        "Format must be like 'examplemod:block'. Example: \"minecraft:netherrack\"")
                .defineList("vilvgaverChallenge_blockInstabreaks", Arrays.asList(
                        "minecraft:netherrack",
                        "minecraft:magma_block",
                        "minecraft:nether_gold_ore",
                        "minecraft:nether_quartz_ore",
                        "minecraft:basalt",
                        "minecraft:blackstone",
                        "minecraft:warped_nylium",
                        "minecraft:crimson_nylium",
                        "minecraft:nether_wart_block",
                        "minecraft:warped_wart_block",
                        "minecraft:soul_sand",
                        "minecraft:soul_soil",
                        "yellowbrossextras:frozen_lava"
                ), String.class::isInstance);

        vilvgaver_ambienceVolume = common
                .comment(" ")
                .comment("The volume of Vilvgaver's ambient noise",
                        "Default = 2.5")
                .defineInRange("vilvgaver_ambienceVolume", 2.5, 0.1, Double.MAX_VALUE);

        vilvgaverChallenge_snowballPush = common
                .comment(" ")
                .comment("How much Vilvgaver should be pushed back by each projectile in the Vilvgaver Challenge",
                        "Be careful with how you set this factor; this can make the challenge impossible or way too easy if used poorly.",
                        "For reference, 1 means that Vilvgaver would have to take 2 seconds to recover",
                        "Default = 1")
                .defineInRange("vilvgaverChallenge_snowballPush", 1, 0.0, Double.MAX_VALUE);

        vilvgaverChallenge_pushLimit = common
                .comment(" ")
                .comment("The limit on how much projectiles can push Vilvgaver back",
                        "Be careful with how you set this factor; this can make the challenge impossible or way too easy if used poorly.",
                        "For reference, 2 is already a pretty long limit; Vilvgaver would have to take 4 seconds to recover",
                        "Default = 2")
                .defineInRange("vilvgaverChallenge_pushLimit", 2, 0.0, Double.MAX_VALUE);

        vilvgaverChallenge_speedBuff = common
                .comment(" ")
                .comment("The amplifier for the Player's own movement speed buff in the Vilvgaver Challenge",
                        "Play around with this value and Vilvgaver's own speed multiplier if you want to make the challenge even harder.",
                        "Default = -1")
                .defineInRange("vilvgaverChallenge_speedBuff", -1, -1, 128);

        vilvgaverChallenge_netherSpeed = common
                .comment(" ")
                .comment("If this value is over 0, Vilvgavers in the Vilvgaver Challenge will move at a different speed in the Nether.",
                        "An alternative to the idea of projectiles pushing Vilvgaver back, which might have been too specific of a weakness.",
                        "Default = 0")
                .defineInRange("vilvgaverChallenge_netherSpeed", 0, 0, Double.MAX_VALUE);

        vilvgaverChallenge_randomSpawnChance = common
                .comment(" ")
                .comment("In addition to the breathing time, Vilvgaver by default has only a random chance of spawning every tick to build even more tension.",
                        "Default = 16")
                .defineInRange("vilvgaverChallenge_randomSpawnChance", 16, 0, Integer.MAX_VALUE);

        common.push("Blacklist and Radius");

        vilvgaverChallenge_mustNotSpawnNear = common
                .comment(" ")
                .comment("Mobs that during the challenge, Vilvgaver cannot spawn when the Player is near.",
                        "Mainly used to make some parts of progression less tedious, such as getting Blaze Rods",
                        "However, it can also be used to stop Vilvgaver from messing with boss fights if needed",
                        "Format must be like 'examplemod:entity'. Example: \"minecraft:zombie\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Requires game restart")
                .defineList("vilvgaverChallenge_mustNotSpawnNear", Arrays.asList(
                        "minecraft:blaze"
                ), String.class::isInstance);

        vilvgaverChallenge_blocklist = common
                .comment(" ")
                .comment("Blocks that during the challenge, Vilvgaver cannot spawn when the Player is near.",
                        "Use this to create free passes for parts of the game that'd be way too annoying in the challenge otherwise",
                        "Format must be like 'examplemod:entity'. Example: \"minecraft:grass\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Requires game restart")
                .defineList("vilvgaverChallenge_blocklist", Arrays.asList(
                        "minecraft:obsidian",
                        "minecraft:nether_bricks",
                        "minecraft:warped_nylium"
                ), String.class::isInstance);

        vilvgaverChallenge_smashBlacklist = common
                .comment(" ")
                .comment("Blocks that Vilvgaver cannot smash through.",
                        "Format must be like 'examplemod:entity'. Example: \"minecraft:grass\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Requires game restart")
                .defineList("vilvgaverChallenge_smashBlacklist", Arrays.asList(
                        "minecraft:bedrock"
                ), String.class::isInstance);


        vilvgaverChallenge_moblistRadius = common
                .comment(" ")
                .comment("Radius of the Vilvgaver Challenge's Mob Blacklist",
                        "Default = 30.0")
                .defineInRange("vilvgaverChallenge_moblistRadius", 30.0, 1.0, Double.MAX_VALUE);

        vilvgaverChallenge_blocklistRadius = common
                .comment(" ")
                .comment("Radius of the Vilvgaver Challenge's Block Blacklist",
                        "Default = 10.0")
                .defineInRange("vilvgaverChallenge_blocklistRadius", 10.0, 1.0, Double.MAX_VALUE);

        vilvgaverChallenge_fallDamageAllowed = common
                .comment(" ")
                .comment("Toggles fall damage cap in the Vilvgaver Challenge",
                        "Default = true")
                .define("vilvgaverChallenge_fallDamageAllowed", true);

        common.pop();

        common.pop();

        common.push("Oryctolins");

        oryctolin_victoryDance = common
                .comment(" ")
                .comment("Determines if Oryctolins should celebrate their victory against an Illager Raid by dancing",
                        "Do note this feature chews up a lot of performance the more Oryctolins there are onscreen; not recommended for slower computers",
                        "Default = false")
                .define("oryctolin_victoryDance", false);

        bunnyBlitz_raiders = common
                .comment(" ")
                .comment("Mobs that will spawn in the Bunny Blitz.",
                        "Format MUST be the entity name, followed by a list of integers. Example: \"minecraft:zombie,1,2,3,4,5,6,7,8,9,10\"",
                        "Use the /summon command to scroll through and find the IDs for mobs you want",
                        "Requires game restart")
                .defineList("bunnyBlitz_raiders", Arrays.asList(
                        "yellowbrossextras:converslin,1,1,1,2,2,2,3,3,4,5"
                ), String.class::isInstance);

        common.pop();

        common.pop();
    }
}
