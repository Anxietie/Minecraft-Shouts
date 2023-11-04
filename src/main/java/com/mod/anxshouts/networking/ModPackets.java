package com.mod.anxshouts.networking;

import com.mod.anxshouts.networking.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ModPackets {
    public static final Identifier ACTION_SHOUT_ID = new Identifier(MODID, "shout_action");
    public static final Identifier SELECT_SHOUT_ID = new Identifier(MODID, "shout_select");
    public static final Identifier OBTAIN_SHOUT_ID = new Identifier(MODID, "shout_obtain");
    public static final Identifier UNLOCK_SHOUT_ID = new Identifier(MODID, "shout_unlock");
    public static final Identifier SET_COOLDOWN_SHOUT_ID = new Identifier(MODID, "shout_cooldown_set");
    public static final Identifier SOUL_COUNT_ID = new Identifier(MODID, "soul_count");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(ACTION_SHOUT_ID, ActionShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SELECT_SHOUT_ID, SelectShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(OBTAIN_SHOUT_ID, ObtainShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(UNLOCK_SHOUT_ID, UnlockShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SET_COOLDOWN_SHOUT_ID, SetCooldownShoutC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SOUL_COUNT_ID, SoulCountC2SPacket::receive);
    }

    public static void registerS2CPackets() {}
}
