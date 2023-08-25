package com.mod.anxshouts.command;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.command.arguments.ShoutArgumentType;
import com.mod.anxshouts.components.IShout;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ShoutCommand {
	private static final SimpleCommandExceptionType INVALID_SOURCE = new SimpleCommandExceptionType(Text.translatable("commands.anxshouts.shout.invalid_source"));
	private static final SimpleCommandExceptionType INVALID_ARGUMENT = new SimpleCommandExceptionType(Text.translatable("commands.anxshouts.shout.invalid_argument"));

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("shout")
				.requires(source -> source.hasPermissionLevel(2))
				.then(argument("player", EntityArgumentType.player())
						.then(literal("give")
								.then(argument("shout", ShoutArgumentType.shout())
										.executes(ctx -> obtainShout(getPlayer(ctx, "player") , ShoutArgumentType.getShout(ctx, "shout")))
								)
						)
						.then(literal("take")
								.then(argument("shout", ShoutArgumentType.shout())
										.executes(ctx -> removeShout(getPlayer(ctx, "player"), ShoutArgumentType.getShout(ctx, "shout")))
								)
						)
						.then(literal("set")
								.then(argument("shout", ShoutArgumentType.shout())
										.executes(ctx -> setSelectedShout(getPlayer(ctx, "player"), ShoutArgumentType.getShout(ctx, "shout")))
								)
						)
						.then(literal("unlock")
								.then(argument("shout", ShoutArgumentType.shout())
										.executes(ctx -> unlockShout(getPlayer(ctx, "player"), ShoutArgumentType.getShout(ctx, "shout")))
								)
						)
						.then(literal("lock")
								.then(argument("shout", ShoutArgumentType.shout())
										.executes(ctx -> lockShout(getPlayer(ctx, "player"), ShoutArgumentType.getShout(ctx, "shout")))
								)
						)
				)
		);
	}

	private static int obtainShout(ServerPlayerEntity player, String shout) throws CommandSyntaxException {
		if (player == null)
			throw INVALID_SOURCE.create();
        if (shout.equals("all"))
            IShout.KEY.get(player).obtainAllShouts();
		else
            IShout.KEY.get(player).obtainShout(ShoutHandler.Shout.byId(shout).ordinal());
		return Command.SINGLE_SUCCESS;
	}

	private static int removeShout(ServerPlayerEntity player, String shout) throws CommandSyntaxException {
		if (player == null)
			throw INVALID_SOURCE.create();
        if (shout.equals("all"))
            IShout.KEY.get(player).removeAllShouts();
        else
            IShout.KEY.get(player).removeShout(ShoutHandler.Shout.byId(shout).ordinal());
        return Command.SINGLE_SUCCESS;
	}

	private static int setSelectedShout(ServerPlayerEntity player, String shout) throws CommandSyntaxException {
		if (player == null)
			throw INVALID_SOURCE.create();
		if (shout.equals("all"))
			throw INVALID_ARGUMENT.create();
		else
			IShout.KEY.get(player).setSelectedShout(ShoutHandler.Shout.byId(shout).ordinal());
		return Command.SINGLE_SUCCESS;
	}

	private static int unlockShout(ServerPlayerEntity player, String shout) throws CommandSyntaxException {
		if (player == null)
			throw INVALID_SOURCE.create();
        if (shout.equals("all"))
            IShout.KEY.get(player).unlockAllShouts();
        else
            IShout.KEY.get(player).unlockShout(ShoutHandler.Shout.byId(shout).ordinal());
        return Command.SINGLE_SUCCESS;
	}

	private static int lockShout(ServerPlayerEntity player, String shout) throws CommandSyntaxException {
		if (player == null)
			throw INVALID_SOURCE.create();
        if (shout.equals("all"))
            IShout.KEY.get(player).lockAllShouts();
        else
            IShout.KEY.get(player).lockShout(ShoutHandler.Shout.byId(shout).ordinal());
        return Command.SINGLE_SUCCESS;
	}
}
