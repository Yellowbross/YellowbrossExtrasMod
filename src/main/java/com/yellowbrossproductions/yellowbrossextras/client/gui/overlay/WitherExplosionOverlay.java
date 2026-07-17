package com.yellowbrossproductions.yellowbrossextras.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WitherExplosionOverlay {
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(YellowbrossExtras.MOD_ID, "textures/entity/defender/witherbazooka/overlay.png");
    private static final int MAX_FLASH_TICKS = 60;
    private static int currentFlashTicks = 0;

    public static void triggerOverlay() {
        currentFlashTicks = MAX_FLASH_TICKS;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.END && currentFlashTicks > 0) {
            currentFlashTicks--;
        }
    }

    public static final IGuiOverlay HUD_OVERLAY = (gui, poseStack, partialTick, width, height) -> {
        if (currentFlashTicks <= 0) return;

        float remaining = (float) currentFlashTicks - partialTick;
        if (remaining < 0) remaining = 0;

        float alpha = 1.0f;
        if (remaining <= 20.0F) alpha = Math.max(0, remaining / 20.0f);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);

        GuiComponent.blit(poseStack, 0, 0, 0, 0, width, height, width, height);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    };
}
