package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.TouhouMaidCompat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record C2SCompleteFeedbackPacket(FeedbackData feedbackData) implements CustomPacketPayload {

    public static final Type<C2SCompleteFeedbackPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "complete_feedback"));

    public static final StreamCodec<FriendlyByteBuf, C2SCompleteFeedbackPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SCompleteFeedbackPacket::encode, C2SCompleteFeedbackPacket::decode);

    public void encode(FriendlyByteBuf buf) {
        FeedbackData.writeToBuf(this.feedbackData, buf);
    }

    public static C2SCompleteFeedbackPacket decode(FriendlyByteBuf buf) {
        return new C2SCompleteFeedbackPacket(FeedbackData.readFromBuf(buf, false));
    }

    public static void handle(C2SCompleteFeedbackPacket message, IPayloadContext context) {
        if (context.flow().isServerbound() && context.player() != null) {
            ServerPlayer sender = (ServerPlayer) context.player();
            context.enqueueWork(() -> {
                handleOnServer(message, sender.serverLevel());
            });
        }
    }

    public static void handleOnServer(C2SCompleteFeedbackPacket message, ServerLevel serverLevel) {
        Entity entity = serverLevel.getEntity(message.feedbackData.flags());
        if (TouhouMaidCompat.isMaidEntity(entity)) {
            TouhouMaidCompat.applyFeedback(entity, message.feedbackData);
        } else if (entity instanceof ServerPlayer serverPlayer) {
            Optional.ofNullable(serverPlayer.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
                cap.applyFeedback(serverPlayer, message.feedbackData);
                if (serverPlayer.getVehicle() != null && serverPlayer.getVehicle().getFirstPassenger() == serverPlayer) {
                    Optional.ofNullable(serverPlayer.getVehicle().getData(Capabilities.VEHICLE_MODEL.get())).ifPresent(vehicleCap -> {
                        cap.getMolangVars().ifPresent(map -> vehicleCap.setModel(cap.getModelId(), map));
                    });
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
