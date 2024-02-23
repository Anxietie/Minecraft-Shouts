package com.mod.anxshouts.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class KnockbackS2CPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		double strength = buf.readDouble();
		double x = buf.readDouble();
		double z = buf.readDouble();
		if (client.player != null)
			client.player.takeKnockback(strength, x, z);
	}
}
