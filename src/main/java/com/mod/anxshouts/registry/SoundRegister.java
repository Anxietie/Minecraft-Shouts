package com.mod.anxshouts.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class SoundRegister {
    public static final SoundEvent UNRELENTING_FORCE = SoundEvent.of(new Identifier(MODID, "force"));
    public static final SoundEvent FIRE_BREATH = SoundEvent.of(new Identifier(MODID, "fire"));
    public static final SoundEvent FROST_BREATH = SoundEvent.of(new Identifier(MODID, "frost"));
    public static final SoundEvent AURA_WHISPER = SoundEvent.of(new Identifier(MODID, "aura"));
    public static final SoundEvent STORM_CALL = SoundEvent.of(new Identifier(MODID, "storm"));
    public static final SoundEvent DISARM = SoundEvent.of(new Identifier(MODID, "disarm"));
    public static final SoundEvent ABSORB_SOUL = SoundEvent.of(new Identifier(MODID, "absorb_soul"));

    public static void registerSounds() {
        register("force", UNRELENTING_FORCE);
        register("fire", FIRE_BREATH);
        register("frost", FROST_BREATH);
        register("aura", AURA_WHISPER);
        register("storm", STORM_CALL);
        register("disarm", DISARM);
        register("absorb_soul", ABSORB_SOUL);
    }

    private static SoundEvent register(String id, SoundEvent soundEvent) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(MODID, id), soundEvent);
    }
}
