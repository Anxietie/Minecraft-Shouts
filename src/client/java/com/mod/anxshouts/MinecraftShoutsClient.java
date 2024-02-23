package com.mod.anxshouts;

import com.mod.anxshouts.events.AdvancementMadeEvent;
import com.mod.anxshouts.networking.ClientModPackets;
import com.mod.anxshouts.registry.BlockRegister;
import com.mod.anxshouts.registry.CommandRegister;
import com.mod.anxshouts.registry.KeybindRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

import static com.mojang.text2speech.Narrator.LOGGER;

@Environment(EnvType.CLIENT)
public class MinecraftShoutsClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        KeybindRegister.registerKeybindings();
        LOGGER.info("[anxshouts] keybindings registered");

        CommandRegister.commandCallback();
        LOGGER.info("[anxshouts] commands registered");

        ShoutHandler.keyCallback();
        LOGGER.info("[anxshouts] callback registered");

        ClientModPackets.registerS2CPackets();
        LOGGER.info("[anxshouts] server-to-client packets initialized");

        AdvancementMadeEvent.registerAdvancements();
        LOGGER.info("[anxshouts] advancement made event registered");

        // BlockRegister.WORDS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegister.WORDS.toArray(Block[]::new));
    }
}
