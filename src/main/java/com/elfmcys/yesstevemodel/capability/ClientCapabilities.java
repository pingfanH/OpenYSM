package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ClientCapabilities {

    public static final DeferredRegister<AttachmentType<?>> CLIENT_ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, YesSteveModel.MOD_ID);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<PlayerCapability>> PLAYER_CAP =
            CLIENT_ATTACHMENT_TYPES.register("player_cap", () -> (AttachmentType<PlayerCapability>) (Object) AttachmentType.builder(() -> (PlayerCapability) null).build());

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<VehicleCapability>> VEHICLE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("vehicle_cap", () -> (AttachmentType<VehicleCapability>) (Object) AttachmentType.builder(() -> (VehicleCapability) null).build());

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<ProjectileCapability>> PROJECTILE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("projectile_cap", () -> (AttachmentType<ProjectileCapability>) (Object) AttachmentType.builder(() -> (ProjectileCapability) null).build());
}
