package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.capabilities.ClientCapabilities;
import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.TouhouMaidCompat;
import com.elfmcys.yesstevemodel.geckolib3.resource.GeckoLibCache;
import com.elfmcys.yesstevemodel.molang.parser.ParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class S2CExecuteMolangPacket implements CustomPacketPayload {

    public static final Type<S2CExecuteMolangPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "execute_molang"));

    public static final StreamCodec<FriendlyByteBuf, S2CExecuteMolangPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CExecuteMolangPacket::encode, S2CExecuteMolangPacket::decode);

    private final int[] entityIds;

    private final String expression;

    public S2CExecuteMolangPacket(int entityIds, String expression) {
        this.entityIds = new int[]{entityIds};
        this.expression = expression;
    }

    public S2CExecuteMolangPacket(int[] entityIds, String expression) {
        this.entityIds = entityIds;
        this.expression = expression;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarIntArray(this.entityIds);
        buf.writeUtf(this.expression);
    }

    public static S2CExecuteMolangPacket decode(FriendlyByteBuf buf) {
        return new S2CExecuteMolangPacket(buf.readVarIntArray(), buf.readUtf());
    }

    public static void handle(S2CExecuteMolangPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                handleCapability(message);
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleCapability(S2CExecuteMolangPacket message) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        for (int i : message.entityIds) {
            Entity entity = minecraft.level.getEntity(i);
            if (entity instanceof Player) {
                Optional.ofNullable(entity.getData(ClientCapabilities.PLAYER_CAP.get())).ifPresent(cap -> {
                    try {
                        cap.executeExpression(GeckoLibCache.parseSimpleExpression(message.expression), true, false, null);
                    } catch (ParseException e) {
                        YesSteveModel.LOGGER.error("Failed to execute molang " + message.expression, e);
                    }
                });
            } else if (TouhouMaidCompat.isMaidEntity(entity)) {
                TouhouMaidCompat.playMaidAnimation(entity, message.expression);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
