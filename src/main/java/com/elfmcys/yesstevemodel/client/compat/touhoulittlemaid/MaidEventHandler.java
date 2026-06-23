package com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid;
import net.neoforged.neoforge.common.NeoForge;
public class MaidEventHandler {
    public static void init() { NeoForge.EVENT_BUS.register(new Object()); }

    public static boolean isMaid(Object entity) { return false; }

    public static void registerMaidRenderer() {}
}