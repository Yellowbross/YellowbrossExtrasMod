package com.yellowbrossproductions.yellowbrossextras.util;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

// Needed for Defender stare goal
@Mod.EventBusSubscriber
public class DelayedActionHandler {
    private static final List<Runnable> QUEUED_ACTIONS = new ArrayList<>();

    public static synchronized void queueAction(Runnable action) {
        QUEUED_ACTIONS.add(action);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
            List<Runnable> localActions;

            synchronized (QUEUED_ACTIONS) {
                if (QUEUED_ACTIONS.isEmpty()) return;
                localActions = new ArrayList<>(QUEUED_ACTIONS);
                QUEUED_ACTIONS.clear();
            }

            for (Runnable action : localActions) {
                action.run();
            }
        }
    }
}
