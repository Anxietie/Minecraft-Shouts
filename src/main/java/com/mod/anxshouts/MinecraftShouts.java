package com.mod.anxshouts;

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

    }
}
