package com.mod.anxshouts.command.arguments;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class ShoutArgumentType extends EnumArgumentType<ShoutHandler.Shout> {
    private ShoutArgumentType() {
        super(ShoutHandler.Shout.CODEC, ShoutHandler.Shout::values);
    }

    public static EnumArgumentType<ShoutHandler.Shout> shout() {
        return new ShoutArgumentType();
    }

    public static ShoutHandler.Shout getShout(final CommandContext<ServerCommandSource> context, final String name) {
        return context.getArgument(name, ShoutHandler.Shout.class);
    }
}
