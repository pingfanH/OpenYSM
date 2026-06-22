package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.capability.ClientCapabilities;
import com.elfmcys.yesstevemodel.capability.PlayerCapability;
import com.elfmcys.yesstevemodel.event.EntityJoinCallbackEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.elfmcys.yesstevemodel.YesSteveModel;

import java.util.Optional;

public class S2CSetModelAndTexturePacket implements CustomPacketPayload {

    public static final Type<S2CSetModelAndTexturePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "set_model_and_texture"));

    public static final StreamCodec<FriendlyByteBuf, S2CSetModelAndTexturePacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSetModelAndTexturePacket::encode, S2CSetModelAndTexturePacket::decode);

    private final int entityId;
    private final String modelId;
    private final String textureId;
    private final boolean disabled;
    private final S2CSyncPlayerStatePacket entityModelSync;

    public S2CSetModelAndTexturePacket(int entityId, String modelId, String textureId, boolean disabled, S2CSyncPlayerStatePacket playerState) {
        this.entityId = entityId;
        this.modelId = modelId;
        this.textureId = textureId;
        this.entityModelSync = playerState;
        this.disabled = disabled;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeUtf(this.modelId);
        buf.writeUtf(this.textureId);
        buf.writeBoolean(this.disabled);
        this.entityModelSync.encode(buf);
    }

    public static S2CSetModelAndTexturePacket decode(FriendlyByteBuf buf) {
        return new S2CSetModelAndTexturePacket(buf.readVarInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean(), S2CSyncPlayerStatePacket.decode(buf));
    }

    public static void handle(S2CSetModelAndTexturePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            EntityJoinCallbackEvent.addCallback(packet.entityId, entity -> {
                applyOnClient(entity, packet);
            });
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void applyOnClient(Entity entity, S2CSetModelAndTexturePacket packet) {
        PlayerCapability cap = entity.getData(ClientCapabilities.PLAYER_CAP.get());
        if (cap != null) {
            cap.initModelWithTexture(packet.modelId, packet.textureId);
            cap.setForceDisabled(packet.disabled);
            S2CSyncPlayerStatePacket.handleCapability(entity, packet.entityModelSync);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
