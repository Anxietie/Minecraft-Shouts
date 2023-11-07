package com.mod.anxshouts.mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StatusEffect.class)
public interface StatusEffectMixin {
	@Invoker("<init>")
	static StatusEffect invokeInit(StatusEffectCategory category, int color) {
		throw new AssertionError();
	}
}
