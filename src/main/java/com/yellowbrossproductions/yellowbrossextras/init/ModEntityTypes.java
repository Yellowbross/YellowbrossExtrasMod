package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.*;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.*;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.IntelligenceEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuideEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions.*;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YellowbrossExtras.MOD_ID);

    // Entity Types
    public static final RegistryObject<EntityType<DefenderEntity>> Defender = ENTITY_TYPES.register("defender",
            () -> EntityType.Builder.of(DefenderEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 2.25f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender").toString()));

    public static final RegistryObject<EntityType<SneakerEntity>> Sneaker = ENTITY_TYPES.register("sneaker",
            () -> EntityType.Builder.of(SneakerEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sneaker").toString()));

    public static final RegistryObject<EntityType<ParacreeperEntity>> Paracreeper = ENTITY_TYPES.register("paracreeper",
            () -> EntityType.Builder.of(ParacreeperEntity::new, MobCategory.MONSTER)
                    .sized(0.4f, 0.6f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "paracreeper").toString()));

    public static final RegistryObject<EntityType<CrawlerEntity>> Crawler = ENTITY_TYPES.register("crawler",
            () -> EntityType.Builder.of(CrawlerEntity::new, MobCategory.MONSTER)
                    .sized(2.2f, 2.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "crawler").toString()));

    public static final RegistryObject<EntityType<FreakerEntity>> Freaker = ENTITY_TYPES.register("freaker",
            () -> EntityType.Builder.of(FreakerEntity::new, MobCategory.MONSTER)
                    .sized(2.625f, 3.75f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "freaker").toString()));

    public static final RegistryObject<EntityType<SprayerEntity>> Sprayer = ENTITY_TYPES.register("sprayer",
            () -> EntityType.Builder.of(SprayerEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "sprayer").toString()));

    public static final RegistryObject<EntityType<VilvgaverEntity>> Vilvgaver = ENTITY_TYPES.register("vilvgaver",
            () -> EntityType.Builder.of(VilvgaverEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.3f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "vilvgaver").toString()));

    public static final RegistryObject<EntityType<HyperSnowGolemEntity>> HyperSnowGolem = ENTITY_TYPES.register("hyper_snow_golem",
            () -> EntityType.Builder.of(HyperSnowGolemEntity::new, MobCategory.MISC)
                    .immuneTo(Blocks.POWDER_SNOW)
                    .fireImmune()
                    .sized(0.7f, 1.9f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "hyper_snow_golem").toString()));

    // Oryctolins
    public static final RegistryObject<EntityType<ConverslinEntity>> Converslin = ENTITY_TYPES.register("converslin",
            () -> EntityType.Builder.of(ConverslinEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.5f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "converslin").toString()));

    public static final RegistryObject<EntityType<CarrotMinionEntity>> CarrotMinion = ENTITY_TYPES.register("carrot_minion",
            () -> EntityType.Builder.of(CarrotMinionEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.8f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "carrot_minion").toString()));

    // Projectiles
    public static final RegistryObject<EntityType<DefenderAxeEntity>> DefenderAxe = ENTITY_TYPES.register("defender_axe",
            () -> EntityType.Builder.of(DefenderAxeEntity::new, MobCategory.MONSTER)
                    .sized(0.2f, 0.2f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "defender_axe").toString()));

    public static final RegistryObject<EntityType<BoomerangEntity>> Boomerang = ENTITY_TYPES.register("boomerang",
            () -> EntityType.Builder.of(BoomerangEntity::new, MobCategory.MONSTER)
                    .sized(0.2f, 0.2f)
                    .build(new ResourceLocation(YellowbrossExtras.MOD_ID, "boomerang").toString()));

    public static final RegistryObject<EntityType<SpikeEntity>> Spike = ENTITY_TYPES.register("spike", () -> EntityType.Builder.<SpikeEntity>of(SpikeEntity::new, MobCategory.MISC).sized(1.2F,1.2F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "spike").toString()));

    public static final RegistryObject<EntityType<TNTProjectileEntity>> TNTProjectile = ENTITY_TYPES.register("tnt_projectile", () -> EntityType.Builder.<TNTProjectileEntity>of(TNTProjectileEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "tnt_projectile").toString()));

    public static final RegistryObject<EntityType<ShurikenEntity>> Shuriken = ENTITY_TYPES.register("shuriken", () -> EntityType.Builder.<ShurikenEntity>of(ShurikenEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "shuriken").toString()));

    public static final RegistryObject<EntityType<ChainsawEntity>> Chainsaw = ENTITY_TYPES.register("chainsaw", () -> EntityType.Builder.<ChainsawEntity>of(ChainsawEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "chainsaw").toString()));

    public static final RegistryObject<EntityType<ConverslinBulletEntity>> ConverslinBullet = ENTITY_TYPES.register("converslin_bullet", () -> EntityType.Builder.<ConverslinBulletEntity>of(ConverslinBulletEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "converslin_bullet").toString()));

    // Misc
    public static final RegistryObject<EntityType<CameraShakeEntity>> CameraShake = ENTITY_TYPES.register("camera_shake", () -> EntityType.Builder.<CameraShakeEntity>of(CameraShakeEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "camera_shake").toString()));

    // Gamemode Fun
    public static final RegistryObject<EntityType<PathGuideEntity>> PathGuide = ENTITY_TYPES.register("path_guide", () -> EntityType.Builder.<PathGuideEntity>of(PathGuideEntity::new, MobCategory.MISC).sized(1.0F,2.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "path_guide").toString()));

    public static final RegistryObject<EntityType<IntelligenceEntity>> Intelligence = ENTITY_TYPES.register("intelligence", () -> EntityType.Builder.<IntelligenceEntity>of(IntelligenceEntity::new, MobCategory.MISC).sized(0.5F,0.5F).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(YellowbrossExtras.MOD_ID, "intelligence").toString()));

    public static void onAttribute(final EntityAttributeCreationEvent event) {
        event.put(Defender.get(), DefenderEntity.createAttributes().build());

        event.put(Sneaker.get(), SneakerEntity.createAttributes().build());
        event.put(Paracreeper.get(), ParacreeperEntity.createAttributes().build());
        event.put(Crawler.get(), CrawlerEntity.createAttributes().build());
        event.put(Freaker.get(), FreakerEntity.createAttributes().build());
        event.put(Sprayer.get(), SprayerEntity.createAttributes().build());

        event.put(Vilvgaver.get(), VilvgaverEntity.createAttributes().build());

        event.put(HyperSnowGolem.get(), HyperSnowGolemEntity.createAttributes().build());

        event.put(Converslin.get(), ConverslinEntity.createAttributes().build());
        event.put(CarrotMinion.get(), CarrotMinionEntity.createAttributes().build());

        event.put(DefenderAxe.get(), DefenderAxeEntity.createAttributes().build());
        event.put(Boomerang.get(), BoomerangEntity.createAttributes().build());
    }
}
