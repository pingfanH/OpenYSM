package com.elfmcys.yesstevemodel.client.event;

import net.minecraft.world.entity.LivingEntity;

public class ShieldBlockCooldownEvent {

    public static final String TAG_KEY = "ysm$shield_block_cooldown";

    public static boolean isOnCooldown(LivingEntity livingEntity) {
        return livingEntity.getPersistentData().contains(TAG_KEY);
    }
}
