package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.capability.Capabilities;

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

public class S2CSyncStarModelsPacket implements CustomPacketPayload {

    public static final Type<S2CSyncStarModelsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "sync_star_models"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncStarModelsPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSyncStarModelsPacket::encode, S2CSyncStarModelsPacket::decode);

    private final Set<String> starModels;

    public S2CSyncStarModelsPacket(Set<String> starModels) {
        this.starModels = starModels;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.starModels.size());
        for (String starModel : this.starModels) {
            buf.writeUtf(starModel);
        }
    }

    public static S2CSyncStarModelsPacket decode(FriendlyByteBuf buf) {
        int varInt = buf.readVarInt();
        HashSet<String> tmp = Sets.newHashSet();
        for (int i = 0; i < varInt; i++) {
            tmp.add(buf.readUtf());
        }
        return new S2CSyncStarModelsPacket(tmp);
    }

    public static void handle(S2CSyncStarModelsPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> handleCapability(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleCapability(S2CSyncStarModelsPacket message) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            Optional.ofNullable(minecraft.player.getData(Capabilities.STAR_MODELS.get())).ifPresent(cap -> cap.setStarModels(message.starModels));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
