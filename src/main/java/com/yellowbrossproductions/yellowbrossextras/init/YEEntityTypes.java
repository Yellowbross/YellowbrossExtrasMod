package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.*;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.*;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.*;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.*;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.Intelligence;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuide;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions.*;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YEEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YellowbrossExtras.MOD_ID);

    // Entity Types
    public static final RegistryObject<EntityType<Vilvgaver>> Vilvgaver = ENTITY_TYPES.register("vilvgaver",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.Vilvgaver::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.3f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "vilvgaver").toString()));

    public static final RegistryObject<EntityType<HyperSnowGolem>> HyperSnowGolem = ENTITY_TYPES.register("hyper_snow_golem",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.HyperSnowGolem::new, MobCategory.MISC)
                    .immuneTo(Blocks.POWDER_SNOW)
                    .fireImmune()
                    .sized(0.7f, 1.9f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "hyper_snow_golem").toString()));

    public static final RegistryObject<EntityType<AmoebicDevourer>> AmoebicDevourer = ENTITY_TYPES.register("amoebic_devourer",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourer::new, MobCategory.MONSTER)
                    .sized(0.9f, 3.0f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "amoebic_devourer").toString()));

    public static final RegistryObject<EntityType<SkeletonSnap>> SkeletonSnap = ENTITY_TYPES.register("skeleton_snap",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.SkeletonSnap::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "skeleton_snap").toString()));

    public static final RegistryObject<EntityType<Aimbot>> Aimbot = ENTITY_TYPES.register("aimbot",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.Aimbot::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "aimbot").toString()));

    public static final RegistryObject<EntityType<StickFigure>> StickFigure = ENTITY_TYPES.register("stick_figure",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.StickFigure::new, MobCategory.MISC)
                    .sized(0.25F, 2.625F)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "stick_figure").toString()));



    // Defender and his entities
    public static final RegistryObject<EntityType<Defender>> Defender = ENTITY_TYPES.register("defender",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender::new, MobCategory.MONSTER)
                    .sized(0.75f, 2.25f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender").toString()));

    public static final RegistryObject<EntityType<SentryGun>> SentryGun = ENTITY_TYPES.register("sentry_gun",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.defender.SentryGun::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.25f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sentry_gun").toString()));

    public static final RegistryObject<EntityType<CreeperBullet>> CreeperBullet = ENTITY_TYPES.register("creeper_bullet",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet::new, MobCategory.MONSTER)
                    .sized(0.3f, 0.75f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "creeper_bullet").toString()));



    // Oryctolins
    public static final RegistryObject<EntityType<Converslin>> Converslin = ENTITY_TYPES.register("converslin",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.Converslin::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.5f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "converslin").toString()));

    public static final RegistryObject<EntityType<CarrotMinionEntity>> CarrotMinion = ENTITY_TYPES.register("carrot_minion",
            () -> EntityType.Builder.of(CarrotMinionEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.8f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "carrot_minion").toString()));



    // Creeper Infection
    public static final RegistryObject<EntityType<Sneaker>> Sneaker = ENTITY_TYPES.register("sneaker",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.creepers.Sneaker::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sneaker").toString()));

    public static final RegistryObject<EntityType<Paracreeper>> Paracreeper = ENTITY_TYPES.register("paracreeper",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.creepers.Paracreeper::new, MobCategory.MONSTER)
                    .sized(0.4f, 0.6f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "paracreeper").toString()));

    public static final RegistryObject<EntityType<Crawler>> Crawler = ENTITY_TYPES.register("crawler",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.creepers.Crawler::new, MobCategory.MONSTER)
                    .sized(2.2f, 2.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "crawler").toString()));

    public static final RegistryObject<EntityType<Freaker>> Freaker = ENTITY_TYPES.register("freaker",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.creepers.Freaker::new, MobCategory.MONSTER)
                    .sized(2.625f, 3.75f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "freaker").toString()));

    public static final RegistryObject<EntityType<Sprayer>> Sprayer = ENTITY_TYPES.register("sprayer",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.creepers.Sprayer::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sprayer").toString()));



    // Projectiles
    public static final RegistryObject<EntityType<DefenderAxe>> DefenderAxe = ENTITY_TYPES.register("defender_axe", () -> EntityType.Builder.<DefenderAxe>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DefenderAxe::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender_axe").toString()));

    public static final RegistryObject<EntityType<Boomerang>> Boomerang = ENTITY_TYPES.register("boomerang",
            () -> EntityType.Builder.of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.Boomerang::new, MobCategory.MONSTER)
                    .sized(0.2f, 0.2f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "boomerang").toString()));

    public static final RegistryObject<EntityType<Spike>> Spike = ENTITY_TYPES.register("spike", () -> EntityType.Builder.<Spike>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.Spike::new, MobCategory.MISC).sized(1.2F,1.2F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "spike").toString()));

    public static final RegistryObject<EntityType<TNTProjectile>> TNTProjectile = ENTITY_TYPES.register("tnt_projectile", () -> EntityType.Builder.<TNTProjectile>of(com.yellowbrossproductions.yellowbrossextras.entities.projectile.TNTProjectile::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "tnt_projectile").toString()));

    public static final RegistryObject<EntityType<Shuriken>> Shuriken = ENTITY_TYPES.register("shuriken", () -> EntityType.Builder.<Shuriken>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.Shuriken::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "shuriken").toString()));

    public static final RegistryObject<EntityType<Chainsaw>> Chainsaw = ENTITY_TYPES.register("chainsaw", () -> EntityType.Builder.<Chainsaw>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.Chainsaw::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "chainsaw").toString()));

    public static final RegistryObject<EntityType<ConverslinBullet>> ConverslinBullet = ENTITY_TYPES.register("converslin_bullet", () -> EntityType.Builder.<ConverslinBullet>of(com.yellowbrossproductions.yellowbrossextras.entities.projectile.ConverslinBullet::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "converslin_bullet").toString()));

    public static final RegistryObject<EntityType<HyperSnowball>> HyperSnowball = ENTITY_TYPES.register("hyper_snowball", () -> EntityType.Builder.<HyperSnowball>of(com.yellowbrossproductions.yellowbrossextras.entities.projectile.HyperSnowball::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "hyper_snowball").toString()));

    public static final RegistryObject<EntityType<SentryBullet>> SentryBullet = ENTITY_TYPES.register("sentry_bullet", () -> EntityType.Builder.<SentryBullet>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SentryBullet::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sentry_bullet").toString()));

    public static final RegistryObject<EntityType<DefenderArrow>> DefenderArrow = ENTITY_TYPES.register("defender_arrow", () -> EntityType.Builder.<DefenderArrow>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DefenderArrow::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender_arrow").toString()));

    public static final RegistryObject<EntityType<SuperDuperPoisonBall>> SuperDuperPoisonBall = ENTITY_TYPES.register("superduperpoison_ball", () -> EntityType.Builder.<SuperDuperPoisonBall>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SuperDuperPoisonBall::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "superduperpoison_ball").toString()));

    public static final RegistryObject<EntityType<SniperRifle>> SniperRifle = ENTITY_TYPES.register("sniper_rifle", () -> EntityType.Builder.<SniperRifle>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SniperRifle::new, MobCategory.MISC).sized(0.0F, 0.0F).setCustomClientFactory(SniperRifle::new).setShouldReceiveVelocityUpdates(false).noSave().build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sniper_rifle").toString()));

    public static final RegistryObject<EntityType<DeadlyArrow>> DeadlyArrow = ENTITY_TYPES.register("deadly_arrow", () -> EntityType.Builder.<DeadlyArrow>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.DeadlyArrow::new, MobCategory.MISC).sized(0.0F, 0.0F).setCustomClientFactory(DeadlyArrow::new).setShouldReceiveVelocityUpdates(false).noSave().build(new ResourceLocation(YellowbrossExtras.MOD_ID, "deadly_arrow").toString()));

    public static final RegistryObject<EntityType<SkullOfDoom>> SkullOfDoom = ENTITY_TYPES.register("skull_of_doom", () -> EntityType.Builder.<SkullOfDoom>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SkullOfDoom::new, MobCategory.MISC).sized(3.0F,3.0F).clientTrackingRange(4).updateInterval(1).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "skull_of_doom").toString()));

    public static final RegistryObject<EntityType<Icicle>> Icicle = ENTITY_TYPES.register("icicle", () -> EntityType.Builder.<Icicle>of(com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.Icicle::new, MobCategory.MISC).sized(0.0F, 0.0F).setCustomClientFactory(Icicle::new).setShouldReceiveVelocityUpdates(false).noSave().build(new ResourceLocation(YellowbrossExtras.MOD_ID, "icicle").toString()));


    // Misc
    public static final RegistryObject<EntityType<CameraShake>> CameraShake = ENTITY_TYPES.register("camera_shake", () -> EntityType.Builder.<CameraShake>of(CameraShake::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).noSave().updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "camera_shake").toString()));

    public static final RegistryObject<EntityType<WitherExplosion>> WitherExplosion = ENTITY_TYPES.register("wither_explosion", () -> EntityType.Builder.<WitherExplosion>of(WitherExplosion::new, MobCategory.MISC).sized(5.0F,5.0F).clientTrackingRange(12).noSave().updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "wither_explosion").toString()));

    public static final RegistryObject<EntityType<OkayMisterImSorry>> OkayMisterImSorry = ENTITY_TYPES.register("okaymisterimsorry", () -> EntityType.Builder.<OkayMisterImSorry>of(OkayMisterImSorry::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).noSave().noSummon().updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "okaymisterimsorry").toString()));


    // Gamemode Fun
    public static final RegistryObject<EntityType<PathGuide>> PathGuide = ENTITY_TYPES.register("path_guide", () -> EntityType.Builder.<PathGuide>of(com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuide::new, MobCategory.MISC).sized(1.0F,2.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "path_guide").toString()));

    public static final RegistryObject<EntityType<Intelligence>> Intelligence = ENTITY_TYPES.register("intelligence", () -> EntityType.Builder.<Intelligence>of(com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.Intelligence::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "intelligence").toString()));
}
