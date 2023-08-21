package com.mod.anxshouts.networking;

import com.mod.anxshouts.networking.packet.ActionShoutC2SPacket;
import com.mod.anxshouts.networking.packet.ObtainShoutC2SPacket;
import com.mod.anxshouts.networking.packet.SelectShoutC2SPacket;
import com.mod.anxshouts.networking.packet.SetCooldownShoutC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ModPackets {
    public static final Identifier ACTION_SHOUT_ID = new Identifier(MODID, "shout_action");
    public static final Identifier SELECT_SHOUT_ID = new Identifier(MODID, "shout_select");
    public static final Identifier OBTAIN_SHOUT_ID = new Identifier(MODID, "shout_obtain");
    public static final Identifier SET_COOLDOWN_SHOUT_ID = new Identifier(MODID, "shout_cooldown_set");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ACTION_SHOUT_ID, ActionShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SELECT_SHOUT_ID, SelectShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(OBTAIN_SHOUT_ID, ObtainShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SET_COOLDOWN_SHOUT_ID, SetCooldownShoutC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}