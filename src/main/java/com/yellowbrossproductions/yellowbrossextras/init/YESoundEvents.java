package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YESoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, YellowbrossExtras.MOD_ID);

    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_HURT = addSoundsToRegistry("entity.defender.hurt");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_STEP = addSoundsToRegistry("entity.defender.step");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_JUMP = addSoundsToRegistry("entity.defender.jump");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_JUMP2 = addSoundsToRegistry("entity.defender.jump2");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SAW = addSoundsToRegistry("entity.defender.saw");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SAW_START = addSoundsToRegistry("entity.defender.saw_start");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SWORD_WHOOSH = addSoundsToRegistry("entity.defender.sword_whoosh");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SWORD_HIT = addSoundsToRegistry("entity.defender.sword_hit");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_BOOMERANG_HIT = addSoundsToRegistry("entity.defender.boomerang_hit");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SPIN_SHORT = addSoundsToRegistry("entity.defender.spin_short");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CRASH = addSoundsToRegistry("entity.defender.crash");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SPIKE = addSoundsToRegistry("entity.defender.spike");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SHOOT = addSoundsToRegistry("entity.defender.shoot");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SHURIKEN_LAUNCHER_WARN = addSoundsToRegistry("entity.defender.shuriken_launcher_warn");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CHAINSAW_WARN = addSoundsToRegistry("entity.defender.chainsaw_warn");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CHAINSAW_CATCH = addSoundsToRegistry("entity.defender.chainsaw_catch");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CHAINSAW = addSoundsToRegistry("entity.defender.chainsaw");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SMACK = addSoundsToRegistry("entity.defender.smack");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_PHASE_ENDED = addSoundsToRegistry("entity.defender.phase_ended");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_EARTH_RUMBLE = addSoundsToRegistry("entity.defender.earth_rumble");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_QUICK_WHOOSH = addSoundsToRegistry("entity.defender.quick_whoosh");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_QUICK_WHOOSH2 = addSoundsToRegistry("entity.defender.quick_whoosh2");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_DEFEATED = addSoundsToRegistry("entity.defender.defeated");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_GAG1 = addSoundsToRegistry("entity.defender.gag1");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_GAG2 = addSoundsToRegistry("entity.defender.gag2");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_GAG3 = addSoundsToRegistry("entity.defender.gag3");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CARNAGE = addSoundsToRegistry("entity.defender.carnage");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SENTRY_SHOOT = addSoundsToRegistry("entity.defender.sentry_gun.shoot");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SENTRY_HIT = addSoundsToRegistry("entity.defender.sentry_gun.hit");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SENTRY_MITOSIS = addSoundsToRegistry("entity.defender.sentry_gun.mitosis");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SENTRY_POP = addSoundsToRegistry("entity.defender.sentry_gun.pop");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_RATATATABOW_WARN = addSoundsToRegistry("entity.defender.ratatatabow_warn");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CREEPERGUN_SHOOT = addSoundsToRegistry("entity.defender.creepergun_shoot");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CREEPERGUN_HIT = addSoundsToRegistry("entity.defender.creepergun_hit");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_CREEPERBULLET_LAND = addSoundsToRegistry("entity.defender.creeperbullet_land");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_FORCEGUN = addSoundsToRegistry("entity.defender.forcegun");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_FORCEGUN_START = addSoundsToRegistry("entity.defender.forcegun_start");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_POISONDARTS = addSoundsToRegistry("entity.defender.poisondarts");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SNIPE_START = addSoundsToRegistry("entity.defender.snipe_start");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SNIPE_SPIN = addSoundsToRegistry("entity.defender.snipe_spin");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SNIPE_SHOOT = addSoundsToRegistry("entity.defender.snipe_shoot");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_SNIPE_HIT = addSoundsToRegistry("entity.defender.snipe_hit");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_WITHERBAZOOKA_EXPLOSION = addSoundsToRegistry("entity.defender.witherbazooka_explosion");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_WITHERBAZOOKA_LOOP = addSoundsToRegistry("entity.defender.witherbazooka_loop");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_WITHERBAZOOKA_SHOOT = addSoundsToRegistry("entity.defender.witherbazooka_shoot");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_WITHERBAZOOKA_START = addSoundsToRegistry("entity.defender.witherbazooka_start");
    public static final RegistryObject<SoundEvent> ENTITY_DEFENDER_WITHERBAZOOKA_END = addSoundsToRegistry("entity.defender.witherbazooka_end");

    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_ATTACK = addSoundsToRegistry("entity.vilvgaver.attack");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_LOOP = addSoundsToRegistry("entity.vilvgaver.loop");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_SAY = addSoundsToRegistry("entity.vilvgaver.say");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_JUMP = addSoundsToRegistry("entity.vilvgaver.jump");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_RESPAWN = addSoundsToRegistry("entity.vilvgaver.respawn");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_WARN = addSoundsToRegistry("entity.vilvgaver.warn");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_CRASH_CLOSE = addSoundsToRegistry("entity.vilvgaver.crash_close");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_CRASH_MEDIUM = addSoundsToRegistry("entity.vilvgaver.crash_medium");
    public static final RegistryObject<SoundEvent> ENTITY_VILVGAVER_CRASH_DISTANT = addSoundsToRegistry("entity.vilvgaver.crash_distant");

    public static final RegistryObject<SoundEvent> ENTITY_CONVERSLIN_STEP = addSoundsToRegistry("entity.converslin.step");
    public static final RegistryObject<SoundEvent> ENTITY_CONVERSLIN_SHRIEK = addSoundsToRegistry("entity.converslin.shriek");
    public static final RegistryObject<SoundEvent> ENTITY_CONVERSLIN_JUMP = addSoundsToRegistry("entity.converslin.jump");

    public static final RegistryObject<SoundEvent> HUGE_EXPLOSION = addSoundsToRegistry("huge_explosion");
    public static final RegistryObject<SoundEvent> YEET = addSoundsToRegistry("yeet");
    public static final RegistryObject<SoundEvent> AIMBOT_SHOOT = addSoundsToRegistry("entity.aimbot.shoot");
    public static final RegistryObject<SoundEvent> AIMBOT_BANNED = addSoundsToRegistry("entity.aimbot.banned");
    public static final RegistryObject<SoundEvent> SUPERDUPERPOISON_EXPLOSION = addSoundsToRegistry("superduperpoison_explosion");
    public static final RegistryObject<SoundEvent> SUPERDUPERPOISON_NOSCREAM = addSoundsToRegistry("superduperpoison_noscream");

    public static final RegistryObject<SoundEvent> ENTITY_AMOEBIC_DEVOURER_GULP = addSoundsToRegistry("entity.amoebic_devourer.gulp");
    public static final RegistryObject<SoundEvent> ENTITY_AMOEBIC_DEVOURER_BIG_GULP = addSoundsToRegistry("entity.amoebic_devourer.big_gulp");

    public static final RegistryObject<SoundEvent> CTF_FRIENDLY_TAKE = addSoundsToRegistry("gamemode_fun.ctf.friendly_take");
    public static final RegistryObject<SoundEvent> CTF_FRIENDLY_CAP = addSoundsToRegistry("gamemode_fun.ctf.friendly_cap");

    public static final RegistryObject<SoundEvent> CTF_ENEMY_RETURN = addSoundsToRegistry("gamemode_fun.ctf.enemy_return");

    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE1_INTRO = addSoundsToRegistry("music.defender.phase1_intro");
    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE1 = addSoundsToRegistry("music.defender.phase1");
    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE1_TRANS = addSoundsToRegistry("music.defender.phase1_trans");
    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE2_INTRO = addSoundsToRegistry("music.defender.phase2_intro");
    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE2 = addSoundsToRegistry("music.defender.phase2");
    public static final RegistryObject<SoundEvent> MUSIC_DEFENDER_PHASE2_TRANS = addSoundsToRegistry("music.defender.phase2_trans");

    private static RegistryObject<SoundEvent> addSoundsToRegistry(String soundId) {
        ResourceLocation name = new ResourceLocation(YellowbrossExtras.MOD_ID, soundId);
        return SOUND_EVENTS.register(soundId, () -> new SoundEvent(name));
    }
}
