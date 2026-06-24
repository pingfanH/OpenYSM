package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.client.ClientModelManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.nio.ByteBuffer;

public class S2CModelSyncPayload implements CustomPacketPayload {

    public static final Type<S2CModelSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "s2c_model_sync"));

    public static final StreamCodec<FriendlyByteBuf, S2CModelSyncPayload> STREAM_CODEC =
            StreamCodec.ofMember(S2CModelSyncPayload::encode, S2CModelSyncPayload::decode);

    private final ByteBuffer data;

    public S2CModelSyncPayload(ByteBuffer data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBytes(this.data.asReadOnlyBuffer());
    }

    public static S2CModelSyncPayload decode(FriendlyByteBuf buf) {
        ByteBuffer data = ByteBuffer.allocateDirect(buf.readableBytes());
        buf.readBytes(data);
        data.flip();
        return new S2CModelSyncPayload(data);
    }

    public static void handle(S2CModelSyncPayload message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            ClientModelManager.startSync(context.connection(), message.data);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
