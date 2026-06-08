package com.yellowbrossproductions.yellowbrossextras.init;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.message.MessageSuperDuperPoison;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// Code adapted from Mowzie's Mobs
public class ServerProxy {
    private int nextMessageId;

    public void init(final IEventBus modbus) {

    }

    public void initNetwork() {
        final String version = "1";
        YellowbrossExtras.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(YellowbrossExtras.MOD_ID, "net"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
        this.registerMessage(MessageSuperDuperPoison.class, MessageSuperDuperPoison::serialize, MessageSuperDuperPoison::deserialize, MessageSuperDuperPoison::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    private <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, FriendlyByteBuf> encoder, final Function<FriendlyByteBuf, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer, final NetworkDirection networkDirection) {
        YellowbrossExtras.NETWORK.registerMessage(this.nextMessageId++, clazz, encoder, decoder, consumer, Optional.of(networkDirection));
    }
}
