package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;

public class ServerTickEvent {
    public static void registerServerTicks() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                IShout data = IShout.KEY.get(player);
                if (data.getShoutCooldown() > 0)
                    data.decrementShoutCooldown();
                if (data.isEthereal())
                    data.decrementEtherealTicks();
                if (data.hasActiveValor()) {
                    if (data.decrementValorTicks() == 0) {
                        ModUtils.killWolfSoldiers(server, player);
                        data.setValorUUID(null);
                    }
                }
                if (data.hasActiveDA()) {
                    if (data.decrementDATicks() == 0) {
                        ModUtils.killCompanions(server, player);
                        data.setCompanionUUID(null);
                    }
                }
                if (data.getDACooldown() > 0)
                    data.decrementDACooldown();
            }
        });
    }
}
