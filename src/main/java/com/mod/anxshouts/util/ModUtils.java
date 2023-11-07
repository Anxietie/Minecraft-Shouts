package com.mod.anxshouts.util;

import com.mod.anxshouts.components.IShout;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public final class ModUtils {
    public static double convertYaw(float yaw) {
        return Math.toRadians((yaw % 360 + 360) % 360); // converts yaw to [0, 2pi)
    }

    public static void killWolfSoldiers(MinecraftServer server, PlayerEntity player) {
        for (ServerWorld world : server.getWorlds()) {
            WolfEntity wolf = (WolfEntity) world.getEntity(IShout.KEY.get(player).getValorUUID());
            if (wolf != null)
                wolf.discard();
        }
    }
}
