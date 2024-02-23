package com.mod.anxshouts.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ShoutSelectionScreen extends CottonClientScreen {
    public ShoutSelectionScreen(GuiDescription description) {
        super(description);
    }
}
