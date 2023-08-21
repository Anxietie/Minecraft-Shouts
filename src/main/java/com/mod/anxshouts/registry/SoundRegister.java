package com.mod.anxshouts.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class SoundRegister {
    public static final SoundEvent UNRELENTING_FORCE = SoundEvent.of(new Identifier(MODID, "unrelenting_force"));

    public static void registerSounds() {
        register("unrelenting_force", UNRELENTING_FORCE);
    }

    private static SoundEvent register(String id, SoundEvent soundEvent) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(MODID, id), soundEvent);
    }
}
