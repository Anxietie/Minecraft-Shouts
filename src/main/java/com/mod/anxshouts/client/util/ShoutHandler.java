package com.mod.anxshouts.client.util;

import com.mod.anxshouts.client.gui.ShoutSelectionGui;
import com.mod.anxshouts.client.gui.ShoutSelectionScreen;
import com.mod.anxshouts.client.registry.KeybindRegister;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.networking.ModPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ShoutHandler {
    public static void keyCallback() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if (player == null) return;

            IShout data = IShout.KEY.get(player);
            while (KeybindRegister.SHOUT_KEY.wasPressed()) {
                if (player.isSpectator()) return;
                if (player.isCreative()) data.resetShoutCooldown();
                if (data.getShoutCooldown() > 0) {
                    int remainingSeconds = data.getShoutCooldown() / 20;
                    client.inGameHud.setOverlayMessage(Text.literal("Shout on cooldown for " + remainingSeconds + " more seconds"), true);
                    return;
                }
                ClientPlayNetworking.send(ModPackets.ACTION_SHOUT_ID, PacketByteBufs.create());
            }
            while (KeybindRegister.SHOUT_GUI_OPEN_KEY.wasPressed()) {
                if (client.currentScreen == null)
                    client.send(() -> client.setScreen(new ShoutSelectionScreen(new ShoutSelectionGui(player))));
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

        @NotNull
        public String getId() { return this.id; }

        public static Shout fromOrdinal(int ordinal) {
            if (ordinal >= values().length || ordinal < 0) throw new IllegalArgumentException("Shout of index " + ordinal + " does not exist!");
            return values()[ordinal];
        }

        static {
            CODEC = StringIdentifiable.createCodec(Shout::values);
        }
    }
}
