package com.elfmcys.yesstevemodel.client.compat.curios;
import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
public class CuriosCompat { 
    public static void init() {} 
    public static boolean isLoaded() { return false; } 
    public static boolean hasNoTaggedItemInSlot(Object entity, String type, List<TagKey<Item>> tags) { return false; }
    public static boolean hasTaggedItemInSlot(Object entity, String type, List<TagKey<Item>> tags) { return false; }
    public static int getCuriosCount(Object entity, String type) { return 0; }
}
