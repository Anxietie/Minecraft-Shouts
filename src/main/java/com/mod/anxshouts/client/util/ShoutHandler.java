package com.mod.anxshouts.client.util;

import com.mod.anxshouts.client.gui.ShoutSelectionGui;
import com.mod.anxshouts.client.gui.ShoutSelectionScreen;
import com.mod.anxshouts.client.registry.KeybindRegister;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.networking.ModPackets;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
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

                ModUtils.emitParticles(player, 20);

                ClientPlayNetworking.send(ModPackets.ACTION_SHOUT_ID, PacketByteBufs.create());
            }
            while (KeybindRegister.SHOUT_GUI_OPEN_KEY.wasPressed()) {
                if (client.currentScreen == null)
                    client.send(() -> client.setScreen(new ShoutSelectionScreen(new ShoutSelectionGui(player))));
            }
        });
    }

    public enum Shout implements StringIdentifiable {
        NONE("none", 0, null),
        FIRE("fire", 2, ParticleTypes.FLAME),
        FROST("frost", 1, ParticleTypes.SNOWFLAKE),
        FORCE("force", 2, ParticleTypes.BUBBLE),
        AURA("aura", 1, ParticleTypes.ENCHANT),
        STORM("storm", 2, ParticleTypes.CRIT);

        public static final Codec<Shout> CODEC;
        private final String id;
        private final Text name;
        private final int cost;
        private final ParticleEffect particle;

        Shout(String id, int cost, ParticleEffect associatedParticle) {
            this.id = id;
            this.name = Text.translatable("shouts." + id);
            this.cost = cost;
            this.particle = associatedParticle;
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

        public ParticleEffect getParticleEffect() { return this.particle; }

        public int getCost() { return this.cost; }

        static {
            CODEC = StringIdentifiable.createCodec(Shout::values);
        }
    }
}
