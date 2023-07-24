package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * Ensures that all missing items are remapped
 * @author Joh0210
 */
@Mod.EventBusSubscriber
public class MissingMappingsHandler {
    private static final Map<String, ResourceLocation> reMap = new HashMap<>();

    public MissingMappingsHandler() {
    }

    @SubscribeEvent
    public static void remapBlocks(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings(DragonMagicAndRelics.MOD_ID)) {
            ResourceLocation key = mapping.key;
            String path = key.getPath();
            ResourceLocation remappedId = reMap.get(path);
            if (remappedId != null) {
                Block remapped = ForgeRegistries.BLOCKS.getValue(remappedId);
                if (remapped != null) {
                    DragonMagicAndRelics.LOGGER.warn("Remapping block '{}' to '{}'", key, remappedId);

                    try {
                        mapping.remap(remapped);
                    } catch (Throwable var8) {
                        DragonMagicAndRelics.LOGGER.warn("Remapping block '{}' to '{}' failed: {}", key, remappedId, var8);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void remapItems(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings(DragonMagicAndRelics.MOD_ID)) {
            ResourceLocation key = mapping.key;
            String path = key.getPath();
            ResourceLocation remappedId = reMap.get(path);
            if (remappedId != null) {
                Item remapped = ForgeRegistries.ITEMS.getValue(remappedId);
                if (remapped != null) {
                    DragonMagicAndRelics.LOGGER.warn("Remapping item '{}' to '{}'", key, remappedId);

                    try {
                        mapping.remap(remapped);
                    } catch (Throwable var8) {
                        DragonMagicAndRelics.LOGGER.warn("Remapping item '{}' to '{}' failed: {}", key, remappedId, var8);
                    }
                }
            }
        }

    }

    static {
        reMap.put("upgrade_seal_damage_boost_i", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMap.put("upgrade_seal_damage_boost_ii", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMap.put("upgrade_seal_damage_boost_iii", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMap.put("upgrade_seal_damage_boost_iv", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMap.put("upgrade_seal_damage_resistance_i", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMap.put("upgrade_seal_damage_resistance_ii", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMap.put("upgrade_seal_damage_resistance_iii", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMap.put("upgrade_seal_dolphins_grace_i", ItemInit.UPGRADE_SEAL_DOLPHINS_GRACE.getId());
        reMap.put("upgrade_seal_dolphins_grace_ii", ItemInit.UPGRADE_SEAL_DOLPHINS_GRACE.getId());
        reMap.put("upgrade_seal_eldrin_sight", Items.NETHER_STAR.getRegistryName()); //Not 1:1 the same, but at least a trade of
        reMap.put("upgrade_seal_elytra_i", ItemInit.UPGRADE_SEAL_ELYTRA.getId());
        reMap.put("upgrade_seal_elytra_ii", ItemInit.UPGRADE_SEAL_ANGEL_FLIGHT.getId());
        reMap.put("upgrade_seal_explosion_resistance", ItemInit.UPGRADE_SEAL_EXPLOSION_RESISTANCE.getId());
        reMap.put("upgrade_seal_fire_resistance_i", ItemInit.UPGRADE_SEAL_MINOR_FIRE_RESISTANCE.getId());
        reMap.put("upgrade_seal_fire_resistance_ii", ItemInit.UPGRADE_SEAL_MAJOR_FIRE_RESISTANCE.getId());
        reMap.put("upgrade_seal_fly_i", ItemInit.UPGRADE_SEAL_FLY.getId());
        reMap.put("upgrade_seal_fly_ii", ItemInit.UPGRADE_SEAL_FLY.getId());
        reMap.put("upgrade_seal_health_boost_i", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMap.put("upgrade_seal_health_boost_ii", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMap.put("upgrade_seal_health_boost_iii", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMap.put("upgrade_seal_health_boost_iv", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMap.put("upgrade_seal_health_boost_v", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMap.put("upgrade_seal_jump_i", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMap.put("upgrade_seal_jump_ii", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMap.put("upgrade_seal_jump_iii", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMap.put("upgrade_seal_kinetic_resistance", ItemInit.UPGRADE_SEAL_KINETIC_RESISTANCE.getId());
        reMap.put("upgrade_seal_mana_boost_i", ItemInit.UPGRADE_SEAL_MANA_BOOST.getId());
        reMap.put("upgrade_seal_mana_boost_ii", ItemInit.UPGRADE_SEAL_MANA_BOOST.getId());
        reMap.put("upgrade_seal_mana_boost_iii", ItemInit.UPGRADE_SEAL_MANA_BOOST.getId());
        reMap.put("upgrade_seal_mana_boost_iv", ItemInit.UPGRADE_SEAL_MANA_BOOST.getId());
        reMap.put("upgrade_seal_mana_boost_v", ItemInit.UPGRADE_SEAL_MANA_BOOST.getId());
        reMap.put("upgrade_seal_mana_regen_i", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMap.put("upgrade_seal_mana_regen_ii", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMap.put("upgrade_seal_mana_regen_iii", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMap.put("upgrade_seal_mana_regen_iv", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMap.put("upgrade_seal_mana_regen_v", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMap.put("upgrade_seal_meteor_jump", ItemInit.UPGRADE_SEAL_METEOR_JUMP.getId());
        reMap.put("upgrade_seal_mist_form", ItemInit.UPGRADE_SEAL_MIST_FORM.getId());
        reMap.put("upgrade_seal_movement_speed_i", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMap.put("upgrade_seal_movement_speed_ii", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMap.put("upgrade_seal_movement_speed_iii", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMap.put("upgrade_seal_night_vision", ItemInit.UPGRADE_SEAL_NIGHT_VISION.getId());
        reMap.put("upgrade_seal_projectile_reflection_i", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMap.put("upgrade_seal_projectile_reflection_ii", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMap.put("upgrade_seal_projectile_reflection_iii", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMap.put("upgrade_seal_regeneration", ItemInit.UPGRADE_SEAL_REGENERATION.getId());
        reMap.put("upgrade_seal_saturation", ItemInit.UPGRADE_SEAL_SATURATION.getId());
        reMap.put("upgrade_seal_water_breathing_i", ItemInit.UPGRADE_SEAL_WATER_BREATHING.getId());
        reMap.put("upgrade_seal_water_breathing_ii", ItemInit.UPGRADE_SEAL_WATER_BREATHING.getId());
        reMap.put("upgrade_seal_wellspring_sight", com.mna.items.ItemInit.WELLSPRING_DOWSING_ROD.getId()); //Not the same, but at least a trade of
    }
}
