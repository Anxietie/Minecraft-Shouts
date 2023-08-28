package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.registry.SoundRegister;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

public class EntityDeathEvent {
    public static void registerEntityDeaths() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, attacker, victim) -> {
            if (!(victim instanceof EnderDragonEntity)) return;
            if (!(attacker instanceof PlayerEntity)) return;
            ((ServerPlayerEntity) attacker).playSound(SoundRegister.ABSORB_SOUL, SoundCategory.PLAYERS, 0.7f, 1.0f);
            ((ServerPlayerEntity) attacker).networkHandler.sendPacket(new TitleS2CPacket(Text.literal("DRAGON SOUL ABSORBED")));
            IShout.KEY.get(attacker).incrementSoulCount();
        });
    }
}
