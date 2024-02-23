package com.mod.anxshouts.events;

import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.util.Shout;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class AdvancementMadeEvent {
    public static void registerAdvancements() {
        AdvancementMadeCallback.EVENT.register((player, advancement) -> {
            if (advancement.getId().getNamespace().equals("anxshouts")) {
                Shout shout = Shout.byId(advancement.getId().getPath().substring(15)); // find_word_wall_ 15 chars
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(shout.ordinal());
                ClientPlayNetworking.send(ModPackets.UNLOCK_SHOUT_ID, buf);

                player.playSound(SoundEvents.GOAT_HORN_SOUNDS.get(0).value(), SoundCategory.PLAYERS, 0.7f, 1.0f);
                String translatable = "advancements." + advancement.getId().toString().replace(':', '.');
                player.sendMessage(Text.translatable(translatable).append(" - ").append(Text.translatable(translatable + ".title")), true);
            }
            return ActionResult.PASS;
        });
    }
}
