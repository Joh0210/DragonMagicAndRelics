package de.joh.dragonmagicandrelics.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.joh.dragonmagicandrelics.Registries;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
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
    public static final DynamicCommandExceptionType PART_BAD_ID = new DynamicCommandExceptionType((p_208696_0_) -> new TranslatableComponent("argument.item.id.invalid", p_208696_0_));

    public ArmorUpgradeArgument() {
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
        if (EXAMPLES.size() == 0) {
            //EXAMPLES.add("\"dncapi:none\"");
            Registries.ARMOR_UPGRADE.get().getKeys().forEach((r) -> EXAMPLES.add("\"" + r.toString() + "\""));
        }

        return EXAMPLES;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(new ArrayList<>(this.populateAndGetExamples()), builder);
    }

    public static <S> ResourceLocation getArmorUpgrade(CommandContext<S> context, String name) {
        return context.getArgument(name, ResourceLocation.class);
    }

    public Collection<String> getExamples() {
        return this.populateAndGetExamples();
    }
}
