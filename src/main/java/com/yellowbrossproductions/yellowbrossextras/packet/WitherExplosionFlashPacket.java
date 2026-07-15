package com.yellowbrossproductions.yellowbrossextras.packet;

import com.yellowbrossproductions.yellowbrossextras.client.gui.overlay.WitherExplosionOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WitherExplosionFlashPacket {
    public WitherExplosionFlashPacket() {}

    public static void encode(WitherExplosionFlashPacket msg, FriendlyByteBuf buf) {}

    public static WitherExplosionFlashPacket decode(FriendlyByteBuf buf) {
        return new WitherExplosionFlashPacket();
    }

    public static void handle(WitherExplosionFlashPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(WitherExplosionOverlay::triggerFlash);
        ctx.get().setPacketHandled(true);
    }
}
