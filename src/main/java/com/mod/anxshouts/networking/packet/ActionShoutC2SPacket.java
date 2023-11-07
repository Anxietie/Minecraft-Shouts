package com.mod.anxshouts.networking.packet;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.registry.SoundRegister;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;

public class ActionShoutC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		IShout data = IShout.KEY.get(player);
		ShoutHandler.Shout shout = ShoutHandler.Shout.fromOrdinal(data.getSelectedShout());

		handleShout(player, shout);

		player.sendMessage(Text.translatable("anxshouts.shouts." + shout.getId()), true);
		data.setShoutCooldown(1000 * shout.getCost());
	}

	public static double getHorizontalDistanceFromPlayer(Entity e, PlayerEntity player) {
		double xComponent = e.getX() - player.getX();
		double yComponent = e.getZ() - player.getZ();
		return Math.sqrt(xComponent * xComponent + yComponent * yComponent);
	}

	public static double getHorizontalDistanceFromOrigin(Entity e) {
		return Math.sqrt(e.getX() * e.getX() + e.getZ() * e.getZ());
	}

	// creates rotated absolute value function based on players yaw and returns whether the position of the entity is inside it
	// x-axis = z position
	// y-axis = x position
	// (yaw is at 0 when at positive z)
	public static double inHorizontalRange(double y1, double x1, double y2, double x2, double yaw) {
		final int ANGLE_SCALE = 1; // the lower this is, the wider the fov, and vice versa

		// normal absolute value function centered at player and rotated based on yaw
		return Math.abs(ANGLE_SCALE * ((x2 - x1) * Math.cos(yaw) - (y2 - y1) * Math.sin(yaw))) - ((x2 - x1) * Math.sin(yaw) + (y2 - y1) * Math.cos(yaw));
	}

	// does same as inHorizontalFOV
	// x-axis = distance from origin
	// y-axis = entity y value
	public static double inVerticalRange(double y1, double x1, double y2, double x2, double pitch) {
		final int ANGLE_SCALE = 1; // the lower this is, the wider the fov, and vice versa

		return Math.abs(ANGLE_SCALE * ((x2 - x1) * Math.cos(pitch) - (y2 - y1) * Math.sin(pitch))) - ((x2 - x1) * Math.sin(pitch) + (y2 - y1) * Math.cos(pitch));
	}

	private static List<Entity> getFacedMobs(ServerPlayerEntity player) throws ConcurrentModificationException {
		float yaw = player.getHeadYaw();
		float pitch = player.getPitch();

		Box box = player.getBoundingBox().expand(4.0);

		return player.getWorld().getOtherEntities(player, box, (entity) -> {
			double distFromOrigin = getHorizontalDistanceFromOrigin(player);
			double distFromPlayer = getHorizontalDistanceFromPlayer(entity, player);
			return entity instanceof LivingEntity
					&& inHorizontalRange(player.getX(), player.getZ(), entity.getX(), entity.getZ(), ModUtils.convertYaw(yaw) + (Math.PI/2)) <= 0
					&& inVerticalRange(player.getEyeY(), distFromOrigin, entity.getEyeY(), distFromOrigin + distFromPlayer, Math.toRadians(pitch + 90)) <= 0
					&& player.canSee(entity);
		});
	}

	private static ItemEntity dropStack(Entity e, ItemStack stack, int yOffset) {
		if (stack.isEmpty())
			return null;
		if (e.getWorld().isClient)
			return null;
		ItemEntity itemEntity = new ItemEntity(e.getWorld(), e.getX(), e.getY() + (double)yOffset, e.getZ(), stack);
		itemEntity.setToDefaultPickupDelay();
		e.getWorld().spawnEntity(itemEntity);
		return itemEntity;
	}

	private static void handleShout(ServerPlayerEntity player, ShoutHandler.Shout shout) {
		float yaw = player.getHeadYaw();

		List<Entity> entities;
		try {
			entities = getFacedMobs(player);
		}
		catch (ConcurrentModificationException e) {
			player.sendMessage(Text.literal("Slow down!"), true);
			return;
		}

		switch(shout) {
			case FIRE -> {
				for (Entity e : entities) {
					if (!e.isFireImmune() && e.getFireTicks() <= 0)
						e.setOnFireFor(8);
				}
				player.playSound(SoundRegister.FIRE_BREATH, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case FROST -> {
				for (Entity e : entities) {
					e.setFrozenTicks(e.getMinFreezeDamageTicks() + 200);
					((LivingEntity) e).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1, false, false));
				}
				player.playSound(SoundRegister.FROST_BREATH, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case FORCE -> {
				for (Entity e : entities) {
					double strength = 2.0;
					double x = strength*Math.cos(ModUtils.convertYaw(yaw) - (Math.PI/2));
					double z = strength*Math.sin(ModUtils.convertYaw(yaw) - (Math.PI/2));
					e.damage(player.getWorld().getDamageSources().playerAttack(player), 0.5f);
					((LivingEntity) e).takeKnockback(strength, x, z);
				}
				player.playSound(SoundRegister.UNRELENTING_FORCE, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case AURA -> {
				for (Entity e : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(20.0), LivingEntity.class::isInstance))
					((LivingEntity) e).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 450, 1, true, true));
				player.playSound(SoundRegister.AURA_WHISPER, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case STORM -> {
				for(Entity e : entities) {
					LightningEntity lightning;
					if ((lightning = EntityType.LIGHTNING_BOLT.create(player.getWorld())) != null) {
						Vec3d pos = e.getPos();
						lightning.setPos(pos.getX(), pos.getY(), pos.getZ());
						player.getWorld().spawnEntity(lightning);
					}
				}
				player.playSound(SoundRegister.STORM_CALL, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case CLEAR -> {
				player.getServerWorld().setWeather(1200, 0, false, false); // 1200 seconds is 1 minecraft day
				player.playSound(SoundRegister.UNRELENTING_FORCE, SoundCategory.PLAYERS, 0.5f, 1.0f); // unrelenting force is generic shout sound
			}
			case DISARM -> {
				for (Entity e : entities) {
					if (((LivingEntity) e).getMainHandStack().isEmpty()) continue;
					dropStack(e, ((LivingEntity) e).getMainHandStack().copyAndEmpty(), 2);
					((ServerWorld) e.getWorld()).spawnParticles(ParticleTypes.ANGRY_VILLAGER, e.getX(), e.getEyeY(), e.getZ(), 1, 0, 1, 0, 0.2);
				}
				player.playSound(SoundRegister.DISARM, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case ETHEREAL -> {
				final int DURATION = 360; // 360 ticks = 18 seconds which is how long the actual shout lasts in skyrim once the full shout is learned
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, DURATION, 1, false, false, false));
				player.playSound(SoundRegister.BECOME_ETHEREAL, SoundCategory.PLAYERS, 0.5f, 1.0f);
				IShout.KEY.get(player).setEtherealTicks(DURATION);
			}
			case DRAIN -> {
				int l = entities.size();
				for (Entity e : entities) {
					e.damage(e.getDamageSources().playerAttack(player), 2.0f/l);
					((ServerWorld) e.getWorld()).spawnParticles(ParticleTypes.HEART, e.getX(), e.getEyeY(), e.getZ(), 1, 0, 1, 0, 0.2);
					player.heal(2.0f);
				}
				player.playSound(SoundRegister.DRAIN_VITALITY, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case ICE -> {
				for (Entity e: entities) {
					e.setFrozenTicks(e.getMinFreezeDamageTicks() + 1200); // 1200 ticks = 1 minute
					((LivingEntity) e).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 255, false, false), player);
				}
				player.playSound(SoundRegister.ICE_FORM, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case SPRINT -> {
				double strength = 2.0;
				double x = strength*Math.cos(ModUtils.convertYaw(yaw) - (Math.PI/2));
				double z = strength*Math.sin(ModUtils.convertYaw(yaw) - (Math.PI/2));

				PacketByteBuf buf = PacketByteBufs.create();
				buf.writeDouble(strength);
				buf.writeDouble(x);
				buf.writeDouble(z);
				ServerPlayNetworking.send(player, ModPackets.KNOCKBACK_ID, buf);

				player.playSound(SoundRegister.WHIRLWIND_SPRINT, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case VALOR -> {
				WolfEntity wolf = new WolfEntity(EntityType.WOLF, player.getWorld());
				wolf.setOwner(player);
				wolf.setPos(player.getX(), player.getY() + 1, player.getZ());
				int i = player.getRandom().nextBetween(0, 3);
				String name = switch (i) {
					case 0 -> "Gormlaith Golden-Hilt";
					case 1 -> "Felldir the Old";
					default -> "Hakon One-Eye";
				};
				wolf.setCustomName(Text.literal(name));
				UUID uuid = wolf.getUuid();
				IShout.KEY.get(player).setValorUUID(uuid);
				IShout.KEY.get(player).setValorTicks(1200); // 1200 ticks = 1 minute
				player.getWorld().spawnEntity(wolf);
				((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.EGG_CRACK, player.getX(), player.getY(), player.getZ(), 10, 0, 1, 1, 0.2);
				player.playSound(SoundRegister.CALL_OF_VALOR, SoundCategory.PLAYERS, 0.5f, 1.0f);
			}
			case ASPECT -> {
				player.playSound(SoundRegister.DRAGON_ASPECT, SoundCategory.PLAYERS, 0.5f, 1.0f);

			}
			case REND -> {
				player.playSound(SoundRegister.DRAGONREND, SoundCategory.PLAYERS, 0.5f, 1.0f);

			}
			default -> {}
		}
	}
}
