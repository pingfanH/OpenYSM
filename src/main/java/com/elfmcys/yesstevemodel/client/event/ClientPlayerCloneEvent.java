package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.capability.ClientCapabilities;

import java.util.Optional;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber({Dist.CLIENT})
public class ClientPlayerCloneEvent {
    @SubscribeEvent
    public static void onPlayerClone(ClientPlayerNetworkEvent.Clone event) {
        if (!YesSteveModel.isAvailable() || !NetworkHandler.isClientConnected()) {
            return;
        }
        event.getOldPlayer().reviveCaps();
        Optional.ofNullable(event.getOldPlayer().getData(ClientCapabilities.PLAYER_CAP.get())).ifPresent(cap -> Optional.ofNullable(event.getNewPlayer().getData(ClientCapabilities.PLAYER_CAP.get())).ifPresent(cap2 -> cap2.copyFrom(cap)));
        event.getOldPlayer().invalidateCaps();
    }
}
