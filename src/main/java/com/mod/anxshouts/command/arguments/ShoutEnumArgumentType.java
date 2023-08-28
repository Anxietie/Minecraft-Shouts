package com.mod.anxshouts.command.arguments;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class ShoutEnumArgumentType extends EnumArgumentType<ShoutHandler.Shout> {
    protected ShoutEnumArgumentType() { super(ShoutHandler.Shout.CODEC, ShoutHandler.Shout::values); }

    public static EnumArgumentType<ShoutHandler.Shout> shout() {
        return new ShoutEnumArgumentType();
    }

    public static ShoutHandler.Shout getShout(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, ShoutHandler.Shout.class);
    }
}
