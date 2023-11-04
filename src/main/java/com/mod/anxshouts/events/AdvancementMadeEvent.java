package com.mod.anxshouts.events;

import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class AdvancementMadeEvent {
    public static void registerAdvancements() {
        AdvancementMadeCallback.EVENT.register((player, advancement) -> {
            player.sendMessage(Text.literal("u got " + advancement.getId().getPath()));
            return ActionResult.PASS;
        });
    }
}
