package com.mod.anxshouts.networking.packet;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.registry.SoundRegister;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ConcurrentModificationException;
import java.util.List;

public class ActionShoutC2SPacket {
    // TODO: add more shouts
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IShout data = IShout.KEY.get(player);
        ShoutHandler.Shout shout = ShoutHandler.Shout.fromOrdinal(data.getSelectedShout());

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
                    ((LivingEntity) e).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2, false, false));
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
            default -> {}
        }
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
}
