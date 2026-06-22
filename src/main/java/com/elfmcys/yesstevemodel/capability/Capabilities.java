package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Supplier;

public final class Capabilities {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, YesSteveModel.MOD_ID);

    public static final Supplier<AttachmentType<ModelInfoCapability>> MODEL_INFO =
            ATTACHMENT_TYPES.register("model_info", () -> AttachmentType.builder(() -> new ModelInfoCapability())
                    .serialize(ModelInfoCapability::serializeNBT, ModelInfoCapability::deserializeNBT)
                    .build());

    public static final Supplier<AttachmentType<AuthModelsCapability>> AUTH_MODELS =
            ATTACHMENT_TYPES.register("auth_models", () -> AttachmentType.builder(() -> new AuthModelsCapability())
                    .serialize(AuthModelsCapability::serializeNBT, AuthModelsCapability::deserializeNBT)
                    .build());

    public static final Supplier<AttachmentType<StarModelsCapability>> STAR_MODELS =
            ATTACHMENT_TYPES.register("star_models", () -> AttachmentType.builder(() -> new StarModelsCapability())
                    .serialize(StarModelsCapability::serializeNBT, StarModelsCapability::deserializeNBT)
                    .build());

    public static final Supplier<AttachmentType<ProjectileModelCapability>> PROJECTILE_MODEL =
            ATTACHMENT_TYPES.register("projectile_model", () -> AttachmentType.builder(() -> new ProjectileModelCapability())
                    .serialize(ProjectileModelCapability::serializeNBT, ProjectileModelCapability::deserializeNBT)
                    .build());

    public static final Supplier<AttachmentType<VehicleModelCapability>> VEHICLE_MODEL =
            ATTACHMENT_TYPES.register("vehicle_model", () -> AttachmentType.builder(() -> new VehicleModelCapability())
                    .serialize(VehicleModelCapability::serializeNBT, VehicleModelCapability::deserializeNBT)
                    .build());
}
