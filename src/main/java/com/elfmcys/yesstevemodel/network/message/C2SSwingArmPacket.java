package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class C2SSwingArmPacket implements CustomPacketPayload {

    public static final Type<C2SSwingArmPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "swing_arm"));

    public static final StreamCodec<FriendlyByteBuf, C2SSwingArmPacket> STREAM_CODEC =
            StreamCodec.ofMember(C2SSwingArmPacket::encode, C2SSwingArmPacket::decode);

    private final InteractionHand hand;

    public C2SSwingArmPacket(InteractionHand hand) {
        this.hand = hand;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
    }

    public static C2SSwingArmPacket decode(FriendlyByteBuf buf) {
        return new C2SSwingArmPacket(buf.readEnum(InteractionHand.class));
    }

    public static void handle(C2SSwingArmPacket message, IPayloadContext context) {
        ServerPlayer sender = (ServerPlayer) context.player();
        if (context.flow().isServerbound() && sender != null) {
            context.enqueueWork(() -> {
                processSwingArm(message, sender);
            });
        }
    }

    public static void processSwingArm(C2SSwingArmPacket message, ServerPlayer sender) {
        InteractionHand interactionHand = message.hand;
        ItemStack itemInHand = sender.getItemInHand(interactionHand);
        if (itemInHand.isEmpty() || !itemInHand.onEntitySwing(sender)) {
            if (!sender.swinging || sender.swingTime >= getSwingDuration(sender) / 2 || sender.swingTime < 0) {
                sender.swingTime = -1;
                sender.swinging = true;
                sender.swingingArm = interactionHand;
                if (sender.level() instanceof ServerLevel) {
                    ((ServerChunkCache) sender.level().getChunkSource()).broadcast(sender, new ClientboundAnimatePacket(sender, interactionHand == InteractionHand.MAIN_HAND ? 0 : 3));
                }
            }
        }
    }

    private static int getSwingDuration(LivingEntity entity) {
        if (MobEffectUtil.hasDigSpeed(entity)) {
            return 6 - (1 + MobEffectUtil.getDigSpeedAmplification(entity));
        }
        if (entity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            return 6 + ((1 + entity.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2);
        }
        return 6;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
