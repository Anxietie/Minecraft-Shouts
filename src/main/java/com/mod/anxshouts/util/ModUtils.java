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
}
