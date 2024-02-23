package com.mod.anxshouts.mixin.client;

import com.mod.anxshouts.events.AdvancementMadeCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientAdvancementManager.class)
public abstract class ClientAdvancementManagerMixin {
    @Shadow
    private @Final MinecraftClient client;

    @ModifyVariable(method = "onAdvancements", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/Advancement;getDisplay()Lnet/minecraft/advancement/AdvancementDisplay;"))
    private Advancement onAdvancements(Advancement advancement) {
        AdvancementMadeCallback.EVENT.invoker().execute(client.player, advancement);
        return advancement;
    }
}
