package com.mod.anxshouts.components;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;

public final class ModComponents implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // registry.registerForPlayers(IShout.KEY, PlayerShout::new, RespawnCopyStrategy.ALWAYS_COPY);
        // registry.beginRegistration(PlayerEntity.class, IShout.KEY).impl(PlayerShout.class).end(player -> new PlayerShout());
        registry.registerFor(PlayerEntity.class, IShout.KEY, PlayerShout::new);
    }
}
