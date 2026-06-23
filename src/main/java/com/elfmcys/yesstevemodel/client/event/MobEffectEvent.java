package com.elfmcys.yesstevemodel.client.event;

import com.elfmcys.yesstevemodel.capability.Capabilities;

import java.util.Optional;

import com.elfmcys.yesstevemodel.YesSteveModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class MobEffectEvent {
    @SubscribeEvent
    public static void onEffectAdded(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Added event) {
        if (!YesSteveModel.isAvailable() || event.getEntity().level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            if (event.getEffectInstance().getEffect().value() != null) {
                MobEffectInstance effectInstance = event.getEffectInstance();
                Optional.ofNullable(serverPlayer.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
                    cap.getAnimSync().syncEffectAdded(serverPlayer, effectInstance.getEffect().value(), effectInstance.getAmplifier() + 1);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove event) {
        if (!YesSteveModel.isAvailable() || event.getEntity().level().isClientSide()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            if (event.getEffect().value() != null) {
                Optional.ofNullable(serverPlayer.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
                    cap.getAnimSync().syncEffectRemoved(serverPlayer, event.getEffect().value());
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired event) {
        if (!YesSteveModel.isAvailable() || event.getEntity().level().isClientSide()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer) {
            if (event.getEffectInstance() != null && event.getEffectInstance().getEffect().value() != null) {
                Optional.ofNullable(serverPlayer.getData(Capabilities.MODEL_INFO.get())).ifPresent(cap -> {
                    cap.getAnimSync().syncEffectRemoved(serverPlayer, event.getEffectInstance().getEffect().value());
                });
            }
        }
    }
}
