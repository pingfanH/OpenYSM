package com.elfmcys.yesstevemodel.client.compat.curios;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.List;
public class CuriosItemGetter {
    public static List<ItemStack> getCuriosItems(LivingEntity entity) { return List.of(); }
    public static boolean hasCuriosItem(LivingEntity entity, String type) { return false; }
}
