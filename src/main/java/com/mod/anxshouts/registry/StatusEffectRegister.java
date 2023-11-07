package com.mod.anxshouts.registry;

import com.mod.anxshouts.mixin.StatusEffectMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class StatusEffectRegister {
	public static final StatusEffect FROST_RESISTANCE = StatusEffectMixin.invokeInit(StatusEffectCategory.BENEFICIAL, 0x38DBFF);

	private static StatusEffect register(String id, StatusEffect entry) {
		return Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, id), entry);
	}

	public static void registerStatusEffects() {
		register("frost_resistance", FROST_RESISTANCE);
	}
}
