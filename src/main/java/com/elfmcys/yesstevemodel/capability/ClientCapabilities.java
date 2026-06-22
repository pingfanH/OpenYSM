package com.elfmcys.yesstevemodel.capability;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = YesSteveModel.MOD_ID)
public final class ClientCapabilities {

    public static final DeferredRegister<AttachmentType<?>> CLIENT_ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, YesSteveModel.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final Supplier<AttachmentType<PlayerCapability>> PLAYER_CAP =
            CLIENT_ATTACHMENT_TYPES.register("player_cap", () -> AttachmentType.builder(() -> null).build());

    @SuppressWarnings("unchecked")
    public static final Supplier<AttachmentType<VehicleCapability>> VEHICLE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("vehicle_cap", () -> AttachmentType.builder(() -> null).build());

    @SuppressWarnings("unchecked")
    public static final Supplier<AttachmentType<ProjectileCapability>> PROJECTILE_CAP =
            CLIENT_ATTACHMENT_TYPES.register("projectile_cap", () -> AttachmentType.builder(() -> null).build());

    @SuppressWarnings("unchecked")
    public static final Supplier<AttachmentType<ClientLazyCapability>> CLIENT_LAZY_CAP =
            CLIENT_ATTACHMENT_TYPES.register("client_lazy_cap", () -> AttachmentType.builder(() -> null).build());
}
