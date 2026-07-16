package com.yellowbrossproductions.yellowbrossextras.events;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.gui.overlay.WitherExplosionOverlay;
import com.yellowbrossproductions.yellowbrossextras.client.model.*;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.*;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.defender.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.layer.SuperDuperPoisonLayer;
import com.yellowbrossproductions.yellowbrossextras.client.render.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEParticleTypes;
import com.yellowbrossproductions.yellowbrossextras.particles.RifleExplosionOnomatopoeiaParticle;
import com.yellowbrossproductions.yellowbrossextras.particles.SuperDuperPoisonDripParticle;
import com.yellowbrossproductions.yellowbrossextras.particles.SuperDuperPoisonExplosionParticle;
import com.yellowbrossproductions.yellowbrossextras.util.DynamicAnimationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, value = Dist.CLIENT)
public class YEClientEventHandler {

    @Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers event) {
            Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
                if (renderer instanceof LivingEntityRenderer) {
                    ((LivingEntityRenderer<?, ?>) renderer).addLayer(new SuperDuperPoisonLayer((LivingEntityRenderer<?, ?>) renderer));
                }
            });
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(DefenderModel.LAYER_LOCATION, DefenderModel::createBodyLayer);
            event.registerLayerDefinition(SentryGunModel.LAYER_LOCATION, SentryGunModel::createBodyLayer);
            event.registerLayerDefinition(CreeperBulletModel.LAYER_LOCATION, CreeperBulletModel::createBodyLayer);

            event.registerLayerDefinition(SneakerModel.LAYER_LOCATION, SneakerModel::createBodyLayer);
            event.registerLayerDefinition(ParacreeperModel.LAYER_LOCATION, ParacreeperModel::createBodyLayer);
            event.registerLayerDefinition(CrawlerModel.LAYER_LOCATION, CrawlerModel::createBodyLayer);
            event.registerLayerDefinition(FreakerModel.LAYER_LOCATION, FreakerModel::createBodyLayer);
            event.registerLayerDefinition(SprayerModel.LAYER_LOCATION, SprayerModel::createBodyLayer);

            event.registerLayerDefinition(ConverslinModel.LAYER_LOCATION, ConverslinModel::createBodyLayer);

            event.registerLayerDefinition(CarrotMinionModel.LAYER_LOCATION, CarrotMinionModel::createBodyLayer);

            event.registerLayerDefinition(AmoebicDevourerModel.LAYER_LOCATION, AmoebicDevourerModel::createBodyLayer);
            event.registerLayerDefinition(HyperSnowGolemModel.LAYER_LOCATION, HyperSnowGolemModel::createBodyLayer);
            event.registerLayerDefinition(SkeletonSnapModel.LAYER_LOCATION, SkeletonSnapModel::createBodyLayer);
            event.registerLayerDefinition(AimbotModel.LAYER_LOCATION, AimbotModel::createBodyLayer);
            event.registerLayerDefinition(StickFigureModel.LAYER_LOCATION, StickFigureModel::createBodyLayer);

            event.registerLayerDefinition(BoomerangModel.LAYER_LOCATION, BoomerangModel::createBodyLayer);
            event.registerLayerDefinition(SpikeModel.LAYER_LOCATION, SpikeModel::createBodyLayer);
            event.registerLayerDefinition(SniperRifleModel.LAYER_LOCATION, SniperRifleModel::createBodyLayer);
            event.registerLayerDefinition(DeadlyArrowModel.LAYER_LOCATION, DeadlyArrowModel::createBodyLayer);
            event.registerLayerDefinition(SkullOfDoomModel.LAYER_LOCATION, SkullOfDoomModel::createBodyLayer);

            event.registerLayerDefinition(PathGuideModel.LAYER_LOCATION, PathGuideModel::createBodyLayer);
            event.registerLayerDefinition(IntelligenceModel.LAYER_LOCATION, IntelligenceModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(YEEntityTypes.Defender.get(), DefenderRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.SentryGun.get(), SentryGunRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.CreeperBullet.get(), CreeperBulletRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.Sneaker.get(), SneakerRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Paracreeper.get(), ParacreeperRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Crawler.get(), CrawlerRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Freaker.get(), FreakerRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Sprayer.get(), SprayerRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.Vilvgaver.get(), VilvgaverRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.Converslin.get(), ConverslinRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.CarrotMinion.get(), CarrotMinionRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.AmoebicDevourer.get(), AmoebicDevourerRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.HyperSnowGolem.get(), HyperSnowGolemRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.SkeletonSnap.get(), SkeletonSnapRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Aimbot.get(), AimbotRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.StickFigure.get(), StickFigureRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.DefenderAxe.get(), DefenderAxeRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Boomerang.get(), BoomerangRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Spike.get(), SpikeRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.TNTProjectile.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Shuriken.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Chainsaw.get(), ChainsawRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.ConverslinBullet.get(), (p_174064_) -> {
                return new ThrownItemRenderer<>(p_174064_, 1.5F, true);
            });
            event.registerEntityRenderer(YEEntityTypes.HyperSnowball.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.SentryBullet.get(), ThrownItemRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.DefenderArrow.get(), DefenderArrowRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.SuperDuperPoisonBall.get(), (p_174064_) -> {
                return new ThrownItemRenderer<>(p_174064_, 1.5F, false);
            });
            event.registerEntityRenderer(YEEntityTypes.SniperRifle.get(), SniperRifleRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.DeadlyArrow.get(), DeadlyArrowRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.SkullOfDoom.get(), SkullOfDoomRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.CameraShake.get(), NothingRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.WitherExplosion.get(), WitherExplosionRenderer::new);

            event.registerEntityRenderer(YEEntityTypes.PathGuide.get(), PathGuideRenderer::new);
            event.registerEntityRenderer(YEEntityTypes.Intelligence.get(), IntelligenceRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
            event.register(YEParticleTypes.SUPERDUPERPOISON_DRIP.get(), SuperDuperPoisonDripParticle.Provider::new);
            event.register(YEParticleTypes.SUPERDUPERPOISON_EXPLOSION.get(), SuperDuperPoisonExplosionParticle.Provider::new);
            event.register(YEParticleTypes.RIFLE_EXPLOSION_ONOMATOPOEIA.get(), RifleExplosionOnomatopoeiaParticle.Provider::new);
        }

        @SubscribeEvent
        public static void registerEntitySpectatorShaders(RegisterEntitySpectatorShadersEvent event) {
            event.register(YEEntityTypes.Sneaker.get(), new ResourceLocation("shaders/post/creeper.json"));
            event.register(YEEntityTypes.Paracreeper.get(), new ResourceLocation("shaders/post/creeper.json"));
            event.register(YEEntityTypes.Crawler.get(), new ResourceLocation("shaders/post/creeper.json"));
            event.register(YEEntityTypes.Freaker.get(), new ResourceLocation("shaders/post/creeper.json"));
            event.register(YEEntityTypes.Sprayer.get(), new ResourceLocation("shaders/post/creeper.json"));
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("witherexplosion_flash_overlay", WitherExplosionOverlay.HUD_OVERLAY);
        }

        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(DynamicAnimationManager.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        Random random = new Random();
        if (player != null) {
            if (YellowbrossExtrasConfig.cameraShakeMultiplier.get() > 0) {
                float shakeAmplitude = 0;
                for (CameraShake cameraShake : player.level.getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(100))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;

                event.setPitch((float) (event.getPitch() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * ((random.nextFloat() - 0.5F) * 3) * 25 * YellowbrossExtrasConfig.cameraShakeMultiplier.get()));
            }
        }
    }
}
