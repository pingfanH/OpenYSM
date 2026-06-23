package com.elfmcys.yesstevemodel.event;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.elfmcys.yesstevemodel.capability.Capabilities;
import com.elfmcys.yesstevemodel.capability.ClientCapabilities;
import com.elfmcys.yesstevemodel.capability.*;
import com.elfmcys.yesstevemodel.config.ServerConfig;
import com.elfmcys.yesstevemodel.model.ServerModelManager;
import com.elfmcys.yesstevemodel.network.NetworkHandler;
import com.elfmcys.yesstevemodel.network.message.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@EventBusSubscriber
public final class CapabilityEvent {

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!YesSteveModel.isAvailable()) {
            return;
        }
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        ModelInfoCapability oldModelInfo = original.getData(Capabilities.MODEL_INFO.get());
        ModelInfoCapability newModelInfo = newPlayer.getData(Capabilities.MODEL_INFO.get());
        if (oldModelInfo != null && newModelInfo != null) {
            newModelInfo.copyFrom(oldModelInfo);
        }

        AuthModelsCapability oldAuthModels = original.getData(Capabilities.AUTH_MODELS.get());
        AuthModelsCapability newAuthModels = newPlayer.getData(Capabilities.AUTH_MODELS.get());
        if (oldAuthModels != null && newAuthModels != null) {
            newAuthModels.copyFrom(oldAuthModels);
        }

        StarModelsCapability oldStarModels = original.getData(Capabilities.STAR_MODELS.get());
        StarModelsCapability newStarModels = newPlayer.getData(Capabilities.STAR_MODELS.get());
        if (oldStarModels != null && newStarModels != null) {
            newStarModels.copyFrom(oldStarModels);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking startTracking) {
        if (!YesSteveModel.isAvailable()) {
            return;
        }
        Entity target = startTracking.getTarget();
        if (target instanceof ServerPlayer trackPlayer) {
            Player entity = startTracking.getEntity();
            ModelInfoCapability modelInfoCap = trackPlayer.getData(Capabilities.MODEL_INFO.get());
            if (modelInfoCap != null) {
                if (!NetworkHandler.isPlayerConnected(trackPlayer) && !modelInfoCap.isMandatory()) {
                    return;
                }
                Optional<S2CSetModelAndTexturePacket> optional = modelInfoCap.createSyncMessage(trackPlayer, false);
                Consumer<? super S2CSetModelAndTexturePacket> consumer = message -> {
                    NetworkHandler.sendToClientPlayer(message, entity);
                };
                Objects.requireNonNull(modelInfoCap);
                optional.ifPresentOrElse(consumer, modelInfoCap::markDirty);
            }
            return;
        }
        target = startTracking.getTarget();
        if (target instanceof Projectile projectile) {
            ProjectileModelCapability projectileModelCap = projectile.getData(Capabilities.PROJECTILE_MODEL.get());
            if (projectileModelCap != null && projectileModelCap.isInitialized()) {
                NetworkHandler.sendToClientPlayer(new S2CSyncProjectileModelPacket(projectile.getId(), projectileModelCap), startTracking.getEntity());
            }
        } else if (startTracking.getTarget() != null) {
            VehicleModelCapability vehicleModelCap = startTracking.getTarget().getData(Capabilities.VEHICLE_MODEL.get());
            if (vehicleModelCap != null && vehicleModelCap.isInitialized()) {
                NetworkHandler.sendToClientPlayer(new S2CSyncVehicleModelPacket(startTracking.getTarget().getId(), vehicleModelCap), startTracking.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!YesSteveModel.isAvailable()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer player) {
            ModelInfoCapability modelInfoCap = player.getData(Capabilities.MODEL_INFO.get());
            if (modelInfoCap != null) {
                if (!NetworkHandler.isPlayerConnected(player) && !modelInfoCap.isMandatory()) {
                    modelInfoCap.markDirty();
                    return;
                }
                modelInfoCap.stopAnimation(player);
                Optional<S2CSetModelAndTexturePacket> optional = modelInfoCap.createSyncMessage(player, false);
                Consumer<? super S2CSetModelAndTexturePacket> consumer = message -> {
                    NetworkHandler.sendToClientPlayer(message, player);
                };
                Objects.requireNonNull(modelInfoCap);
                optional.ifPresentOrElse(consumer, modelInfoCap::markDirty);
            }
            AuthModelsCapability authModelsCap = player.getData(Capabilities.AUTH_MODELS.get());
            if (authModelsCap != null) {
                for (String modelId : ServerModelManager.getAuthModels()) {
                    authModelsCap.addModel(modelId);
                }
                NetworkHandler.sendToClientPlayer(new S2CSyncAuthModelsPacket(authModelsCap.getAuthModels()), player);
            }
            StarModelsCapability starModelsCap = player.getData(Capabilities.STAR_MODELS.get());
            if (starModelsCap != null) {
                NetworkHandler.sendToClientPlayer(new S2CSyncStarModelsPacket(starModelsCap.getStarModels()), player);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post serverTickEvent) {
        if (YesSteveModel.isAvailable()) {
            List<ServerPlayer> players = serverTickEvent.getServer().getPlayerList().getPlayers();
            Boolean bool = ServerConfig.LOW_BANDWIDTH_USAGE;
            for (ServerPlayer serverPlayer : players) {
                ModelInfoCapability modelInfoCap = serverPlayer.getData(Capabilities.MODEL_INFO.get());
                if (modelInfoCap == null) continue;
                if (!NetworkHandler.isPlayerConnected(serverPlayer) && !modelInfoCap.isMandatory()) {
                    if (serverPlayer.tickCount == 200 || serverPlayer.tickCount == 600 || serverPlayer.tickCount == 1800) {
                        NetworkHandler.sendToClientPlayer(new S2CVersionCheckPacket(), serverPlayer);
                    }
                    continue;
                }
                if (modelInfoCap.isDirty()) {
                    modelInfoCap.getAnimSync().updateAndSync(serverPlayer, false, bool);
                    modelInfoCap.createSyncMessage(serverPlayer, true).ifPresent(message -> {
                        modelInfoCap.clearDirty();
                        NetworkHandler.sendToTrackingEntityAndSelf(message, serverPlayer);
                        if (serverPlayer.getVehicle() != null && serverPlayer.getVehicle().getFirstPassenger() == serverPlayer) {
                            syncVehicleModel(serverPlayer.getVehicle(), serverPlayer);
                        }
                    });
                } else {
                    modelInfoCap.getAnimSync().updateAndSync(serverPlayer, true, bool);
                }
            }
        }
    }

    public static void syncProjectileModel(Projectile projectile, ServerPlayer serverPlayer) {
        ModelInfoCapability modelInfoCap = serverPlayer.getData(Capabilities.MODEL_INFO.get());
        if (modelInfoCap == null) return;
        if (!NetworkHandler.isPlayerConnected(serverPlayer) && !modelInfoCap.isMandatory()) {
            return;
        }
        ProjectileModelCapability projectileModelCap = projectile.getData(Capabilities.PROJECTILE_MODEL.get());
        if (projectileModelCap != null) {
            modelInfoCap.withMolangVars(object2FloatOpenHashMap -> {
                projectileModelCap.setModel(modelInfoCap.getModelId(), object2FloatOpenHashMap);
                NetworkHandler.sendToTrackingEntity(new S2CSyncProjectileModelPacket(projectile.getId(), projectileModelCap), projectile);
            });
        }
    }

    public static void syncVehicleModel(Entity entity, ServerPlayer serverPlayer) {
        ModelInfoCapability modelInfoCap = serverPlayer.getData(Capabilities.MODEL_INFO.get());
        if (modelInfoCap == null) return;
        if (!NetworkHandler.isPlayerConnected(serverPlayer) && !modelInfoCap.isMandatory()) {
            return;
        }
        VehicleModelCapability vehicleModelCap = entity.getData(Capabilities.VEHICLE_MODEL.get());
        if (vehicleModelCap != null) {
            modelInfoCap.getMolangVars().ifPresent(object2FloatOpenHashMap -> {
                vehicleModelCap.setModel(modelInfoCap.getModelId(), object2FloatOpenHashMap);
                NetworkHandler.sendToTrackingEntity(new S2CSyncVehicleModelPacket(entity.getId(), vehicleModelCap), entity);
            });
        }
    }

    public static ModelInfoCapability getModelInfoCap(Player player) {
        return player.getData(Capabilities.MODEL_INFO.get());
    }

    public static AuthModelsCapability getAuthModelsCap(Player player) {
        return player.getData(Capabilities.AUTH_MODELS.get());
    }

    public static StarModelsCapability getStarModelsCap(Player player) {
        return player.getData(Capabilities.STAR_MODELS.get());
    }
}
