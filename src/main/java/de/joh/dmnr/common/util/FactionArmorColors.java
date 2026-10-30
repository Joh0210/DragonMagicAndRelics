package de.joh.dmnr.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Default colors of the Dragon Mage Armor for each M&A faction, loaded from data packs on the server.
 * <br>The armor takes on these colors when a player obtains it through the Totem of the Dragon, and the dyeing GUI
 * resets to them.
 * <p>
 * Every faction has its own file, named after the ID of the faction: the colors of {@code mna:council} are in
 * {@code data/mna/dragon_mage_armor_colors/council.json}. Mods that add factions ship the colors in their own namespace
 * without depending on this mod, and data packs can replace any file. Files of factions that are not installed are
 * ignored. The colors of players without a faction are in {@code data/dmnr/dragon_mage_armor_colors/none.json}.
 * <pre>{@code
 * {
 *   "primary": "#59AFFF",
 *   "secondary": "#FFFFFF"
 * }
 * }</pre>
 * {@code primary} is the color of the ornaments, {@code secondary} the color of the plates. Both are a hex color code
 * {@code "#RRGGBB"} or the name of a text color (e.g. {@code "gold"}).
 * <br>A faction without a file gets its text color (the color of its torn journal pages) for the ornaments and the
 * original plates.
 * @see DragonMageArmorItem
 * @author Joh0210
 */
public class FactionArmorColors extends SimpleJsonResourceReloadListener {
    public static final String DIRECTORY = "dragon_mage_armor_colors";
    /**
     * ID of the file with the colors of players without a faction.
     */
    public static final ResourceLocation NO_FACTION = RLoc.create("none");
    public static final FactionArmorColors INSTANCE = new FactionArmorColors();

    private Map<ResourceLocation, Colors> colors = Map.of();

    private FactionArmorColors() {
        super(new Gson(), DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, Colors> loaded = new HashMap<>();
        files.forEach((id, json) -> Colors.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(error -> DragonMagicAndRelics.LOGGER.error("Couldn't parse Dragon Mage Armor colors {}: {}", id, error))
                .ifPresent(parsed -> loaded.put(id, parsed)));
        this.colors = Map.copyOf(loaded);
    }

    /**
     * Only available on the server.
     * @return the default colors for the faction of the player
     */
    public static Colors getColors(Player player) {
        IFaction faction = player.getCapability(PlayerProgressionProvider.PROGRESSION).map(IPlayerProgression::getAlliedFaction).orElse(null);
        return getColors(faction);
    }

    /**
     * Only available on the server.
     * @param faction the faction, or null for players without a faction
     * @return the default colors for the faction
     */
    public static Colors getColors(@Nullable IFaction faction) {
        ResourceLocation id = faction == null ? NO_FACTION : Registries.Factions.get().getKey(faction);
        Colors fromData = id == null ? null : INSTANCE.colors.get(id);
        if (fromData != null) {
            return fromData;
        }
        return faction == null ? Colors.ORIGINAL : Colors.fromFaction(faction);
    }

    /**
     * Both colors of the Dragon Mage Armor.
     * @param primary RGB color of the ornaments
     * @param secondary RGB color of the plates
     */
    public record Colors(int primary, int secondary) {
        /**
         * The colors of the original design, which undyed armor has.
         */
        public static final Colors ORIGINAL = new Colors(DragonMageArmorItem.DEFAULT_PRIMARY_COLOR, DragonMageArmorItem.DEFAULT_SECONDARY_COLOR);

        public static final Codec<Colors> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TextColor.CODEC.fieldOf("primary").forGetter(colors -> TextColor.fromRgb(colors.primary())),
                TextColor.CODEC.fieldOf("secondary").forGetter(colors -> TextColor.fromRgb(colors.secondary()))
        ).apply(instance, (primary, secondary) -> new Colors(primary.getValue(), secondary.getValue())));

        public Colors {
            primary &= 0xFFFFFF;
            secondary &= 0xFFFFFF;
        }

        /**
         * Fallback for factions without a file: ornaments in the text color of the faction, original plates.
         */
        private static Colors fromFaction(IFaction faction) {
            ChatFormatting textColor = faction.getTornJournalPageFactionColor();
            Integer color = textColor != null ? textColor.getColor() : null;
            return color != null ? new Colors(color, DragonMageArmorItem.DEFAULT_SECONDARY_COLOR) : ORIGINAL;
        }

        public static Colors read(FriendlyByteBuf buffer) {
            return new Colors(buffer.readInt(), buffer.readInt());
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(primary);
            buffer.writeInt(secondary);
        }
    }
}
