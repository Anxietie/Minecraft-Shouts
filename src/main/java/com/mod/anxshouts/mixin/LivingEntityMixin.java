package com.mod.anxshouts.mixin;

import com.mod.anxshouts.registry.StatusEffectRegister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Unique
	private final LivingEntity This = (LivingEntity)((Object) this);

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (source.isIn(DamageTypeTags.IS_FREEZING) && This.hasStatusEffect(StatusEffectRegister.FROST_RESISTANCE))
			cir.setReturnValue(false);
	}
}
