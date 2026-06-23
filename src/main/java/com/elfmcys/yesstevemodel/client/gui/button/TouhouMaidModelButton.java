package com.elfmcys.yesstevemodel.client.gui.button;

import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import com.elfmcys.yesstevemodel.client.compat.touhoulittlemaid.capability.MaidCapabilities;
import com.elfmcys.yesstevemodel.client.entity.PlayerPreviewEntity;
import com.elfmcys.yesstevemodel.client.model.ModelAssembly;
import com.elfmcys.yesstevemodel.util.ComponentUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import immersive_aircraft.cobalt.network.NetworkHandler;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class TouhouMaidModelButton extends ModelButton {

    private final EntityMaid maid;

    public TouhouMaidModelButton(int x, int y, boolean isAuthLocked, PlayerPreviewEntity previewEntity, ModelAssembly modelAssembly, EntityMaid entityMaid) {
        super(x, y, isAuthLocked, previewEntity, modelAssembly);
        this.maid = entityMaid;
    }

    @Override
    public void onPress() {
        if (this.isStarred) {
            return;
        }
        Component component = ComponentUtil.getDisplayName(this.renderContext, this.modelIdHolder.getModelId());
        Optional.ofNullable(this.maid.getData(MaidCapabilities.MAID_CAP.get())).ifPresent(cap -> {
            cap.setYsmModel(this.modelIdHolder.getModelId(), this.modelIdHolder.getCurrentTextureName());
            NetworkHandler.sendToServer(null);
        });
    }
}