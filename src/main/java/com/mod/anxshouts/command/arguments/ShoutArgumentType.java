package com.mod.anxshouts.command.arguments;

import com.google.gson.JsonPrimitive;
import com.mod.anxshouts.client.util.ShoutHandler;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ShoutArgumentType implements ArgumentType<String> {
    private static final DynamicCommandExceptionType INVALID_ENUM_EXCEPTION = new DynamicCommandExceptionType(value -> Text.translatable("argument.enum.invalid", value));

    private final Codec<ShoutHandler.Shout> codec;
    private final Supplier<ShoutHandler.Shout[]> valuesSupplier;

    public ShoutArgumentType() {
        this.codec = ShoutHandler.Shout.CODEC;
        this.valuesSupplier = ShoutHandler.Shout::values;
    }

    public static ShoutArgumentType shout() {
        return new ShoutArgumentType();
    }

    public static String getShout(final CommandContext<ServerCommandSource> context, final String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        String string = reader.readUnquotedString();
        if (string.equals("all"))
            return "all";
        return this.codec.parse(JsonOps.INSTANCE, new JsonPrimitive(string)).result().orElseThrow(() -> INVALID_ENUM_EXCEPTION.create(string)).getId();
    }

    @Override
    public Collection<String> getExamples() {
        Collection<String> examples = Arrays.stream(this.valuesSupplier.get()).map(ShoutHandler.Shout::getId).collect(Collectors.toList());
        examples.add("all");
        return examples;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(getExamples(), builder);
    }
}
