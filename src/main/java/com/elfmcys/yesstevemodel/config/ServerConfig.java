package com.elfmcys.yesstevemodel.config;

import com.google.common.collect.Lists;

import java.util.List;

public class ServerConfig {

    public static int THREAD_COUNT = 0;

    public static int BANDWIDTH_LIMIT = 5;

    public static int PLAYER_SYNC_TIMEOUT = 0;

    public static boolean LOW_BANDWIDTH_USAGE = false;

    public static boolean CAN_SWITCH_MODEL = true;

    public static String DEFAULT_MODEL_ID = "default";

    public static String DEFAULT_MODEL_TEXTURE = "default";

    public static int ACCEPT_SOUND_FX = 0;

    public static List<String> CLIENT_NOT_DISPLAY_MODELS = Lists.newArrayList();

    public static void buildSpec() {}

    private static void defineOptions() {}
}
