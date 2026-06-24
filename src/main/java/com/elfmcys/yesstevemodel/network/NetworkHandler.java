package com.elfmcys.yesstevemodel.network;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.network.message.*;
import io.netty.util.AttributeKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = YesSteveModel.MOD_ID)
public final class NetworkHandler {

    public static final String VERSION = "2.6.0";

    private static final AttributeKey<String> CHANNEL_VERSION_KEY = AttributeKey.valueOf("yes_steve_model_channel_version");

    public static boolean setChannelVersion(Connection connection, String str) {
        return connection != null
                && connection.channel() != null
                && connection.channel().attr(CHANNEL_VERSION_KEY).compareAndSet(null, str);
    }

    public static boolean isPlayerConnected(ServerPlayer serverPlayer) {
        return serverPlayer.connection != null && isConnectionValid(serverPlayer.connection.getConnection());
    }

    public static boolean isClientConnected() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        return connection != null && isConnectionValid(connection.getConnection());
    }

    public static boolean isConnectionValid(@Nullable Connection connection) {
        return connection != null
                && connection.channel() != null
                && VERSION.equals(connection.channel().attr(CHANNEL_VERSION_KEY).get());
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // S2C packets (play_to_client)
        registrar.playToClient(
                S2CModelSyncPayload.TYPE,
                S2CModelSyncPayload.STREAM_CODEC,
                S2CModelSyncPayload::handle
        );
        registrar.playToClient(
                S2CExecuteMolangPacket.TYPE,
                S2CExecuteMolangPacket.STREAM_CODEC,
                S2CExecuteMolangPacket::handle
        );
        registrar.playToClient(
                S2CSetModelAndTexturePacket.TYPE,
                S2CSetModelAndTexturePacket.STREAM_CODEC,
                S2CSetModelAndTexturePacket::handle
        );
        registrar.playToClient(
                S2CSyncAuthModelsPacket.TYPE,
                S2CSyncAuthModelsPacket.STREAM_CODEC,
                S2CSyncAuthModelsPacket::handle
        );
        registrar.playToClient(
                S2CSyncPlayerStatePacket.TYPE,
                S2CSyncPlayerStatePacket.STREAM_CODEC,
                S2CSyncPlayerStatePacket::handle
        );
        registrar.playToClient(
                S2CSyncProjectileModelPacket.TYPE,
                S2CSyncProjectileModelPacket.STREAM_CODEC,
                S2CSyncProjectileModelPacket::handle
        );
        registrar.playToClient(
                S2CSyncStarModelsPacket.TYPE,
                S2CSyncStarModelsPacket.STREAM_CODEC,
                S2CSyncStarModelsPacket::handle
        );
        registrar.playToClient(
                S2CSyncVehicleModelPacket.TYPE,
                S2CSyncVehicleModelPacket.STREAM_CODEC,
                S2CSyncVehicleModelPacket::handle
        );
        registrar.playToClient(
                S2CVersionCheckPacket.TYPE,
                S2CVersionCheckPacket.STREAM_CODEC,
                S2CVersionCheckPacket::handle
        );
        registrar.playToClient(
                S2CSyncAnimationExpressionPacket.TYPE,
                S2CSyncAnimationExpressionPacket.STREAM_CODEC,
                S2CSyncAnimationExpressionPacket::handle
        );

        // C2S packets (play_to_server)
        registrar.playToServer(
                C2SModelSyncPayload.TYPE,
                C2SModelSyncPayload.STREAM_CODEC,
                C2SModelSyncPayload::handle
        );
        registrar.playToServer(
                C2SCompleteFeedbackPacket.TYPE,
                C2SCompleteFeedbackPacket.STREAM_CODEC,
                C2SCompleteFeedbackPacket::handle
        );
        registrar.playToServer(
                C2SPlayAnimationPacket.TYPE,
                C2SPlayAnimationPacket.STREAM_CODEC,
                C2SPlayAnimationPacket::handle
        );
        registrar.playToServer(
                C2SRequestExecuteMolangPacket.TYPE,
                C2SRequestExecuteMolangPacket.STREAM_CODEC,
                C2SRequestExecuteMolangPacket::handle
        );
        registrar.playToServer(
                C2SRequestSwitchModelPacket.TYPE,
                C2SRequestSwitchModelPacket.STREAM_CODEC,
                C2SRequestSwitchModelPacket::handle
        );
        registrar.playToServer(
                C2SSetStarModelPacket.TYPE,
                C2SSetStarModelPacket.STREAM_CODEC,
                C2SSetStarModelPacket::handle
        );
        registrar.playToServer(
                C2SSwingArmPacket.TYPE,
                C2SSwingArmPacket.STREAM_CODEC,
                C2SSwingArmPacket::handle
        );
        registrar.playToServer(
                C2SSyncAnimationExpressionPacket.TYPE,
                C2SSyncAnimationExpressionPacket.STREAM_CODEC,
                C2SSyncAnimationExpressionPacket::handle
        );
        registrar.playToServer(
                C2SVersionCheckPacket.TYPE,
                C2SVersionCheckPacket.STREAM_CODEC,
                C2SVersionCheckPacket::handle
        );
    }

    public static void init() {
        // Network registration is handled by the @SubscribeEvent register method
    }

    public static void sendToServer(Object obj) {
        if (isClientConnected() && obj instanceof CustomPacketPayload) {
            Minecraft.getInstance().getConnection().send((CustomPacketPayload) obj);
        }
    }

    public static void sendToClientPlayer(Object obj, Player player) {
        if (obj instanceof CustomPacketPayload payload) {
            ((ServerPlayer) player).connection.send(payload);
        }
    }

    public static void sendToAll(Object obj) {
        if (obj instanceof CustomPacketPayload payload) {
            net.minecraft.server.MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.getPlayerList().broadcastAll(new net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket(payload));
            }
        }
    }

    public static void sendToTrackingEntity(Object obj, Entity entity) {
        if (obj instanceof CustomPacketPayload payload) {
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
        }
    }

    public static void sendToTrackingEntityAndSelf(Object obj, Player player) {
        if (obj instanceof CustomPacketPayload payload) {
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, payload);
        }
    }
}
