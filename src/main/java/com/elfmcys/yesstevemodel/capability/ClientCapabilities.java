package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ClientCapabilities {

    public static final DeferredRegister<AttachmentType<?>> CLIENT_ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, YesSteveModel.MOD_ID);

    public static final Supplier<AttachmentType<PlayerCapability>> PLAYER_CAP =
            CLIENT_ATTACHMENT_TYPES.register("player_cap", () -> AttachmentType.builder(ClientCapabilities::createPlayerCapability).build());

    public static final Supplier<AttachmentType<VehicleCapability>> VEHICLE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("vehicle_cap", () -> AttachmentType.builder(ClientCapabilities::createVehicleCapability).build());

    public static final Supplier<AttachmentType<ProjectileCapability>> PROJECTILE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("projectile_cap", () -> AttachmentType.builder(ClientCapabilities::createProjectileCapability).build());

    private static PlayerCapability createPlayerCapability(IAttachmentHolder holder) {
        if (holder instanceof Player player) {
            return new PlayerCapability(player);
        }
        throw new IllegalStateException("player_cap attached to non-player holder: " + holder.getClass().getName());
    }

    private static VehicleCapability createVehicleCapability(IAttachmentHolder holder) {
        if (holder instanceof Entity entity) {
            return new VehicleCapability(entity);
        }
        throw new IllegalStateException("vehicle_cap attached to non-entity holder: " + holder.getClass().getName());
    }

    private static ProjectileCapability createProjectileCapability(IAttachmentHolder holder) {
        if (holder instanceof Projectile projectile) {
            return new ProjectileCapability(projectile);
        }
        throw new IllegalStateException("projectile_cap attached to non-projectile holder: " + holder.getClass().getName());
    }
}
