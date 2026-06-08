package com.yellowbrossproductions.yellowbrossextras.message;

import com.yellowbrossproductions.yellowbrossextras.capability.SuperDuperPoisonCapability;
import com.yellowbrossproductions.yellowbrossextras.init.YECapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

// Code adapted from Mowzie's Mobs
public class MessageSuperDuperPoison {
    private int entityID;
    private boolean hasSuperDuperPoison;

    public MessageSuperDuperPoison() {}

    public MessageSuperDuperPoison(LivingEntity entity, boolean activate) {
        entityID = entity.getId();
        hasSuperDuperPoison = activate;
    }

    public static void serialize(final MessageSuperDuperPoison message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.hasSuperDuperPoison);
    }

    public static MessageSuperDuperPoison deserialize(final FriendlyByteBuf buf) {
        final MessageSuperDuperPoison message = new MessageSuperDuperPoison();
        message.entityID = buf.readVarInt();
        message.hasSuperDuperPoison = buf.readBoolean();
        return message;
    }

    public static void handle(MessageSuperDuperPoison message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) entity;
                    SuperDuperPoisonCapability.ISuperDuperPoisonCapability capability = YECapabilities.getCapability(living, YECapabilities.SUPERDUPERPOISON_CAPABILITY);
                    if (capability != null) {
                        capability.setSuperDuperPoison(message.hasSuperDuperPoison);
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
