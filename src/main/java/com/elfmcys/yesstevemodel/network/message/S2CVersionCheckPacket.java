package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.client.ClientModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class S2CVersionCheckPacket implements CustomPacketPayload {

    public static final Type<S2CVersionCheckPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "s2c_version_check"));

    public static final StreamCodec<FriendlyByteBuf, S2CVersionCheckPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CVersionCheckPacket::encode, S2CVersionCheckPacket::decode);

    private final String version;

    public S2CVersionCheckPacket() {
        this(NetworkHandler.VERSION);
    }

    private S2CVersionCheckPacket(String version) {
        this.version = version;
    }

    public static S2CVersionCheckPacket decode(FriendlyByteBuf buf) {
        return new S2CVersionCheckPacket(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.version);
    }

    public static void handle(S2CVersionCheckPacket message, IPayloadContext context) {
        if (NetworkHandler.setChannelVersion(context.connection(), message.version)) {
            context.enqueueWork(() -> ClientModelManager.onSyncConnected());
        }
        NetworkHandler.sendToServer(new C2SVersionCheckPacket());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
