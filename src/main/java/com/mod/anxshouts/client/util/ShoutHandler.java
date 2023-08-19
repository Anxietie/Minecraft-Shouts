package com.mod.anxshouts.client.util;

import com.mod.anxshouts.client.gui.ShoutSelectionGui;
import com.mod.anxshouts.client.gui.ShoutSelectionScreen;
import com.mod.anxshouts.client.registry.KeybindRegister;
import com.mod.anxshouts.components.IShout;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

@Environment(EnvType.CLIENT)
public class ShoutHandler {
    public static void shoutCallback() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeybindRegister.SHOUT_KEY.wasPressed()) {
                PlayerEntity player = client.player;
                try {
                    if (player.isSpectator()) return;
                    if (player.isCreative()) IShout.KEY.get(player).resetShoutCooldown();
                    if (IShout.KEY.get(player).getShoutCooldown() > 0) {
                        int remainingSeconds = IShout.KEY.get(player).getShoutCooldown() / 20;
                        client.inGameHud.setOverlayMessage(Text.literal("Shout on cooldown for " + remainingSeconds + " more seconds"), true);
                        return;
                    }

                    // ClientPlayNetworking.send(ModPackets.SHOUT_ID, PacketByteBufs.create());
                }
                catch (NullPointerException ignored) {}
            }
            while (KeybindRegister.SHOUT_GUI_OPEN_KEY.wasPressed()) {
                PlayerEntity player = client.player;
                try {
                    if (client.currentScreen == null)
                        client.send(() -> client.setScreen(new ShoutSelectionScreen(new ShoutSelectionGui(player))));
                }
                catch (NullPointerException ignored) {}
            }
        });
    }

    public enum Shout implements StringIdentifiable {
        NONE("none"),
        FIRE("fire"),
        FROST("frost"),
        FORCE("force");

        public static final Codec<Shout> CODEC;
        private final String id;
        private final Text name;

        Shout(String id) {
            this.id = id;
            this.name = Text.translatable("shouts." + id);
        }

        @Override
        public String asString() {
            return this.id;
        }

        public Text getName() {
            return this.name;
        }

        public String getId() { return this.id; }

        public static Shout fromOrdinal(int ordinal) {
            if (ordinal >= values().length || ordinal < 0) return null;
            return values()[ordinal];
        }

        static {
            CODEC = StringIdentifiable.createCodec(Shout::values);
        }
    }
}
