package com.mod.anxshouts;

import com.mod.anxshouts.events.*;
import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.registry.BlockRegister;
import com.mod.anxshouts.registry.ItemRegister;
import com.mod.anxshouts.registry.StatusEffectRegister;
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
        LOGGER.info("[anxshouts] initializing");

        ModPackets.registerC2SPackets();
        LOGGER.info("[anxshouts] client-to-server packets initialized");

        ServerTickEvent.registerServerTicks();
        EntityDeathEvent.registerEntityDeaths();
        // AdvancementMadeEvent.registerAdvancements();
        AttackEntityEvent.registerEntityAttacks();
        LivingEntityDamageEvent.registerLivingEntityDamage();
        PlayerDisconnectEvent.registerDisconnectEvents();
        LOGGER.info("[anxshouts] events registered");

        BlockRegister.registerBlocks();
        LOGGER.info("[anxshouts] blocks registered");

        ItemRegister.registerItems();
        ItemRegister.registerItemGroups();
        LOGGER.info("[anxshouts] items registered");

        StatusEffectRegister.registerStatusEffects();
        LOGGER.info("[anxshouts] status effects registered");
    }
}
