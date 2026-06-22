package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class C2SSyncAnimationExpressionPacket implements CustomPacketPayload {

    public static final Type<C2SSyncAnimationExpressionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "c2s_sync_animation_expression"));

    public static final StreamCodec<FriendlyByteBuf, C2SSyncAnimationExpressionPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SSyncAnimationExpressionPacket::encode, C2SSyncAnimationExpressionPacket::decode);

    private final FloatArrayList floatData;

    public C2SSyncAnimationExpressionPacket(FloatArrayList floatData) {
        this.floatData = floatData;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.floatData.size());
        for (Float floatDatum : this.floatData) {
            buf.writeFloat(floatDatum);
        }
    }

    public static C2SSyncAnimationExpressionPacket decode(FriendlyByteBuf buf) {
        int size = buf.readByte();
        FloatArrayList floatArrayList = new FloatArrayList(size);
        for (int i = 0; i < size; i++) {
            floatArrayList.add(buf.readFloat());
        }
        return new C2SSyncAnimationExpressionPacket(floatArrayList);
    }

    public static void handle(C2SSyncAnimationExpressionPacket message, IPayloadContext context) {
        ServerPlayer sender = (ServerPlayer) context.player();
        if (context.flow().isServerbound() && sender != null) {
            context.enqueueWork(() -> NetworkHandler.sendToTrackingEntityAndSelf(new S2CSyncAnimationExpressionPacket(sender.getId(), message.floatData), sender));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
