package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;

public class AttackEntityEvent {
	public static void registerEntityAttacks() {
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (IShout.KEY.get(player).isEthereal())
				return ActionResult.FAIL;
			return ActionResult.PASS;
		});
	}
}
