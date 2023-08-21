package com.mod.anxshouts.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public final class ModUtils {
    public static double convertYaw(float yaw) {
        return Math.toRadians((yaw % 360 + 360) % 360);
    }

    public static double getHorizontalDistanceFromPlayer(Entity e, PlayerEntity player) {
        double xComponent = e.getX() - player.getX();
        double yComponent = e.getZ() - player.getZ();
        return Math.sqrt(xComponent * xComponent + yComponent * yComponent);
    }

    public static double getHorizontalDistanceFromOrigin(PlayerEntity player) {
        return Math.sqrt(player.getX() * player.getX() + player.getZ() * player.getZ());
    }

    // creates rotated absolute value function based on players yaw and returns whether the position of the entity is inside it
    // x-axis = z position
    // y-axis = x position
    // (yaw is at 0 when at positive z)
    public static double inHorizontalFOV(double y1, double x1, double y2, double x2, double yaw) {
        // normal absolute value function centered at player and rotated based on yaw
        return Math.abs(((x2 - x1) * Math.cos(yaw) - (y2 - y1) * Math.sin(yaw))) - ((x2 - x1) * Math.sin(yaw) + (y2 - y1) * Math.cos(yaw));
    }

    // does same as inHorizontalFOV
    // x-axis = distance from origin
    // y-axis = entity y value
    public static double inVerticalFOV(double y1, double x1, double y2, double x2, double pitch) {
        return Math.abs(((x2 - x1) * Math.cos(pitch) - (y2 - y1) * Math.sin(pitch))) - ((x2 - x1) * Math.sin(pitch) + (y2 - y1) * Math.cos(pitch));
    }
}
