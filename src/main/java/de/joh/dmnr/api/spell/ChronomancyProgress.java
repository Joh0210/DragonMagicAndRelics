package de.joh.dmnr.api.spell;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.events.RitualCompleteEvent;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.spells.SpellCaster;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagic;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dmnr.common.init.SpellInit;
import de.joh.dmnr.common.spell.component.AccelerateTimeComponent;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rote progress of the silver spell Accelerate Time, which becomes rote by bending time.
 * <br> Every source has a weight and grants {@link CommonConfig#CHRONOMANCY_PROGRESS} times its weight as rote progress,
 * but only from {@link CommonConfig#CHRONOMANCY_REQUIRED_TIER} on and at most once per {@link CommonConfig#CHRONOMANCY_COOLDOWN}.
 * <br> Spell parts are counted when M&A applies them to a target (ComponentApplyingEvent). M&A has no event after the
 * application, so a spell part also counts if its effect fails afterwards; the cooldown limits that anyway.
 * <br><br> Sources are registered here by ID, so spell parts of other mods (e.g. Postponement of Factions and Curiosities)
 * only need an entry, not a dependency. Other mods can add sources (e.g. during the FMLCommonSetupEvent):
 * <br> - {@link #register}: counted automatically whenever M&A applies the spell part with this ID to a target,
 * or completes the ritual with this ID (the ID of the ritual recipe).
 * <br> - {@link #registerManual} + {@link #grant}: for sources that are no spell part or ritual (e.g. an item),
 * which report their success themselves.
 * <br> Entries of {@link CommonConfig#CHRONOMANCY_SOURCES} override the registered weights. IDs that only exist in the config
 * are counted automatically.
 * @see AccelerateTimeComponent
 * @author Joh0210
 */
public class ChronomancyProgress {
    private static final Map<ResourceLocation, Float> SOURCES = new ConcurrentHashMap<>();
    private static final Set<ResourceLocation> MANUAL_SOURCES = ConcurrentHashMap.newKeySet();

    /**
     * Parsed {@link CommonConfig#CHRONOMANCY_SOURCES}, rebuilt when the config value changes
     */
    private static List<? extends String> parsedConfig = null;
    private static Map<ResourceLocation, Float> configWeights = Map.of();

    static {
        // spell parts: counted when applied to a target. Spell parts of mods that are not installed never match.
        register(RLoc.create("components/sunrise"), 1.0f);
        register(RLoc.create("components/moonrise"), 1.0f);
        register(new ResourceLocation("mna", "components/chrono_anchor"), 1.0f);
        register(new ResourceLocation("fnc", "components/postponement"), 1.0f);

        // rituals: counted when completed
        register(new ResourceLocation("mna", "rituals/aurora"), 0.5f);
        register(new ResourceLocation("mna", "rituals/eventide"), 0.5f);

        // not a spell: the item reports itself
        registerManual(RLoc.create("weather_fairy_staff"), 1.0f);
    }

    /**
     * Registers a source that is counted automatically: a spell part (counted when M&A applies it to a target)
     * or a ritual (counted when it is completed).
     * @param id Registry ID of the spell part (e.g. "mna:components/chrono_anchor") or ID of the ritual recipe (e.g. "mna:rituals/aurora")
     * @param weight Multiplier of the granted rote progress (1.0 = {@link CommonConfig#CHRONOMANCY_PROGRESS})
     */
    public static void register(@NotNull ResourceLocation id, float weight) {
        SOURCES.put(id, weight);
        MANUAL_SOURCES.remove(id);
    }

    /**
     * Registers a source that reports its success itself via {@link #grant}, for sources that are no spell part or ritual.
     * It is not counted automatically, even if a spell part or ritual with this ID exists.
     * @param id ID of the source, e.g. the registry ID of the item
     * @param weight Multiplier of the granted rote progress (1.0 = {@link CommonConfig#CHRONOMANCY_PROGRESS})
     */
    public static void registerManual(@NotNull ResourceLocation id, float weight) {
        SOURCES.put(id, weight);
        MANUAL_SOURCES.add(id);
    }

    /**
     * @return Weight of the source: the config entry if there is one, otherwise the registered weight, otherwise 0.
     */
    public static float getWeight(@NotNull ResourceLocation id) {
        Float configWeight = getConfigWeights().get(id);
        return configWeight != null ? configWeight : SOURCES.getOrDefault(id, 0.0f);
    }

    /**
     * Grants rote progress for Accelerate Time, after the player successfully bent time with this source.
     * Call it on the server side.
     * @return true if progress was granted; false if the source has no weight, the player's tier is too low,
     * the spell is already rote or disabled, or the cooldown is still running
     */
    public static boolean grant(@Nullable Player player, @NotNull ResourceLocation id) {
        if (player == null || player.level().isClientSide() || player instanceof FakePlayer || !CommonConfig.ACCELERATE_TIME_ENABLED.get()) {
            return false;
        }

        float amount = getWeight(id) * CommonConfig.CHRONOMANCY_PROGRESS.get();
        SpellEffect part = SpellInit.ACCELERATE_TIME;
        IPlayerProgression progression = player.getCapability(PlayerProgressionProvider.PROGRESSION).resolve().orElse(null);
        IPlayerRoteSpells rote = player.getCapability(PlayerRoteSpellsProvider.ROTE).resolve().orElse(null);
        PlayerDragonMagic dragonMagic = player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).resolve().orElse(null);
        if (amount <= 0 || progression == null || rote == null || dragonMagic == null
                || progression.getTier() < CommonConfig.CHRONOMANCY_REQUIRED_TIER.get() || rote.isRote(part)) {
            return false;
        }

        // game time instead of day time: the sources themselves change the time of day
        long gameTime = player.level().getGameTime();
        long cooldown = CommonConfig.CHRONOMANCY_COOLDOWN.get() * 20L;
        long last = dragonMagic.getLastChronomancyProgress();
        if (cooldown > 0 && last != Long.MIN_VALUE && gameTime >= last && gameTime - last < cooldown) {
            return false;
        }

        dragonMagic.setLastChronomancyProgress(gameTime);
        if (rote.addRoteXP(player, part, amount)) {
            SpellCaster.sendRoteMessage(player, part);
        } else {
            player.displayClientMessage(Component.translatable("dmnr.feedback.chronomancy.progress",
                    Component.translatable(part.getRegistryName().toString()),
                    (int) rote.getRoteProgression(part), part.requiredXPForRote()), true);
        }
        return true;
    }

    /**
     * Counts automatic spell part sources. Only called for components that were not canceled.
     */
    public static void eventHandleComponentApplying(ComponentApplyingEvent event) {
        ResourceLocation id = event.getComponent().getRegistryName();
        if (event.getSource().isPlayerCaster() && isAutomatic(id)) {
            grant(event.getSource().getPlayer(), id);
        }
    }

    /**
     * Counts automatic ritual sources.
     */
    public static void eventHandleRitualComplete(RitualCompleteEvent event) {
        ResourceLocation id = event.getRitual().getRegistryId();
        if (isAutomatic(id)) {
            grant(event.getCaster(), id);
        }
    }

    private static boolean isAutomatic(@Nullable ResourceLocation id) {
        return id != null && !MANUAL_SOURCES.contains(id) && getWeight(id) > 0;
    }

    private static Map<ResourceLocation, Float> getConfigWeights() {
        List<? extends String> config = CommonConfig.CHRONOMANCY_SOURCES.get();
        if (config != parsedConfig) {
            Map<ResourceLocation, Float> weights = new HashMap<>();
            for (String entry : config) {
                try {
                    String[] parts = entry.split("=");
                    weights.put(new ResourceLocation(parts[0].trim()), Float.parseFloat(parts[1].trim()));
                } catch (RuntimeException e) {
                    DragonMagicAndRelics.LOGGER.warn("Ignoring invalid Accelerate Time progress source: " + entry);
                }
            }
            configWeights = weights;
            parsedConfig = config;
        }
        return configWeights;
    }
}
