package com.mod.anxshouts.util;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public final class ModUtils {
    public static double convertYaw(float yaw) {
        return Math.toRadians((yaw % 360 + 360) % 360); // converts yaw to [0, 2pi)
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

    public static List<Entity> getFacedMobs(ServerPlayerEntity player) throws ConcurrentModificationException {
        float yaw = player.getHeadYaw();
        float pitch = player.getPitch();

        Box box = player.getBoundingBox().expand(4.0);

        return player.getWorld().getOtherEntities(player, box, (entity) -> {
                double distFromOrigin = getHorizontalDistanceFromOrigin(player);
                double distFromPlayer = getHorizontalDistanceFromPlayer(entity, player);
                return entity instanceof LivingEntity
                        && inHorizontalRange(player.getX(), player.getZ(), entity.getX(), entity.getZ(), convertYaw(yaw) + (Math.PI/2)) <= 0
                        && inVerticalRange(player.getEyeY(), distFromOrigin, entity.getEyeY(), distFromOrigin + distFromPlayer, Math.toRadians(pitch + 90)) <= 0
                        && player.canSee(entity);
                });
    }

    public static void emitParticles(ClientPlayerEntity player, int count) {
        ParticleEffect particleEffect = ShoutHandler.Shout.fromOrdinal(IShout.KEY.get(player).getSelectedShout()).getParticleEffect();
        if (particleEffect == null) return;

        Random random = new Random();
        final double SPREAD = 0.6; // affects spread

        double velocityX = Math.cos(ModUtils.convertYaw(player.getHeadYaw()) + (Math.PI/2));
        double velocityZ = Math.sin(ModUtils.convertYaw(player.getHeadYaw()) + (Math.PI/2));
        double velocityY = Math.sin(Math.toRadians(-player.getPitch()));
        for (int i = 0; i < count; i++)
            // (SPREAD * (2 * random.nextDouble() - 1)) makes the particles deviate off their path by a value between -1 and 1
            player.getWorld().addParticle(particleEffect, player.getX(), player.getEyeY(), player.getZ(), velocityX + (SPREAD * (2 * random.nextDouble() - 1)), velocityY + (SPREAD * (2 * random.nextDouble() - 1)), velocityZ + (SPREAD * (2 * random.nextDouble() - 1)));
    }
}
