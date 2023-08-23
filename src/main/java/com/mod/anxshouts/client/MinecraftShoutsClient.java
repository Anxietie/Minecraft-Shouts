package com.mod.anxshouts.client;

import com.mod.anxshouts.client.registry.KeybindRegister;
import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.registry.CommandRegister;
import net.fabricmc.api.ClientModInitializer;

import static com.mojang.text2speech.Narrator.LOGGER;

public class MinecraftShoutsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        KeybindRegister.registerKeybindings();
        LOGGER.info("Shouts keybindings registered");

        CommandRegister.commandCallback();
        LOGGER.info("Shouts commands registered");

        ShoutHandler.keyCallback();
        LOGGER.info("Shouts callback registered");

        ModPackets.registerS2CPackets();
        LOGGER.info("Shouts server-to-client packets initialized");
    }
}
