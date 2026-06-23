package com.elfmcys.yesstevemodel.client.gui.button;

import net.minecraft.network.chat.Component;

public class ConfigCheckBoxForge extends ConfigCheckBox {

    public ConfigCheckBoxForge(int x, int y, String str, boolean booleanValue) {
        super(x, y, 400, Component.translatable("gui.yes_steve_model.config." + str), (v) -> {});
    }

    @Override
    public void onPress() {
        super.onPress();
    }
}
