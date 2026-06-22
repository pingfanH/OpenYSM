package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class C2SVersionCheckPacket implements CustomPacketPayload {

    public static final Type<C2SVersionCheckPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "c2s_version_check"));

    public static final StreamCodec<FriendlyByteBuf, C2SVersionCheckPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SVersionCheckPacket::encode, C2SVersionCheckPacket::decode);

    private final String version;

    public C2SVersionCheckPacket() {
        this(NetworkHandler.VERSION);
    }

    public C2SVersionCheckPacket(String version) {
        this.version = version;
    }

    public static C2SVersionCheckPacket decode(FriendlyByteBuf buf) {
        return new C2SVersionCheckPacket(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.version);
    }

    public static void handle(C2SVersionCheckPacket message, IPayloadContext context) {
        ServerPlayer sender = (ServerPlayer) context.player();
        if (sender != null && NetworkHandler.setChannelVersion(context.connection(), message.version)) {
            ServerModelManager.validatePlayerModel(sender);
            Optional.ofNullable(sender.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
                cap.setMandatory(false);
                cap.stopAnimation(sender);
            });
            Optional.ofNullable(sender.getData(Capabilities.AUTH_MODELS.get())).ifPresent(cap -> {
                NetworkHandler.sendToClientPlayer(new S2CSyncAuthModelsPacket(cap.getAuthModels()), sender);
            });
            Optional.ofNullable(sender.getData(Capabilities.STAR_MODELS.get())).ifPresent(cap -> {
                NetworkHandler.sendToClientPlayer(new S2CSyncStarModelsPacket(cap.getStarModels()), sender);
            });
            ServerModelManager.requestPlayerAuth(sender, null);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
