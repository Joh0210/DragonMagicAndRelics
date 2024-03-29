package de.joh.dmnr.common.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.joh.dmnr.common.util.Registries;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Argument to list all armor upgrades.
 * @author Joh0210
 */
public class ArmorUpgradeArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = new ArrayList<>();
    public static final DynamicCommandExceptionType PART_BAD_ID = new DynamicCommandExceptionType((p_208696_0_) -> Component.translatable("argument.item.id.invalid", p_208696_0_));

    public ArmorUpgradeArgument() {
    }

    public static ArmorUpgradeArgument armorUpgrade() {
        return new ArmorUpgradeArgument();
    }

    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        String inputString = reader.readString();

        try {
            return new ResourceLocation(inputString);
        } catch (Exception var5) {
            reader.setCursor(i);
            throw PART_BAD_ID.createWithContext(reader, inputString);
        }
    }

    private Collection<String> populateAndGetExamples() {
        if (EXAMPLES.isEmpty()) {
            Registries.ARMOR_UPGRADE.get().getKeys().forEach((r) -> EXAMPLES.add("\"" + r.toString() + "\""));
        }

        return EXAMPLES;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(new ArrayList<>(this.populateAndGetExamples()), builder);
    }

    public Collection<String> getExamples() {
        return this.populateAndGetExamples();
    }
}
