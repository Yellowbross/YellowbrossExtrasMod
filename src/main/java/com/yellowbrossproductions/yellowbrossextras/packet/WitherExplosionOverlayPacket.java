package com.yellowbrossproductions.yellowbrossextras.packet;

import com.yellowbrossproductions.yellowbrossextras.client.gui.overlay.WitherExplosionOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WitherExplosionOverlayPacket {
    public WitherExplosionOverlayPacket() {}

    public static void encode(WitherExplosionOverlayPacket msg, FriendlyByteBuf buf) {}

    public static WitherExplosionOverlayPacket decode(FriendlyByteBuf buf) {
        return new WitherExplosionOverlayPacket();
    }

    public static void handle(WitherExplosionOverlayPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(WitherExplosionOverlay::triggerOverlay);
        ctx.get().setPacketHandled(true);
    }
}
