package de.joh.dragonmagicandrelics.item;

import com.mna.api.capabilities.Faction;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.block.BlockInit;
import de.joh.dragonmagicandrelics.item.items.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

    public static final RegistryObject<Item> DRAGON_CORE = ITEMS.register("dragon_core", ()->new DragonCore(new Item.Properties().fireResistant().rarity(Rarity.EPIC).tab(CreativeModeTab.CreativeModeTab)));

    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", ManaCake::new);

    public static final RegistryObject<Item> FACTION_AMULET = ITEMS.register("faction_amulet", ()->new FactionAmulet(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(CreativeModeTab.CreativeModeTab)));

    public static final RegistryObject<Item> RIFT_EMITTER_ITEM = ITEMS.register("rift_emitter", () -> new RiftEmitterItem(BlockInit.RIFT_EMITTER.get(), new Item.Properties().stacksTo(1).tab(CreativeModeTab.CreativeModeTab)));

    public static final RegistryObject<Item> BRIMSTONE_CHALK = ITEMS.register("brimstone_chalk", () -> new BrimstoneChalk(new Item.Properties().stacksTo(1).fireResistant().tab(CreativeModeTab.CreativeModeTab)));

    public static final RegistryObject<Item> VOIDFEATHER_CHARM = ITEMS.register("voidfeather_charm", () -> new VoidfeatherCharm((new Item.Properties()).setNoRepair().stacksTo(1).durability(1).tab(CreativeModeTab.CreativeModeTab)));

    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_HELMET = ITEMS.register("abyssal_dragon_mage_helmet", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.HEAD, Faction.UNDEAD));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("abyssal_dragon_mage_chestplate", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.CHEST, Faction.UNDEAD));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_LEGGING = ITEMS.register("abyssal_dragon_mage_leggings", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.LEGS, Faction.UNDEAD));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_BOOTS = ITEMS.register("abyssal_dragon_mage_boots", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.FEET, Faction.UNDEAD));

    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_HELMET = ITEMS.register("arch_dragon_mage_helmet", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.HEAD, Faction.ANCIENT_WIZARDS));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_CHESTPLATE = ITEMS.register("arch_dragon_mage_chestplate", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.CHEST, Faction.ANCIENT_WIZARDS));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_LEGGING = ITEMS.register("arch_dragon_mage_leggings", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.LEGS, Faction.ANCIENT_WIZARDS));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_BOOTS = ITEMS.register("arch_dragon_mage_boots", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.FEET, Faction.ANCIENT_WIZARDS));

    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_HELMET = ITEMS.register("infernal_dragon_mage_helmet", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.HEAD, Faction.DEMONS));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("infernal_dragon_mage_chestplate", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.CHEST, Faction.DEMONS));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_LEGGING = ITEMS.register("infernal_dragon_mage_leggings", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.LEGS, Faction.DEMONS));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_BOOTS = ITEMS.register("infernal_dragon_mage_boots", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.FEET, Faction.DEMONS));

    public static final RegistryObject<Item> WILD_DRAGON_MAGE_HELMET = ITEMS.register("wild_dragon_mage_helmet", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.HEAD, Faction.FEY_COURT));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_CHESTPLATE = ITEMS.register("wild_dragon_mage_chestplate", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.CHEST, Faction.FEY_COURT));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_LEGGING = ITEMS.register("wild_dragon_mage_leggings", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.LEGS, Faction.FEY_COURT));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_BOOTS = ITEMS.register("wild_dragon_mage_boots", () -> new DragonMageArmor(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, EquipmentSlot.FEET, Faction.FEY_COURT));


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

    public static final RegistryObject<Item> UPGRADE_SEAL_METEOR_JUMP = ITEMS.register("upgrade_seal_meteor_jump", () -> new UpgradeSeal(3, "meteor_jump", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_MIST_FORM = ITEMS.register("upgrade_seal_mist_form", () -> new UpgradeSeal(4, "mist_form", 1));


    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_I = ITEMS.register("upgrade_seal_movement_speed_i", () -> new UpgradeSeal(1, "movement_speed"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_II = ITEMS.register("upgrade_seal_movement_speed_ii", () -> new UpgradeSeal(2, "movement_speed"));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED_III = ITEMS.register("upgrade_seal_movement_speed_iii", () -> new UpgradeSeal(3, "movement_speed"));

    public static final RegistryObject<Item> UPGRADE_SEAL_NIGHT_VISION = ITEMS.register("upgrade_seal_night_vision", () -> new UpgradeSeal(1, "night_vision"));

    public static final RegistryObject<Item> UPGRADE_SEAL_PROJECTILE_REFLECTION_I = ITEMS.register("upgrade_seal_projectile_reflection_i", () -> new UpgradeSeal(2, "projectile_reflection", 1));
    public static final RegistryObject<Item> UPGRADE_SEAL_PROJECTILE_REFLECTION_II = ITEMS.register("upgrade_seal_projectile_reflection_ii", () -> new UpgradeSeal(3, "projectile_reflection",2));
    public static final RegistryObject<Item> UPGRADE_SEAL_PROJECTILE_REFLECTION_III = ITEMS.register("upgrade_seal_projectile_reflection_iii", () -> new UpgradeSeal(4, "projectile_reflection",3));

    public static final RegistryObject<Item> UPGRADE_SEAL_REGENERATION = ITEMS.register("upgrade_seal_regeneration", () -> new UpgradeSeal(4, "regeneration", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_SATURATION = ITEMS.register("upgrade_seal_saturation", () -> new UpgradeSeal(3, "saturation", 1));

    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING_I = ITEMS.register("upgrade_seal_water_breathing_i", () -> new UpgradeSeal(1, "water_breathing"));
    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING_II = ITEMS.register("upgrade_seal_water_breathing_ii", () -> new UpgradeSeal(2, "water_breathing"));

    public static final RegistryObject<Item> UPGRADE_SEAL_WELLSPRING_SIGHT = ITEMS.register("upgrade_seal_wellspring_sight", () -> new UpgradeSeal(1, "wellspring_sight", 1));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
