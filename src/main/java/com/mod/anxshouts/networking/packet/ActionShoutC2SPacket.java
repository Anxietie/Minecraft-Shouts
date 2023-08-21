package com.mod.anxshouts.networking.packet;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.registry.SoundRegister;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.ConcurrentModificationException;
import java.util.List;

public class ActionShoutC2SPacket {
    // TODO: add particles and sounds
    // TODO: add more shouts
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IShout data = IShout.KEY.get(player);
        ShoutHandler.Shout shout = ShoutHandler.Shout.fromOrdinal(data.getSelectedShout());

        float yaw = player.getHeadYaw();
        float pitch = player.getPitch();

        Box box = player.getBoundingBox().expand(4.0);
        List<Entity> entities;
        try {
            entities = player.getWorld().getOtherEntities(player, box, (entity) -> {
                double distFromOrigin = ModUtils.getHorizontalDistanceFromOrigin(player);
                double distFromPlayer = ModUtils.getHorizontalDistanceFromPlayer(entity, player);
                double playerHeightY = player.getStandingEyeHeight() + player.getY();
                double entityHeightY = entity.getStandingEyeHeight() + entity.getY();
                return entity instanceof LivingEntity
                        && ModUtils.inHorizontalFOV(player.getX(), player.getZ(), entity.getX(), entity.getZ(), ModUtils.convertYaw(yaw) + (Math.PI/2)) <= 0
                        && ModUtils.inVerticalFOV(playerHeightY, distFromOrigin, entityHeightY, distFromOrigin + distFromPlayer, Math.toRadians(pitch + 90)) <= 0
                        && player.canSee(entity);
            });
        }
        catch (ConcurrentModificationException ignore) {
            player.sendMessage(Text.literal("Slow down!"), true);
            return;
        }

        switch(shout) {
            case FIRE -> {
                for (Entity e : entities) {
                    if (!e.isFireImmune() && e.getFireTicks() <= 0)
                        e.setOnFireFor(8);
                }
                data.setShoutCooldown(2000);
                player.sendMessage(Text.translatable("anxshouts.shouts.fire"), true);
            }
            case FROST -> {
                for (Entity e : entities) {
                    e.setFrozenTicks(e.getMinFreezeDamageTicks() + 200);
                    ((LivingEntity) e).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2, false, false));
                }
                data.setShoutCooldown(2000);
                player.sendMessage(Text.translatable("anxshouts.shouts.frost"), true);

            }
            case FORCE -> {
                for (Entity e : entities) {
                    double strength = 2.0;
                    double x = strength*Math.cos(ModUtils.convertYaw(yaw) - (Math.PI/2));
                    double z = strength*Math.sin(ModUtils.convertYaw(yaw) - (Math.PI/2));
                    e.damage(player.getWorld().getDamageSources().playerAttack(player), 0.5f);
                    ((LivingEntity) e).takeKnockback(strength, x, z);
                }
                player.playSound(SoundRegister.UNRELENTING_FORCE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                data.setShoutCooldown(2000);
                player.sendMessage(Text.translatable("anxshouts.shouts.force"), true);
            }
            default -> {
                player.sendMessage(Text.translatable("anxshouts.shouts.none"), true);
            }
        }
    }
}
