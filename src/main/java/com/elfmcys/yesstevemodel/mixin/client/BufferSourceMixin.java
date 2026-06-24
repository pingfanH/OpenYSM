package com.elfmcys.yesstevemodel.mixin.client;

import com.elfmcys.yesstevemodel.util.accessors.BufferSourceAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({MultiBufferSource.BufferSource.class})
public class BufferSourceMixin implements BufferSourceAccessor {

    @Override
    @Unique
    public void initialize() {
        ((MultiBufferSource.BufferSource) (Object) this).endBatch();
    }
}
