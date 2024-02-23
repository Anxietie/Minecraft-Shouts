package com.mod.anxshouts.networking;

import com.mod.anxshouts.networking.packet.KnockbackS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientModPackets {
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(ModPackets.KNOCKBACK_ID, KnockbackS2CPacket::receive);
	}
}
