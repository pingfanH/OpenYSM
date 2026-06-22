package com.elfmcys.yesstevemodel.network.message;

import com.google.common.collect.Sets;
import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class S2CSyncAuthModelsPacket implements CustomPacketPayload {

    public static final Type<S2CSyncAuthModelsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "sync_auth_models"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncAuthModelsPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSyncAuthModelsPacket::encode, S2CSyncAuthModelsPacket::decode);

    private final Set<String> authModels;

    public S2CSyncAuthModelsPacket(Set<String> authModels) {
        this.authModels = authModels;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.authModels.size());
        for (String modelId : this.authModels) {
            buf.writeUtf(modelId);
        }
    }

    public static S2CSyncAuthModelsPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        HashSet<String> tmp = Sets.newHashSet();
        for (int i = 0; i < size; i++) {
            tmp.add(buf.readUtf());
        }
        return new S2CSyncAuthModelsPacket(tmp);
    }

    public static void handle(S2CSyncAuthModelsPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                handleCapability(message);
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleCapability(S2CSyncAuthModelsPacket message) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            Optional.ofNullable(minecraft.player.getData(Capabilities.AUTH_MODELS.get())).ifPresent(cap -> {
                cap.setAuthModels(message.authModels);
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
