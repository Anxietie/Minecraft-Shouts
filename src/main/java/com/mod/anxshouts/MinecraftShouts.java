package com.mod.anxshouts;

import com.mod.anxshouts.events.AdvancementMadeEvent;
import com.mod.anxshouts.events.EntityDeathEvent;
import com.mod.anxshouts.events.ServerTickEvent;
import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.registry.BlockRegister;
import com.mod.anxshouts.registry.ItemRegister;
import com.mod.anxshouts.registry.SoundRegister;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftShouts implements ModInitializer {
    public static final String MODID = "anxshouts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        LOGGER.info("Shouts initializing");

        ModPackets.registerC2SPackets();
        LOGGER.info("Shouts client-to-server packets initialized");

        SoundRegister.registerSounds();
        LOGGER.info("Shouts sounds registered");

        ServerTickEvent.registerServerTicks();
        EntityDeathEvent.registerEntityDeaths();
        AdvancementMadeEvent.registerAdvancements();
        LOGGER.info("Shouts events registered");

        BlockRegister.registerBlocks();
        LOGGER.info("Shouts blocks registered");

        ItemRegister.registerItems();
        ItemRegister.registerItemGroups();
        LOGGER.info("Shouts items registered");
    }
}
