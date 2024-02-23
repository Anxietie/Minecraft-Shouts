package com.mod.anxshouts.command.arguments;

import com.mod.anxshouts.util.Shout;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class ShoutEnumArgumentType extends EnumArgumentType<Shout> {
    public ShoutEnumArgumentType() { super(Shout.CODEC, Shout::values); }

    public static EnumArgumentType<Shout> shout() {
        return new ShoutEnumArgumentType();
    }

    public static Shout getShout(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, Shout.class);
    }
}
