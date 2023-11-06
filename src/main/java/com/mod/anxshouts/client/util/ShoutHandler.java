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
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

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

                emitParticles(player, 20);

                ClientPlayNetworking.send(ModPackets.ACTION_SHOUT_ID, PacketByteBufs.create());
            }
            while (KeybindRegister.SHOUT_GUI_OPEN_KEY.wasPressed()) {
                if (client.currentScreen == null)
                    client.send(() -> client.setScreen(new ShoutSelectionScreen(new ShoutSelectionGui(player))));
            }
        });
    }

    private static void emitParticles(ClientPlayerEntity player, int count) {
        ParticleEffect particleEffect = ShoutHandler.Shout.fromOrdinal(IShout.KEY.get(player).getSelectedShout()).getParticleEffect();
        if (particleEffect == null) return;

        Random random = new Random();
        final double SPREAD = 0.6; // affects spread

        double velocityX = Math.cos(ModUtils.convertYaw(player.getHeadYaw()) + (Math.PI/2));
        double velocityZ = Math.sin(ModUtils.convertYaw(player.getHeadYaw()) + (Math.PI/2));
        double velocityY = Math.sin(Math.toRadians(-player.getPitch()));
        for (int i = 0; i < count; i++)
            // (SPREAD * (2 * random.nextDouble() - 1)) makes the particles deviate off their path by a value between -1 and 1
            player.getWorld().addParticle(particleEffect, player.getX(), player.getEyeY(), player.getZ(), velocityX + (SPREAD * (2 * random.nextDouble() - 1)), velocityY + (SPREAD * (2 * random.nextDouble() - 1)), velocityZ + (SPREAD * (2 * random.nextDouble() - 1)));
    }

    public enum Shout implements StringIdentifiable {
        NONE("none", 0, null),
        FIRE("fire", 2, ParticleTypes.FLAME),
        FROST("frost", 1, ParticleTypes.SNOWFLAKE),
        FORCE("force", 2, ParticleTypes.DRAGON_BREATH),
        AURA("aura", 1, ParticleTypes.ENCHANT),
        STORM("storm", 2, ParticleTypes.CRIT),
        CLEAR("clear", 1, ParticleTypes.SNOWFLAKE),
        DISARM("disarm", 2, ParticleTypes.EFFECT);

        public static final Codec<Shout> CODEC;
        private final String id;
        private final Text name;
        private final int cost;
        private final ParticleEffect particle;

        Shout(String id, int cost, ParticleEffect associatedParticle) {
            this.id = id;
            this.name = Text.translatable("anxshouts.shouts." + id);
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

        public static Shout byId(String id) {
            for (Shout shout : values())
                if (shout.getId().equals(id)) return shout;
            return NONE;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Shout::values);
        }
    }
}
