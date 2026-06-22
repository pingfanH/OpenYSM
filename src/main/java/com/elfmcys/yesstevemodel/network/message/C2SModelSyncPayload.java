package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.nio.ByteBuffer;

public class C2SModelSyncPayload implements CustomPacketPayload {

    public static final Type<C2SModelSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "model_sync"));

    public static final StreamCodec<FriendlyByteBuf, C2SModelSyncPayload> STREAM_CODEC =
            StreamCodec.ofMember(C2SModelSyncPayload::encode, C2SModelSyncPayload::decode);

    private final ByteBuffer data;

    public C2SModelSyncPayload(ByteBuffer data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBytes(this.data);
    }

    public static C2SModelSyncPayload decode(FriendlyByteBuf buf) {
        ByteBuffer data = ByteBuffer.allocateDirect(buf.readableBytes());
        buf.readBytes(data);
        return new C2SModelSyncPayload(data);
    }

    public static void handle(C2SModelSyncPayload message, IPayloadContext context) {
        if (context.flow().isServerbound() && context.player() != null) {
            ServerModelManager.nativeSendModelData(((ServerPlayer) context.player()).getUUID(), message.data);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
