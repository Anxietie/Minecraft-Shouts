package com.mod.anxshouts.command;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.command.arguments.ShoutArgumentType;
import com.mod.anxshouts.components.IShout;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mod.anxshouts.command.arguments.ShoutArgumentType.getShout;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShoutCommand {
    private static final SimpleCommandExceptionType INVALID_SOURCE = new SimpleCommandExceptionType(Text.translatable("commands.shout.source"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shout")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("give")
                        .then(argument("shout", ShoutArgumentType.shout())
                                .executes(ctx -> addShout(ctx.getSource(), getShout(ctx, "shout")))
                        )
                )
                .then(literal("take")
                        .then(argument("shout", ShoutArgumentType.shout())
                                .executes(ctx -> removeShout(ctx.getSource(), getShout(ctx, "shout")))
                        )
                )
                .then(literal("set")
                        .then(argument("shout", ShoutArgumentType.shout())
                                .executes(ctx -> setShout(ctx.getSource(), getShout(ctx, "shout")))
                        )
                )
        );
    }

    private static int addShout(ServerCommandSource source, ShoutHandler.Shout shout) throws CommandSyntaxException {
        PlayerEntity player = source.getPlayer();
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).obtainShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int removeShout(ServerCommandSource source, ShoutHandler.Shout shout) throws CommandSyntaxException {
        PlayerEntity player = source.getPlayer();
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).removeShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int setShout(ServerCommandSource source, ShoutHandler.Shout shout) throws CommandSyntaxException {
        PlayerEntity player = source.getPlayer();
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).setSelectedShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }
}
