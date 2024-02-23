package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.util.ModUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class EntityDeathEvent {
    public static void registerEntityDeaths() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, attacker, victim) -> {
            if (!(victim instanceof EnderDragonEntity)) return;
            if (!(attacker instanceof PlayerEntity)) return;
            ((ServerPlayerEntity) attacker).playSound(SoundEvents.GOAT_HORN_SOUNDS.get(0).value(), SoundCategory.PLAYERS, 0.7f, 1.0f);
            ((ServerPlayerEntity) attacker).networkHandler.sendPacket(new TitleS2CPacket(Text.literal("DRAGON SOUL ABSORBED")));
            IShout.KEY.get(attacker).incrementSoulCount();
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof PlayerEntity) {
                IShout data = IShout.KEY.get(entity);
                data.setShoutCooldown(0);
                data.setEtherealTicks(0);
                data.setDATicks(0);
                data.setDACooldown(0);
                data.setValorTicks(0);
                if (entity.getServer() != null) {
                    ModUtils.killWolfSoldiers(entity.getServer(), (PlayerEntity) entity);
                    ModUtils.killCompanions(entity.getServer(), (PlayerEntity) entity);
                }
                data.setValorUUID(null);
                data.setCompanionUUID(null);
            }
        });
    }
}
