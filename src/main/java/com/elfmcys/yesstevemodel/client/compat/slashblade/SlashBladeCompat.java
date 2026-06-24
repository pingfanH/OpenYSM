package com.elfmcys.yesstevemodel.client.compat.slashblade;

import com.elfmcys.yesstevemodel.client.entity.CustomPlayerEntity;
import com.elfmcys.yesstevemodel.geckolib3.core.builder.ILoopType;
import com.elfmcys.yesstevemodel.geckolib3.core.enums.PlayState;
import com.elfmcys.yesstevemodel.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.world.entity.LivingEntity;

public class SlashBladeCompat {
    public static void init() {}

    public static void registerControllerFunctions(Object binding) {}

    public static PlayState handleSlashBladeAnim(LivingEntity entity, AnimationEvent<CustomPlayerEntity> event, String name, ILoopType loopType) {
        return null;
    }

    public static boolean isSlashBladeItem(Object item) { return false; }

    public static boolean isLoaded() { return false; }
}
