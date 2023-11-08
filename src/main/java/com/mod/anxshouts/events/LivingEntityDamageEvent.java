package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class LivingEntityDamageEvent {
	public static void registerLivingEntityDamage() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (!(entity instanceof ServerPlayerEntity)) return true;
			IShout data = IShout.KEY.get(entity);
			if (data.isEthereal()) return false;
			if (entity.getHealth() - amount <= 10.0 && data.hasActiveDA() && source.isOf(DamageTypes.MOB_ATTACK) && !data.companionSummoned()) {
				IronGolemEntity ig = new IronGolemEntity(EntityType.IRON_GOLEM, entity.getWorld());
				ig.setPos(entity.getX(), entity.getY() + 1, entity.getZ());
				ig.setCustomName(Text.literal("Ancient Dragonborn"));
				UUID uuid = ig.getUuid();
				data.setCompanionUUID(uuid);
				entity.getWorld().spawnEntity(ig);
			}
			return true;
		});
	}
}
