package com.mod.anxshouts.events;

import com.mod.anxshouts.components.IShout;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityDeathEvent {
    public static void registerEntityDeaths() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, attacker, victim) -> {
            if (!(victim instanceof EnderDragonEntity)) return;
            if (!(attacker instanceof PlayerEntity)) return;
            IShout.KEY.get(attacker).incrementSoulCount();
        });
    }
}
