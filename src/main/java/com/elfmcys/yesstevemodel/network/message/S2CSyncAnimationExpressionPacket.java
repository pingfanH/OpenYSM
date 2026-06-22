package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.capabilities.ClientCapabilities;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class S2CSyncAnimationExpressionPacket implements CustomPacketPayload {

    public static final Type<S2CSyncAnimationExpressionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "s2c_sync_animation_expression"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncAnimationExpressionPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSyncAnimationExpressionPacket::encode, S2CSyncAnimationExpressionPacket::decode);

    private final int entityId;

    private final FloatArrayList floatData;

    public S2CSyncAnimationExpressionPacket(int entityId, FloatArrayList floatData) {
        this.entityId = entityId;
        this.floatData = floatData;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeByte(this.floatData.size());
        for (Float floatDatum : this.floatData) {
            buf.writeFloat(floatDatum);
        }
    }

    public static S2CSyncAnimationExpressionPacket decode(FriendlyByteBuf buf) {
        int varInt = buf.readVarInt();
        int count = buf.readByte();
        FloatArrayList floatArrayList = new FloatArrayList(count);
        for (int i = 0; i < count; i++) {
            floatArrayList.add(buf.readFloat());
        }
        return new S2CSyncAnimationExpressionPacket(varInt, floatArrayList);
    }

    public static void handle(S2CSyncAnimationExpressionPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                handleCapability(message);
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleCapability(S2CSyncAnimationExpressionPacket message) {
        Optional.ofNullable(Minecraft.getInstance().level.getEntity(message.entityId).getData(ClientCapabilities.PLAYER_CAP.get())).ifPresent(cap -> cap.executeAnimationExpression(message.floatData));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
