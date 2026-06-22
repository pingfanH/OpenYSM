package com.elfmcys.yesstevemodel.client.compat.sbackpack;
import net.minecraft.world.item.ItemStack;
public class SBackpackCompat { 
    public static void init() {} 
    public static boolean isLoaded() { return false; } 
    public static void setupRenderLayers() {}
    public static ItemStack getBackpackItem(Object context) { return null; }
    public static Object getInCompatibleInfo() { return java.util.Optional.empty(); }
}
