package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
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
    private static final Map<String, ResourceLocation> reMapItem = new HashMap<>();
    private static final Map<String, ArmorUpgrade> reMapTag = new HashMap<>();

    public MissingMappingsHandler() {
    }

    @SubscribeEvent
    public static void remapBlocks(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings(DragonMagicAndRelics.MOD_ID)) {
            ResourceLocation key = mapping.key;
            String path = key.getPath();
            ResourceLocation remappedId = reMapItem.get(path);
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
            ResourceLocation remappedId = reMapItem.get(path);
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

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event){
        if(event.getEntity() instanceof Player && event.getSlot() == EquipmentSlot.CHEST){
            if(event.getTo().getItem() instanceof DragonMageArmor){
                updateTag(event.getTo());
            }
        }
    }

    //todo: Delete in an later version
    private static void updateTag(ItemStack chest){
        if(chest.hasTag() && !chest.getTag().getBoolean(DragonMagicAndRelics.MOD_ID + "is_up_to_date")){
            for (Map.Entry<String, ArmorUpgrade> entry : reMapTag.entrySet()){
                if(!chest.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + entry.getKey()).equals("")){
                    try{
                        int level = Integer.parseInt(chest.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + entry.getKey()));
                        ArmorUpgrade armorUpgrade = entry.getValue();
                        if (armorUpgrade == ArmorUpgradeInit.ELYTRA && level >= 2){
                            armorUpgrade = ArmorUpgradeInit.ANGEL_FLIGHT;
                            level -= 1;
                        } else if (armorUpgrade == ArmorUpgradeInit.MINOR_FIRE_RESISTANCE && level >= 2){
                            armorUpgrade = ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE;
                            level -= 1;
                        } else if (armorUpgrade == ArmorUpgradeInit.MANA_BOOST && level >= 4){
                            armorUpgrade = ArmorUpgradeInit.MAJOR_MANA_BOOST;
                            level -= 1;
                        } else if (armorUpgrade == ArmorUpgradeInit.MANA_REGEN){
                            level = Math.round(level/2.0F);
                        }

                        ((DragonMageArmor)chest.getItem()).addDragonMagicToItem(chest, armorUpgrade, level, true);
                    }catch (NumberFormatException ignored){
                    }

                    chest.getTag().remove(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + entry.getKey());
                }
            }

            chest.getTag().putBoolean(DragonMagicAndRelics.MOD_ID + "is_up_to_date", true);
        }
    }

    static {
        reMapTag.put("fly", ArmorUpgradeInit.FLY);
        reMapTag.put("saturation", ArmorUpgradeInit.SATURATION);
        reMapTag.put("movement_speed", ArmorUpgradeInit.MOVEMENT_SPEED);
        reMapTag.put("water_breathing", ArmorUpgradeInit.WATER_BREATHING);
        reMapTag.put("meteor_jump", ArmorUpgradeInit.METEOR_JUMP);
        reMapTag.put("dolphins_grace", ArmorUpgradeInit.DOLPHINS_GRACE);
        reMapTag.put("regeneration", ArmorUpgradeInit.REGENERATION);
        //reMapTag.put("wellspring_sight", ArmorUpgradeInit.);
        //reMapTag.put("eldrin_sight", ArmorUpgradeInit.);
        reMapTag.put("mana_boost", ArmorUpgradeInit.MANA_BOOST);
        reMapTag.put("mana_regen", ArmorUpgradeInit.MANA_REGEN);
        reMapTag.put("health_boost", ArmorUpgradeInit.HEALTH_BOOST);
        reMapTag.put("damage_resistance", ArmorUpgradeInit.DAMAGE_RESISTANCE);
        reMapTag.put("damage_boost", ArmorUpgradeInit.DAMAGE_BOOST);
        reMapTag.put("fire_resistance", ArmorUpgradeInit.MINOR_FIRE_RESISTANCE);
        reMapTag.put("kinetic_resistance", ArmorUpgradeInit.KINETIC_RESISTANCE);
        reMapTag.put("explosion_resistance", ArmorUpgradeInit.EXPLOSION_RESISTANCE);
        reMapTag.put("projectile_reflection", ArmorUpgradeInit.PROJECTILE_REFLECTION);
        reMapTag.put("mist_form", ArmorUpgradeInit.MIST_FORM);
        reMapTag.put("jump", ArmorUpgradeInit.JUMP);
        reMapTag.put("elytra", ArmorUpgradeInit.ELYTRA);
        reMapTag.put("night_vision", ArmorUpgradeInit.NIGHT_VISION);


        reMapItem.put("upgrade_seal_damage_boost_i", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMapItem.put("upgrade_seal_damage_boost_ii", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMapItem.put("upgrade_seal_damage_boost_iii", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMapItem.put("upgrade_seal_damage_boost_iv", ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.getId());
        reMapItem.put("upgrade_seal_damage_resistance_i", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_damage_resistance_ii", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_damage_resistance_iii", ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_dolphins_grace_i", ItemInit.UPGRADE_SEAL_DOLPHINS_GRACE.getId());
        reMapItem.put("upgrade_seal_dolphins_grace_ii", ItemInit.UPGRADE_SEAL_DOLPHINS_GRACE.getId());
        reMapItem.put("upgrade_seal_eldrin_sight", Items.NETHER_STAR.getRegistryName()); //Not 1:1 the same, but at least a trade of
        reMapItem.put("upgrade_seal_elytra_i", ItemInit.UPGRADE_SEAL_ELYTRA.getId());
        reMapItem.put("upgrade_seal_elytra_ii", ItemInit.UPGRADE_SEAL_ANGEL_FLIGHT.getId());
        reMapItem.put("upgrade_seal_explosion_resistance", ItemInit.UPGRADE_SEAL_EXPLOSION_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_fire_resistance_i", ItemInit.UPGRADE_SEAL_MINOR_FIRE_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_fire_resistance_ii", ItemInit.UPGRADE_SEAL_MAJOR_FIRE_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_fly_i", ItemInit.UPGRADE_SEAL_FLY.getId());
        reMapItem.put("upgrade_seal_fly_ii", ItemInit.UPGRADE_SEAL_FLY.getId());
        reMapItem.put("upgrade_seal_health_boost_i", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMapItem.put("upgrade_seal_health_boost_ii", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMapItem.put("upgrade_seal_health_boost_iii", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMapItem.put("upgrade_seal_health_boost_iv", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMapItem.put("upgrade_seal_health_boost_v", ItemInit.UPGRADE_SEAL_HEALTH_BOOST.getId());
        reMapItem.put("upgrade_seal_jump_i", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMapItem.put("upgrade_seal_jump_ii", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMapItem.put("upgrade_seal_jump_iii", ItemInit.UPGRADE_SEAL_JUMP.getId());
        reMapItem.put("upgrade_seal_kinetic_resistance", ItemInit.UPGRADE_SEAL_KINETIC_RESISTANCE.getId());
        reMapItem.put("upgrade_seal_mana_boost_i", ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.getId());
        reMapItem.put("upgrade_seal_mana_boost_ii", ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.getId());
        reMapItem.put("upgrade_seal_mana_boost_iii", ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.getId());
        reMapItem.put("upgrade_seal_mana_boost_iv", ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.getId());
        reMapItem.put("upgrade_seal_mana_boost_v", ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.getId());
        reMapItem.put("upgrade_seal_mana_regen_i", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMapItem.put("upgrade_seal_mana_regen_ii", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMapItem.put("upgrade_seal_mana_regen_iii", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMapItem.put("upgrade_seal_mana_regen_iv", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMapItem.put("upgrade_seal_mana_regen_v", ItemInit.UPGRADE_SEAL_MANA_REGEN.getId());
        reMapItem.put("upgrade_seal_meteor_jump", ItemInit.UPGRADE_SEAL_METEOR_JUMP.getId());
        reMapItem.put("upgrade_seal_mist_form", ItemInit.UPGRADE_SEAL_MIST_FORM.getId());
        reMapItem.put("upgrade_seal_movement_speed_i", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMapItem.put("upgrade_seal_movement_speed_ii", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMapItem.put("upgrade_seal_movement_speed_iii", ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.getId());
        reMapItem.put("upgrade_seal_night_vision", ItemInit.UPGRADE_SEAL_NIGHT_VISION.getId());
        reMapItem.put("upgrade_seal_projectile_reflection_i", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMapItem.put("upgrade_seal_projectile_reflection_ii", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMapItem.put("upgrade_seal_projectile_reflection_iii", ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.getId());
        reMapItem.put("upgrade_seal_regeneration", ItemInit.UPGRADE_SEAL_REGENERATION.getId());
        reMapItem.put("upgrade_seal_saturation", ItemInit.UPGRADE_SEAL_SATURATION.getId());
        reMapItem.put("upgrade_seal_water_breathing_i", ItemInit.UPGRADE_SEAL_WATER_BREATHING.getId());
        reMapItem.put("upgrade_seal_water_breathing_ii", ItemInit.UPGRADE_SEAL_WATER_BREATHING.getId());
        reMapItem.put("upgrade_seal_wellspring_sight", com.mna.items.ItemInit.WELLSPRING_DOWSING_ROD.getId()); //Not the same, but at least a trade of
    }
}
