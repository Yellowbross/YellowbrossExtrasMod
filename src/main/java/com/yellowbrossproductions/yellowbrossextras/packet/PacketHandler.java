package com.yellowbrossproductions.yellowbrossextras.packet;


import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

// code borrowed from The Twilight Forest
public class PacketHandler {
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            YellowbrossExtras.prefix("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings({"UnusedAssignment", "Convert2Lambda", "Anonymous2MethodRef"})
    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id++, ParticlePacket.class, ParticlePacket::encode, ParticlePacket::new, ParticlePacket.Handler::onMessage);
        // CHANNEL.registerMessage(id++, MessageSuperDuperPoison.class, MessageSuperDuperPoison::serialize, MessageSuperDuperPoison::deserialize, MessageSuperDuperPoison::handle);
        CHANNEL.registerMessage(id++, WitherExplosionFlashPacket.class, WitherExplosionFlashPacket::encode, WitherExplosionFlashPacket::decode, WitherExplosionFlashPacket::handle);
        CHANNEL.registerMessage(id++, WitherExplosionOverlayPacket.class, WitherExplosionOverlayPacket::encode, WitherExplosionOverlayPacket::decode, WitherExplosionOverlayPacket::handle);
    }
}
