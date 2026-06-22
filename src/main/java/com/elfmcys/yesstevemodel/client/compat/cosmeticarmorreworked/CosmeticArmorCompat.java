package com.elfmcys.yesstevemodel.client.compat.cosmeticarmorreworked;

import net.neoforged.fml.ModList;

public class CosmeticArmorCompat {
    private static boolean IS_LOADED = false;
    public static void init() {
        if (ModList.get().isLoaded("cosmeticarmorreworked")) {
            IS_LOADED = true;
        }
    }
    public static boolean isLoaded() { return IS_LOADED; }
    public static boolean startWithArmor() { return false; }
}
