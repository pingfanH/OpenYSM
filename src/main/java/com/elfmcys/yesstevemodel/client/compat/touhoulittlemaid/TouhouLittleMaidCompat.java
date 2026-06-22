package com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid;

import com.elfmcys.yesstevemodel.client.animation.molang.TLMBinding;
import com.elfmcys.yesstevemodel.client.model.ModelResourceBundle;
import com.elfmcys.yesstevemodel.client.model.PlayerModelBundle;
import com.elfmcys.yesstevemodel.client.entity.LivingAnimatable;
import com.elfmcys.yesstevemodel.geckolib3.core.event.predicate.AnimationEvent;
import com.elfmcys.yesstevemodel.geckolib3.core.molang.util.StringPool;
import com.elfmcys.yesstevemodel.geckolib3.core.enums.PlayState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

@net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)
public class TouhouLittleMaidCompat {
    public static boolean isLoaded() { return false; }
    public static boolean isMaidEntity(Entity entity) { return false; }
    public static boolean isMaidRideable(Entity entity) { return false; }
    public static boolean isSimplePlanesEntity(Entity entity) { return false; }
    public static boolean isImmersiveAircraftEntity(Entity entity) { return false; }
    public static boolean isMaidItem(Item item) { return false; }
    public static String getMaidEntityId(Entity entity) { return StringPool.EMPTY; }
    public static boolean isMaidSitting(LivingEntity livingEntity) { return false; }
    public static void registerMaidAnimStates(TLMBinding tlmBinding) {}
    public static Object buildControllers(PlayerModelBundle bundle, ModelResourceBundle resBundle) { return null; }
    public static void init() {}
    public static MaidEntityRenderer getMaidModelProvider() { return null; }
    public static boolean isMaidChatAvailable() { return false; }
    public static void openMaidChat() {}
    @Nullable public static PlayState handleMaidInteraction(AnimationEvent<LivingAnimatable<?>> event, LivingEntity living, Entity entity) { return null; }
    public static void syncMaidState(LivingEntity livingEntity) {}
}
