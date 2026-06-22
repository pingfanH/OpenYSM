package com.elfmcys.yesstevemodel;

import com.elfmcys.yesstevemodel.capability.Capabilities;
import com.elfmcys.yesstevemodel.capability.ClientCapabilities;
import com.elfmcys.yesstevemodel.config.GeneralConfig;
import com.elfmcys.yesstevemodel.config.ModSoundEvents;
import com.elfmcys.yesstevemodel.config.ServerConfig;
import com.elfmcys.yesstevemodel.util.obfuscate.Keep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * TODO:
 * 默认模型应该就在模组架加载的时候就预加载了
 * 其它模型统统都是进入世界后加载
 */
@Mod(YesSteveModel.MOD_ID)
public class YesSteveModel {

    public static final String MOD_ID = "yes_steve_model";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public YesSteveModel() throws IOException {
        NativeLibLoader.init();
        if (!NativeLibLoader.isAvailable()) {
            LOGGER.error(getErrorMessage());
        } else {
            initConfig();
        }
        Capabilities.ATTACHMENT_TYPES.register(ModLoadingContext.get().getActiveContainer().getEventBus());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientCapabilities.CLIENT_ATTACHMENT_TYPES.register(ModLoadingContext.get().getActiveContainer().getEventBus());
            // Register MaidCapabilities if TouhouLittleMaid is loaded
            if (ModList.get() != null && ModList.get().isLoaded("touhou_little_maid")) {
                com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilities.MAID_ATTACHMENT_TYPES
                        .register(ModLoadingContext.get().getActiveContainer().getEventBus());
            }
        }
    }

    @SuppressWarnings({"deprecation", "removal"})
    private static void initConfig() {
        File oldConfig = FMLPaths.CONFIGDIR.get().resolve("yes_steve_model-common.toml").toFile();
        if (oldConfig.isFile()) {
            File file2 = FMLPaths.CONFIGDIR.get().resolve("yes_steve_model-client.toml").toFile();
            if (!file2.isFile()) {
                oldConfig.renameTo(file2);
            } else {
                oldConfig.delete();
            }
        }
        // FIXME: ModLoadingContext.registerConfig removed in NeoForge 1.21.1
        // ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, GeneralConfig.buildSpec());
        // FIXME: ModLoadingContext.registerConfig removed in NeoForge 1.21.1
        // ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.buildSpec());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModSoundEvents.REGISTER.register(ModLoadingContext.get().getActiveContainer().getEventBus());
        }
    }

    @Keep
    public static boolean isAvailable() {
        return NativeLibLoader.isAvailable();
    }

    public static boolean isOnAndroid() {
        return NativeLibLoader.isOnAndroid();
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendUnavailableMessage() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            localPlayer.sendSystemMessage(getUnavailableComponent());
        }
    }

    public static Component getLoadingWarning() {
        return NativeLibLoader.createLoadingWarning();
    }

    public static Component getUnavailableComponent() {
        return NativeLibLoader.getErrorComponent();
    }

    public static String getErrorMessage() {
        return NativeLibLoader.getErrorMessage();
    }
}