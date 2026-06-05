package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.*;
import com.yellowbrossproductions.yellowbrossextras.client.model.defender.*;
import com.yellowbrossproductions.yellowbrossextras.client.model.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.defender.*;
import com.yellowbrossproductions.yellowbrossextras.client.render.oryctolins.*;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

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

        event.registerLayerDefinition(PathGuideModel.LAYER_LOCATION, PathGuideModel::createBodyLayer);
        event.registerLayerDefinition(IntelligenceModel.LAYER_LOCATION, IntelligenceModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.Defender.get(), DefenderRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SentryGun.get(), SentryGunRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CreeperBullet.get(), CreeperBulletRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.Sneaker.get(), SneakerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Paracreeper.get(), ParacreeperRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Crawler.get(), CrawlerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Freaker.get(), FreakerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Sprayer.get(), SprayerRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.Vilvgaver.get(), VilvgaverRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.Converslin.get(), ConverslinRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.CarrotMinion.get(), CarrotMinionRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.AmoebicDevourer.get(), AmoebicDevourerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.HyperSnowGolem.get(), HyperSnowGolemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SkeletonSnap.get(), SkeletonSnapRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Aimbot.get(), AimbotRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.StickFigure.get(), StickFigureRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.DefenderAxe.get(), DefenderAxeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Boomerang.get(), BoomerangRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Spike.get(), SpikeRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TNTProjectile.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Shuriken.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Chainsaw.get(), ChainsawRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ConverslinBullet.get(), (p_174064_) -> {
            return new ThrownItemRenderer<>(p_174064_, 1.5F, true);
        });
        event.registerEntityRenderer(ModEntityTypes.HyperSnowball.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SentryBullet.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DefenderArrow.get(), DefenderArrowRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.CameraShake.get(), NothingRenderer::new);

        event.registerEntityRenderer(ModEntityTypes.PathGuide.get(), PathGuideRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.Intelligence.get(), IntelligenceRenderer::new);
    }
}
