package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class LivingEntityDamageEvent {
	public static void registerLivingEntityDamage() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (!(entity instanceof ServerPlayerEntity)) return true;
			IShout data = IShout.KEY.get(entity);
			return !data.isEthereal();
		});
	}
}
