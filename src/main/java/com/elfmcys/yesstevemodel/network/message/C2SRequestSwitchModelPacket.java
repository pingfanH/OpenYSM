package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.config.ServerConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class C2SRequestSwitchModelPacket implements CustomPacketPayload {

    public static final Type<C2SRequestSwitchModelPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "request_switch_model"));

    public static final StreamCodec<FriendlyByteBuf, C2SRequestSwitchModelPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SRequestSwitchModelPacket::encode, C2SRequestSwitchModelPacket::decode);

    private final String modelId;

    private final String textureId;

    public C2SRequestSwitchModelPacket(String modelId, String textureId) {
        this.modelId = modelId;
        this.textureId = textureId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.modelId);
        buf.writeUtf(this.textureId);
    }

    public static C2SRequestSwitchModelPacket decode(FriendlyByteBuf buf) {
        return new C2SRequestSwitchModelPacket(buf.readUtf(), buf.readUtf());
    }

    public static void handle(C2SRequestSwitchModelPacket message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                if (sender != null && ServerConfig.CAN_SWITCH_MODEL.get()) {
                    handleCapability(message, sender);
                }
            });
        }
    }

    private static void handleCapability(C2SRequestSwitchModelPacket message, ServerPlayer sender) {
        Optional.ofNullable(sender.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
            Optional.ofNullable(sender.getData(Capabilities.AUTH_MODELS.get())).ifPresent(cap2 -> {
                String str = message.modelId;
                if (!ServerModelManager.getServerModelInfo().containsKey(str) || ((ServerModelManager.getAuthModels().contains(str) && !cap2.containsModel(message.modelId)) || !ServerModelManager.getServerModelInfo().get(str).getModelInfo().getTextures().contains(message.textureId))) {
                    cap.resetToDefault();
                } else {
                    cap.setModelAndTexture(message.modelId, message.textureId);
                }
                cap.stopAnimation(sender);
            });
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
