package com.elfmcys.yesstevemodel.config;

public class LoadingStateConfig {

    public static boolean DISABLE_LOADING_STATE_SCREEN = false;

    public static Position LOADING_STATE_POSITION = Position.TOP_CENTER;

    public enum Position {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    public static void define() {}
}
