package com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability;

import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilities;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MaidCapabilityProvider {

    public static MaidCapability getOrCreate(EntityMaid entityMaid) {
        MaidCapability capability = entityMaid.getData(MaidCapabilities.MAID_CAP.get());
        if (capability == null) {
            capability = new MaidCapability(entityMaid, true);
            entityMaid.setData(MaidCapabilities.MAID_CAP.get(), capability);
        }
        return capability;
    }
}
