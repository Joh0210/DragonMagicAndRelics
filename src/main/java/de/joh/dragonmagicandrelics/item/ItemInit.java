package de.joh.dragonmagicandrelics.item;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.item.items.ManaCake;
import de.joh.dragonmagicandrelics.item.items.ArmorMaterials;
import de.joh.dragonmagicandrelics.item.items.UpgradeSeal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Inits of all mod items.
 * Registration through DragonMagicAndRelics
 * @see DragonMagicAndRelics
 * @author Joh0210
 */
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", () -> new ManaCake());

    public static final RegistryObject<Item> DRAGON_MAGE_HELMET = ITEMS.register("dragon_mage_helmet", () -> {
        return new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.HEAD);
    });
    public static final RegistryObject<Item> DRAGON_MAGE_CHESTPLATE = ITEMS.register("dragon_mage_chestplate", () -> {
        return new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.CHEST);
    });
    public static final RegistryObject<Item> DRAGON_MAGE_LEGGING = ITEMS.register("dragon_mage_leggings", () -> {
        return new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.LEGS);
    });
    public static final RegistryObject<Item> DRAGON_MAGE_BOOTS = ITEMS.register("dragon_mage_boots", () -> {
        return new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.FEET);
    });

    //ARMOR UPGRADES:
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST_I = ITEMS.register("upgrade_seal_damage_boost_i", () -> new UpgradeSeal(2, "damage_boost", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST_II = ITEMS.register("upgrade_seal_damage_boost_ii", () -> new UpgradeSeal(3, "damage_boost", 2));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST_III = ITEMS.register("upgrade_seal_damage_boost_iii", () -> new UpgradeSeal(4, "damage_boost", 3));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST_IV = ITEMS.register("upgrade_seal_damage_boost_iv", () -> new UpgradeSeal(4, "damage_boost", 4));

    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_RESISTANCE_I = ITEMS.register("upgrade_seal_damage_resistance_i", () -> new UpgradeSeal(3, "damage_resistance", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_RESISTANCE_II = ITEMS.register("upgrade_seal_damage_resistance_ii", () -> new UpgradeSeal(4, "damage_resistance", 2));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_RESISTANCE_III = ITEMS.register("upgrade_seal_damage_resistance_iii", () -> new UpgradeSeal(4, "damage_resistance", 3));

    public static final RegistryObject<Item> UPGRADE_SEAL_DOLPHINS_GRACE_I = ITEMS.register("upgrade_seal_dolphins_grace_i", () -> new UpgradeSeal(1, "dolphins_grace"));
    public static final RegistryObject<Item> UPGRADE_SEAL_DOLPHINS_GRACE_II = ITEMS.register("upgrade_seal_dolphins_grace_ii", () -> new UpgradeSeal(2, "dolphins_grace", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_ELDRIN_SIGHT = ITEMS.register("upgrade_seal_eldrin_sight", () -> new UpgradeSeal(3, "eldrin_sight", 1)); //Eldrin Sight pot. als Upgrade von Wellspring sight

    public static final RegistryObject<Item> UPGRADE_SEAL_ELYTRA_I = ITEMS.register("upgrade_seal_elytra_i", () -> new UpgradeSeal(2, "elytra", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_ELYTRA_II = ITEMS.register("upgrade_seal_elytra_ii", () -> new UpgradeSeal(4, "elytra", 2));

    public static final RegistryObject<Item> UPGRADE_SEAL_EXPLOSION_RESISTANCE = ITEMS.register("upgrade_seal_explosion_resistance", () -> new UpgradeSeal(4, "explosion_resistance", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_FIRE_RESISTANCE_I = ITEMS.register("upgrade_seal_fire_resistance_i", () -> new UpgradeSeal(2, "fire_resistance", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_FIRE_RESISTANCE_II = ITEMS.register("upgrade_seal_fire_resistance_ii", () -> new UpgradeSeal(3, "fire_resistance", 2));

    public static final RegistryObject<Item> UPGRADE_SEAL_FLY_I = ITEMS.register("upgrade_seal_fly_i", () -> new UpgradeSeal(2, "fly", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_FLY_II = ITEMS.register("upgrade_seal_fly_ii", () -> new UpgradeSeal(4, "fly", 2));

    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST_I = ITEMS.register("upgrade_seal_health_boost_i", () -> new UpgradeSeal(1, "health_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST_II = ITEMS.register("upgrade_seal_health_boost_ii", () -> new UpgradeSeal(2, "health_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST_III = ITEMS.register("upgrade_seal_health_boost_iii", () -> new UpgradeSeal(3, "health_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST_IV = ITEMS.register("upgrade_seal_health_boost_iv", () -> new UpgradeSeal(4, "health_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST_V = ITEMS.register("upgrade_seal_health_boost_v", () -> new UpgradeSeal(4, "health_boost", 5));

    public static final RegistryObject<Item> UPGRADE_SEAL_JUMP_I = ITEMS.register("upgrade_seal_jump_i", () -> new UpgradeSeal(1, "jump", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_JUMP_II = ITEMS.register("upgrade_seal_jump_ii", () -> new UpgradeSeal(1, "jump", 2));
    public static final RegistryObject<Item> UPGRADE_SEAL_JUMP_III = ITEMS.register("upgrade_seal_jump_iii", () -> new UpgradeSeal(2, "jump", 3));

    public static final RegistryObject<Item> UPGRADE_SEAL_KINETIC_RESISTANCE = ITEMS.register("upgrade_seal_kinetic_resistance", () -> new UpgradeSeal(2, "kinetic_resistance", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST_I = ITEMS.register("upgrade_seal_mana_boost_i", () -> new UpgradeSeal(1, "mana_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST_II = ITEMS.register("upgrade_seal_mana_boost_ii", () -> new UpgradeSeal(2, "mana_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST_III = ITEMS.register("upgrade_seal_mana_boost_iii", () -> new UpgradeSeal(3, "mana_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST_IV = ITEMS.register("upgrade_seal_mana_boost_iv", () -> new UpgradeSeal(4, "mana_boost"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST_V = ITEMS.register("upgrade_seal_mana_boost_v", () -> new UpgradeSeal(4, "mana_boost", 5));

    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN_I = ITEMS.register("upgrade_seal_mana_regen_i", () -> new UpgradeSeal(1, "mana_regen"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN_II = ITEMS.register("upgrade_seal_mana_regen_ii", () -> new UpgradeSeal(2, "mana_regen"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN_III = ITEMS.register("upgrade_seal_mana_regen_iii", () -> new UpgradeSeal(3, "mana_regen"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN_IV = ITEMS.register("upgrade_seal_mana_regen_iv", () -> new UpgradeSeal(4, "mana_regen"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN_V = ITEMS.register("upgrade_seal_mana_regen_v", () -> new UpgradeSeal(4, "mana_regen", 5));

    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_I = ITEMS.register("upgrade_seal_movement_speed_i", () -> new UpgradeSeal(1, "movement_speed"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_II = ITEMS.register("upgrade_seal_movement_speed_ii", () -> new UpgradeSeal(2, "movement_speed"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_III = ITEMS.register("upgrade_seal_movement_speed_iii", () -> new UpgradeSeal(3, "movement_speed"));

    public static final RegistryObject<Item> UPGRADE_SEAL_NIGHT_VISION = ITEMS.register("upgrade_seal_night_vision", () -> new UpgradeSeal(1, "night_vision"));

    public static final RegistryObject<Item> UPGRADE_SEAL_REGENERATION = ITEMS.register("upgrade_seal_regeneration", () -> new UpgradeSeal(4, "regeneration", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_SATURATION = ITEMS.register("upgrade_seal_saturation", () -> new UpgradeSeal(3, "saturation", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING_I = ITEMS.register("upgrade_seal_water_breathing_i", () -> new UpgradeSeal(1, "water_breathing"));
    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING_II = ITEMS.register("upgrade_seal_water_breathing_ii", () -> new UpgradeSeal(2, "water_breathing"));

    public static final RegistryObject<Item> UPGRADE_SEAL_WELLSPRING_SIGHT = ITEMS.register("upgrade_seal_wellspring_sight", () -> new UpgradeSeal(1, "wellspring_sight", 1));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
