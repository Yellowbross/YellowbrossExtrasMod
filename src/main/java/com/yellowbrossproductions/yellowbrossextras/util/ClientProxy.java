package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.events.ClientEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
    }
}
