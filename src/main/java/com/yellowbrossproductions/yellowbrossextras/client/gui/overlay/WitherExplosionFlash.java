package com.yellowbrossproductions.yellowbrossextras.client.gui.overlay;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YellowbrossExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WitherExplosionFlash {
    private static final int MAX_FLASH_TICKS = 8;
    private static int currentFlashTicks = 0;

    public static void triggerFlash() {
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

        float alpha = remaining / (float) MAX_FLASH_TICKS;
        int alphaByte = Math.round(alpha * 255.0F);

        int color = (alphaByte << 24) | 0x00FFFFFF;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        GuiComponent.fill(poseStack, 0, 0, width, height, color);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    };
}
