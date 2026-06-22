package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.capability.Capabilities;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class C2SSetStarModelPacket implements CustomPacketPayload {

    public static final Type<C2SSetStarModelPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "set_star_model"));

    public static final StreamCodec<FriendlyByteBuf, C2SSetStarModelPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SSetStarModelPacket::encode, C2SSetStarModelPacket::decode);

    private final String modelId;

    private final boolean isAdd;

    private C2SSetStarModelPacket(String modelId, boolean isAdd) {
        this.modelId = modelId;
        this.isAdd = isAdd;
    }

    public static C2SSetStarModelPacket add(String modelId) {
        return new C2SSetStarModelPacket(modelId, true);
    }

    public static C2SSetStarModelPacket remove(String modelId) {
        return new C2SSetStarModelPacket(modelId, false);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.modelId);
        buf.writeBoolean(this.isAdd);
    }

    public static C2SSetStarModelPacket decode(FriendlyByteBuf buf) {
        return new C2SSetStarModelPacket(buf.readUtf(), buf.readBoolean());
    }

    public static void handle(C2SSetStarModelPacket message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                if (sender == null) {
                    return;
                }
                handleCapability(message, sender);
            });
        }
    }

    private static void handleCapability(C2SSetStarModelPacket message, ServerPlayer sender) {
        Optional.ofNullable(sender.getData(Capabilities.STAR_MODELS.get())).ifPresent(cap -> {
            if (message.isAdd) {
                cap.addModel(message.modelId);
            } else {
                cap.removeModel(message.modelId);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
