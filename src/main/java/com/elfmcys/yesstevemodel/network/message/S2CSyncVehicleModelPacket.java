package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.capabilities.ClientCapabilities;
import com.elfmcys.yesstevemodel.capability.VehicleModelCapability;
import com.elfmcys.yesstevemodel.capability.VehicleCapability;
import com.elfmcys.yesstevemodel.event.EntityJoinCallbackEvent;
import com.elfmcys.yesstevemodel.geckolib3.core.molang.util.StringPool;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class S2CSyncVehicleModelPacket implements CustomPacketPayload {

    public static final Type<S2CSyncVehicleModelPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(YesSteveModel.MOD_ID, "sync_vehicle_model"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncVehicleModelPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CSyncVehicleModelPacket::encode, S2CSyncVehicleModelPacket::decode);

    private final int entityId;

    private final VehicleModelCapability capability;

    private final Int2FloatOpenHashMap floatMap;

    public S2CSyncVehicleModelPacket(int entityId, VehicleModelCapability capability, Int2FloatOpenHashMap floatMap) {
        this.entityId = entityId;
        this.capability = capability;
        this.floatMap = floatMap;
    }

    public S2CSyncVehicleModelPacket(int entityId, VehicleModelCapability capability) {
        this(entityId, capability, new Int2FloatOpenHashMap(0));
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeVarInt(this.entityId);
        friendlyByteBuf.writeNbt(this.capability.serializeNBT());
    }

    public static S2CSyncVehicleModelPacket decode(FriendlyByteBuf buf) {
        int varInt = buf.readVarInt();
        CompoundTag nbt = buf.readNbt();
        VehicleModelCapability cap = new VehicleModelCapability();
        if (nbt != null) {
            cap.deserializeNBT(nbt);
        }
        Object2FloatOpenHashMap<String> objectMap = cap.getMolangVars();
        Int2FloatOpenHashMap floatMap = new Int2FloatOpenHashMap();
        objectMap.object2FloatEntrySet().fastForEach(entry -> floatMap.put(StringPool.computeIfAbsent(entry.getKey()), entry.getFloatValue()));
        return new S2CSyncVehicleModelPacket(varInt, cap, floatMap);
    }

    public static void handle(S2CSyncVehicleModelPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            EntityJoinCallbackEvent.addCallback(message.entityId, entity -> handleCapability(entity, message.capability, message.floatMap));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleCapability(Entity entity, VehicleModelCapability capability, Int2FloatOpenHashMap floatMap) {
        VehicleCapability vehicleCapability = entity.getData(ClientCapabilities.VEHICLE_CAP.get());
        if (vehicleCapability != null) {
            vehicleCapability.setOwnerModelId(capability.getOwnerModelId());
            vehicleCapability.setFloatMap(floatMap);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
