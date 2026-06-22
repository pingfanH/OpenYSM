package com.elfmcys.yesstevemodel.client.renderer.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class SophisticatedBackpackLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public SophisticatedBackpackLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }
    @Override
    public void render(com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {}
}
