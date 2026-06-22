package com.elfmcys.yesstevemodel.client.renderer;

import java.util.Optional;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.projectile.Projectile;

public class CustomProjectileRenderer {
    public static boolean renderProjectile(Projectile projectile, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        return Optional.ofNullable(projectile.getData(ClientCapabilities.PROJECTILE_CAP.get())).map(cap -> {
            if (cap.isModelInitialized() && cap.isModelReady()) {
                RendererManager.getProjectileRenderer().render(cap, entityYaw, partialTick, poseStack, multiBufferSource, packedLight);
                return false;
            }
            return true;
        }).orElse(true);
    }
}