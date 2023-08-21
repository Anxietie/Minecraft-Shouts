package com.mod.anxshouts.command;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.command.arguments.ShoutArgumentType;
import com.mod.anxshouts.components.IShout;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mod.anxshouts.command.arguments.ShoutArgumentType.getShout;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShoutCommand {
    private static final SimpleCommandExceptionType INVALID_SOURCE = new SimpleCommandExceptionType(Text.translatable("commands.anxshouts.shout.invalid_source"));
    private static final SimpleCommandExceptionType INVALID_ARGUMENT = new SimpleCommandExceptionType(Text.translatable("commands.anxshouts.shout.invalid_argument"));

    // TODO: create new argument type that accepts "all" so this doesnt look retarded
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("shout")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(literal("give")
                                .then(argument("shout", ShoutArgumentType.shout())
                                        .executes(ctx -> obtainShout(getPlayer(ctx, "player") , getShout(ctx, "shout")))
                                )
                                .then(argument("string", StringArgumentType.string())
                                        .executes(ctx -> obtainAllShouts(getPlayer(ctx, "player"), getString(ctx, "string")))
                                )
                        )
                        .then(literal("take")
                                .then(argument("shout", ShoutArgumentType.shout())
                                        .executes(ctx -> removeShout(getPlayer(ctx, "player"), getShout(ctx, "shout")))
                                )
                                .then(argument("string", StringArgumentType.string())
                                        .executes(ctx -> removeAllShouts(getPlayer(ctx, "player"), getString(ctx, "string")))
                                )
                        )
                        .then(literal("set")
                                .then(argument("shout", ShoutArgumentType.shout())
                                        .executes(ctx -> setSelectedShout(getPlayer(ctx, "player"), getShout(ctx, "shout")))
                                )
                        )
                        .then(literal("unlock")
                                .then(argument("shout", ShoutArgumentType.shout())
                                        .executes(ctx -> unlockShout(getPlayer(ctx, "player"), getShout(ctx, "shout")))
                                )
                                .then(argument("string", StringArgumentType.string())
                                        .executes(ctx -> unlockAllShouts(getPlayer(ctx, "player"), getString(ctx, "string")))
                                )
                        )
                        .then(literal("lock")
                                .then(argument("shout", ShoutArgumentType.shout())
                                        .executes(ctx -> lockShout(getPlayer(ctx, "player"), getShout(ctx, "shout")))
                                )
                                .then(argument("string", StringArgumentType.string())
                                        .executes(ctx -> lockAllShouts(getPlayer(ctx, "player"), getString(ctx, "string")))
                                )
                        )
                )
        );
    }

    private static int obtainShout(ServerPlayerEntity player, ShoutHandler.Shout shout) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).obtainShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int removeShout(ServerPlayerEntity player, ShoutHandler.Shout shout) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).removeShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int setSelectedShout(ServerPlayerEntity player, ShoutHandler.Shout shout) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).setSelectedShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int unlockShout(ServerPlayerEntity player, ShoutHandler.Shout shout) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).unlockShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int lockShout(ServerPlayerEntity player, ShoutHandler.Shout shout) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).lockShout(shout.ordinal());
        return Command.SINGLE_SUCCESS;
    }

    private static int obtainAllShouts(ServerPlayerEntity player, String all) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        if (!all.equals("all"))
            throw INVALID_ARGUMENT.create();
        IShout.KEY.get(player).obtainAllShouts();
        return Command.SINGLE_SUCCESS;
    }

    private static int removeAllShouts(ServerPlayerEntity player, String all) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        if (!all.equals("all"))
            throw INVALID_ARGUMENT.create();
        IShout.KEY.get(player).removeAllShouts();
        return Command.SINGLE_SUCCESS;
    }

    private static int unlockAllShouts(ServerPlayerEntity player, String all) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        if (!all.equals("all"))
            throw INVALID_ARGUMENT.create();
        IShout.KEY.get(player).unlockAllShouts();
        return Command.SINGLE_SUCCESS;
    }

    private static int lockAllShouts(ServerPlayerEntity player, String all) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        if (!all.equals("all"))
            throw INVALID_ARGUMENT.create();
        IShout.KEY.get(player).lockAllShouts();
        return Command.SINGLE_SUCCESS;
    }
}
