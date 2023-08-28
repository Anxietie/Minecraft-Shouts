package com.mod.anxshouts.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class SoundRegister {
    public static final SoundEvent UNRELENTING_FORCE = SoundEvent.of(new Identifier(MODID, "unrelenting_force"));
    public static final SoundEvent FIRE_BREATH = SoundEvent.of(new Identifier(MODID, "fire_breath"));
    public static final SoundEvent FROST_BREATH = SoundEvent.of(new Identifier(MODID, "frost_breath"));
    public static final SoundEvent AURA_WHISPER = SoundEvent.of(new Identifier(MODID, "aura_whisper"));
    public static final SoundEvent STORM_CALL = SoundEvent.of(new Identifier(MODID, "storm_call"));
    public static final SoundEvent ABSORB_SOUL = SoundEvent.of(new Identifier(MODID, "absorb_soul"));

    public static void registerSounds() {
        register("unrelenting_force", UNRELENTING_FORCE);
        register("fire_breath", FIRE_BREATH);
        register("frost_breath", FROST_BREATH);
        register("aura_whisper", AURA_WHISPER);
        register("storm_call", STORM_CALL);
        register("absorb_soul", ABSORB_SOUL);
    }

    private static SoundEvent register(String id, SoundEvent soundEvent) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(MODID, id), soundEvent);
    }
}
