package com.mod.anxshouts.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface AdvancementMadeCallback {
    Event<AdvancementMadeCallback> EVENT = EventFactory.createArrayBacked(AdvancementMadeCallback.class,
            (listeners) -> (player, advancement) -> {
                for (AdvancementMadeCallback listener : listeners) {
                    listener.execute(player, advancement);
                }

                return ActionResult.PASS;
            });

    ActionResult execute(PlayerEntity player, Advancement advancement);
}
