package de.joh.dragonmagicandrelics.item;

import com.mna.factions.Factions;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.block.BlockInit;
import de.joh.dragonmagicandrelics.item.items.*;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.*;
import de.joh.dragonmagicandrelics.utils.RLoc;
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
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<Item> DRAGON_CORE = ITEMS.register("dragon_core", ()->new DragonCore(new Item.Properties().fireResistant().rarity(Rarity.EPIC).tab(CreativeModeTab.CreativeModeTab)));

    //DragonMageArmor
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_HELMET = ITEMS.register("abyssal_dragon_mage_helmet", () -> new AbyssalDragonMageArmor(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("abyssal_dragon_mage_chestplate", () -> new AbyssalDragonMageArmor(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_LEGGING = ITEMS.register("abyssal_dragon_mage_leggings", () -> new AbyssalDragonMageArmor(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_BOOTS = ITEMS.register("abyssal_dragon_mage_boots", () -> new AbyssalDragonMageArmor(EquipmentSlot.FEET));

    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_HELMET = ITEMS.register("arch_dragon_mage_helmet", () -> new ArchDragonMageArmor(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_CHESTPLATE = ITEMS.register("arch_dragon_mage_chestplate", () -> new ArchDragonMageArmor(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_LEGGING = ITEMS.register("arch_dragon_mage_leggings", () -> new ArchDragonMageArmor(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_BOOTS = ITEMS.register("arch_dragon_mage_boots", () -> new ArchDragonMageArmor(EquipmentSlot.FEET));

    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_HELMET = ITEMS.register("infernal_dragon_mage_helmet", () -> new InfernalDragonMageArmor(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("infernal_dragon_mage_chestplate", () -> new InfernalDragonMageArmor(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_LEGGING = ITEMS.register("infernal_dragon_mage_leggings", () -> new InfernalDragonMageArmor(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_BOOTS = ITEMS.register("infernal_dragon_mage_boots", () -> new InfernalDragonMageArmor(EquipmentSlot.FEET));

    public static final RegistryObject<Item> WILD_DRAGON_MAGE_HELMET = ITEMS.register("wild_dragon_mage_helmet", () -> new WildDragonMageArmor(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_CHESTPLATE = ITEMS.register("wild_dragon_mage_chestplate", () -> new WildDragonMageArmor(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_LEGGING = ITEMS.register("wild_dragon_mage_leggings", () -> new WildDragonMageArmor(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_BOOTS = ITEMS.register("wild_dragon_mage_boots", () -> new WildDragonMageArmor(EquipmentSlot.FEET));

    //Curios
    //public static final RegistryObject<Item> DRAGON_MAGE_TEST_CURIOS = ITEMS.register("dragon_mage_test_curios", () -> new DragonMageCurios(16, "dm_test_curios", new Item.Properties().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> RING_OF_POWER = ITEMS.register("ring_of_power", RingOfPower::new);
    public static final RegistryObject<Item> RING_OF_RULING = ITEMS.register("ring_of_ruling", RingOfRuling::new);

    public static final RegistryObject<Item> BRACELET_OF_FRIENDSHIP = ITEMS.register("bracelet_of_friendship", () -> new BraceletOfFriendship(new Item.Properties().tab(CreativeModeTab.CreativeModeTab).stacksTo(1)));
    public static final RegistryObject<Item> FACTION_AMULET = ITEMS.register("faction_amulet", ()->new FactionAmulet(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(CreativeModeTab.CreativeModeTab)));
    public static final RegistryObject<Item> ANGEL_RING = ITEMS.register("angel_ring", ()->new AngelRing(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.CreativeModeTab), Factions.FEY.getRegistryName()));
    public static final RegistryObject<Item> FALLEN_ANGEL_RING = ITEMS.register("fallen_angel_ring", ()->new AngelRing(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.CreativeModeTab), Factions.UNDEAD.getRegistryName()));
    public static final RegistryObject<Item> VOIDFEATHER_CHARM = ITEMS.register("voidfeather_charm", () -> new VoidfeatherCharm((new Item.Properties()).setNoRepair().stacksTo(1).durability(1).tab(CreativeModeTab.CreativeModeTab)));

    //Other
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new Mutandis((new Item.Properties()).tab(CreativeModeTab.CreativeModeTab)));
    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", ManaCake::new);
    public static final RegistryObject<Item> RIFT_EMITTER_ITEM = ITEMS.register("rift_emitter", () -> new RiftEmitterItem(BlockInit.RIFT_EMITTER.get(), new Item.Properties().stacksTo(1).tab(CreativeModeTab.CreativeModeTab)));
    public static final RegistryObject<Item> BRIMSTONE_CHALK = ITEMS.register("brimstone_chalk", BrimstoneChalk::new);



    //ARMOR UPGRADES:
    public static final RegistryObject<Item> UPGRADE_SEAL_FLY = ITEMS.register("upgrade_seal_fly", () -> new UpgradeSeal(RLoc.create("armorupgrade/fly"), Rarity.RARE));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST = ITEMS.register("upgrade_seal_damage_boost", () -> new UpgradeSeal(RLoc.create("armorupgrade/damage_boost"), Rarity.UNCOMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_RESISTANCE = ITEMS.register("upgrade_seal_damage_resistance", () -> new UpgradeSeal(RLoc.create("armorupgrade/damage_resistance"), Rarity.RARE));
    public static final RegistryObject<Item> UPGRADE_SEAL_DOLPHINS_GRACE = ITEMS.register("upgrade_seal_dolphins_grace", () -> new UpgradeSeal(RLoc.create("armorupgrade/dolphins_grace"), Rarity.COMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_REACH_DISTANCE = ITEMS.register("upgrade_seal_reach_distance", () -> new UpgradeSeal(RLoc.create("armorupgrade/reach_distance"), Rarity.EPIC));

    public static final RegistryObject<Item> UPGRADE_SEAL_ELYTRA = ITEMS.register("upgrade_seal_elytra", () -> new UpgradeSeal(RLoc.create("armorupgrade/elytra"), Rarity.UNCOMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_ANGEL_FLIGHT = ITEMS.register("upgrade_seal_angel_flight", () -> new UpgradeSeal(RLoc.create("armorupgrade/angel_flight"), Rarity.EPIC));
    public static final RegistryObject<Item> UPGRADE_SEAL_EXPLOSION_RESISTANCE = ITEMS.register("upgrade_seal_explosion_resistance", () -> new UpgradeSeal(RLoc.create("armorupgrade/explosion_resistance"), Rarity.RARE));
    public static final RegistryObject<Item> UPGRADE_SEAL_MAJOR_FIRE_RESISTANCE = ITEMS.register("upgrade_seal_major_fire_resistance", () -> new UpgradeSeal(RLoc.create("armorupgrade/major_fire_resistance"), Rarity.UNCOMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_MINOR_FIRE_RESISTANCE = ITEMS.register("upgrade_seal_minor_fire_resistance", () -> new UpgradeSeal(RLoc.create("armorupgrade/minor_fire_resistance"), Rarity.COMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST = ITEMS.register("upgrade_seal_health_boost", () -> new UpgradeSeal(RLoc.create("armorupgrade/health_boost"), Rarity.UNCOMMON));

    public static final RegistryObject<Item> UPGRADE_SEAL_JUMP = ITEMS.register("upgrade_seal_jump", () -> new UpgradeSeal(RLoc.create("armorupgrade/jump"), Rarity.COMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_KINETIC_RESISTANCE = ITEMS.register("upgrade_seal_kinetic_resistance", () -> new UpgradeSeal(RLoc.create("armorupgrade/kinetic_resistance"), Rarity.UNCOMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_BOOST = ITEMS.register("upgrade_seal_mana_boost", () -> new UpgradeSeal(RLoc.create("armorupgrade/mana_boost"), Rarity.RARE));
    public static final RegistryObject<Item> UPGRADE_SEAL_METEOR_JUMP = ITEMS.register("upgrade_seal_meteor_jump", () -> new UpgradeSeal(RLoc.create("armorupgrade/meteor_jump"), Rarity.UNCOMMON));

    public static final RegistryObject<Item> UPGRADE_SEAL_MIST_FORM = ITEMS.register("upgrade_seal_mist_form", () -> new UpgradeSeal(RLoc.create("armorupgrade/mist_form"), Rarity.EPIC));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED = ITEMS.register("upgrade_seal_movement_speed", () -> new UpgradeSeal(RLoc.create("armorupgrade/movement_speed"), Rarity.UNCOMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_NIGHT_VISION = ITEMS.register("upgrade_seal_night_vision", () -> new UpgradeSeal(RLoc.create("armorupgrade/night_vision"), Rarity.COMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_PROJECTILE_REFLECTION = ITEMS.register("upgrade_seal_projectile_reflection", () -> new UpgradeSeal(RLoc.create("armorupgrade/projectile_reflection"), Rarity.RARE));

    public static final RegistryObject<Item> UPGRADE_SEAL_REGENERATION = ITEMS.register("upgrade_seal_regeneration", () -> new UpgradeSeal(RLoc.create("armorupgrade/regeneration"), Rarity.EPIC));
    public static final RegistryObject<Item> UPGRADE_SEAL_SATURATION = ITEMS.register("upgrade_seal_saturation", () -> new UpgradeSeal(RLoc.create("armorupgrade/saturation"), Rarity.RARE));
    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING = ITEMS.register("upgrade_seal_water_breathing", () -> new UpgradeSeal(RLoc.create("armorupgrade/water_breathing"), Rarity.COMMON));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN = ITEMS.register("upgrade_seal_mana_regen", () -> new UpgradeSeal(RLoc.create("armorupgrade/mana_regen"), Rarity.RARE));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
