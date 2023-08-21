package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;

public class ServerTickEvent {
    public static void registerServerTicks() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (IShout.KEY.get(player).getShoutCooldown() > 0)
                    IShout.KEY.get(player).decrementShoutCooldown();
            }
        });
    }
}
