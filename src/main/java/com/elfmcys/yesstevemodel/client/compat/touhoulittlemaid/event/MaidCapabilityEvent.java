package com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.event;

import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MaidCapabilityEvent {

    public static void register() {
        // Attachment types are registered via DeferredRegister in MaidCapabilities
        // No need for AttachCapabilitiesEvent in NeoForge
    }
}
