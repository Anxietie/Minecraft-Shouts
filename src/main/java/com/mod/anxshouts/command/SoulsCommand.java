package com.mod.anxshouts.command;

import com.mod.anxshouts.components.IShout;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SoulsCommand {
    private static final SimpleCommandExceptionType INVALID_SOURCE = new SimpleCommandExceptionType(Text.translatable("commands.anxshouts.shout.invalid_source"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("souls")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgumentType.player())
                        .then(CommandManager.literal("give")
                                .then(argument("souls", IntegerArgumentType.integer(0))
                                        .executes(ctx -> giveSouls(getPlayer(ctx, "player"), getInteger(ctx, "souls")))
                                )
                        )
                        .then(CommandManager.literal("take")
                                .then(argument("souls", IntegerArgumentType.integer(0))
                                        .executes(ctx -> takeSouls(getPlayer(ctx, "player"), getInteger(ctx, "souls")))
                                )
                        )
                        .then(CommandManager.literal("set")
                                .then(argument("souls", IntegerArgumentType.integer(0))
                                        .executes(ctx -> setSouls(getPlayer(ctx, "player"), getInteger(ctx, "souls")))
                                )
                        )
                )
        );
    }

    private static int giveSouls(ServerPlayerEntity player, int souls) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).setSoulCount(IShout.KEY.get(player).getSoulCount() + souls);
        return Command.SINGLE_SUCCESS;
    }

    private static int takeSouls(ServerPlayerEntity player, int souls) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).setSoulCount(Math.max(IShout.KEY.get(player).getSoulCount() - souls, 0));
        return Command.SINGLE_SUCCESS;
    }

    private static int setSouls(ServerPlayerEntity player, int souls) throws CommandSyntaxException {
        if (player == null)
            throw INVALID_SOURCE.create();
        IShout.KEY.get(player).setSoulCount(souls);
        return Command.SINGLE_SUCCESS;
    }
}
