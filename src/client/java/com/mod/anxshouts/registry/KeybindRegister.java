package com.mod.anxshouts.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeybindRegister {
    public static final KeyBinding SHOUT_KEY = new KeyBinding(
            "key.anxshouts.shout_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.anxshouts.shout_controls"
    );

    public static final KeyBinding SHOUT_GUI_OPEN_KEY = new KeyBinding(
            "key.anxshouts.gui_open_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.anxshouts.shout_controls"
    );

    public static void registerKeybindings() {
        register(SHOUT_KEY);
        register(SHOUT_GUI_OPEN_KEY);
    }

    private static KeyBinding register(KeyBinding keybind) {
        return KeyBindingHelper.registerKeyBinding(keybind);
    }
}
