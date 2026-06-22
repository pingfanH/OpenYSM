package com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability;

import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilities;

import com.elfmcys.yesstevemodel.YesSteveModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class MaidCapabilities {

    public static final DeferredRegister<AttachmentType<?>> MAID_ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, YesSteveModel.MOD_ID);

    @SuppressWarnings("unchecked")
    public static final Supplier<AttachmentType<MaidCapability>> MAID_CAP =
            MAID_ATTACHMENT_TYPES.register("maid_cap", () -> AttachmentType.builder(() -> null).build());
}
