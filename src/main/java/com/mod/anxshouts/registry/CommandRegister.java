package com.mod.anxshouts.registry;

import com.mod.anxshouts.command.ShoutCommand;
import com.mod.anxshouts.command.arguments.ShoutArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;

import static com.mod.anxshouts.MinecraftShouts.MODID;
import static com.mojang.text2speech.Narrator.LOGGER;

public class CommandRegister {
    public static void commandCallback() {
        CommandRegistrationCallback.EVENT.register(CommandRegister::register);
        registerArgType("shout", ShoutArgumentType.class, ConstantArgumentSerializer.of(ShoutArgumentType::shout));
    }

    private static void registerArgType(String id, Class<? extends ArgumentType> argumentTypeClass, ArgumentSerializer serializer) {
        try {
            argumentTypeClass.getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(String.valueOf(e));
        }

        ArgumentTypeRegistry.registerArgumentType(new Identifier(MODID, id), argumentTypeClass, serializer);
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        ShoutCommand.register(dispatcher);
    }
}
