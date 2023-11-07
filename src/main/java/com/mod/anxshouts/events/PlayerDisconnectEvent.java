package com.mod.anxshouts.events;

import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerDisconnectEvent {
	public static void registerDisconnectEvents() {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			for (PlayerEntity player : server.getPlayerManager().getPlayerList())
				ModUtils.killWolfSoldiers(server, player);
		});
	}
}
