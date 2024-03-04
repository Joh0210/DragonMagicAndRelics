package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.item.BaseTieredItem;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.common.item.dragonmagearmor.AbyssalDragonMageArmorItem;
import de.joh.dmnr.common.item.dragonmagearmor.ArchDragonMageArmorItem;
import de.joh.dmnr.common.item.dragonmagearmor.InfernalDragonMageArmorItem;
import de.joh.dmnr.common.item.dragonmagearmor.WildDragonMageArmorItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Inits of all mod items
 * @author Joh0210
 */
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<Item> DRAGON_CORE = ITEMS.register("dragon_core", ()->new DragonCoreItem(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    //DragonMageArmor
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_HELMET = ITEMS.register("abyssal_dragon_mage_helmet", () -> new AbyssalDragonMageArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("abyssal_dragon_mage_chestplate", () -> new AbyssalDragonMageArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_LEGGING = ITEMS.register("abyssal_dragon_mage_leggings", () -> new AbyssalDragonMageArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> ABYSSAL_DRAGON_MAGE_BOOTS = ITEMS.register("abyssal_dragon_mage_boots", () -> new AbyssalDragonMageArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_HELMET = ITEMS.register("arch_dragon_mage_helmet", () -> new ArchDragonMageArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_CHESTPLATE = ITEMS.register("arch_dragon_mage_chestplate", () -> new ArchDragonMageArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_LEGGING = ITEMS.register("arch_dragon_mage_leggings", () -> new ArchDragonMageArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> ARCH_DRAGON_MAGE_BOOTS = ITEMS.register("arch_dragon_mage_boots", () -> new ArchDragonMageArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_HELMET = ITEMS.register("infernal_dragon_mage_helmet", () -> new InfernalDragonMageArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_CHESTPLATE = ITEMS.register("infernal_dragon_mage_chestplate", () -> new InfernalDragonMageArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_LEGGING = ITEMS.register("infernal_dragon_mage_leggings", () -> new InfernalDragonMageArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> INFERNAL_DRAGON_MAGE_BOOTS = ITEMS.register("infernal_dragon_mage_boots", () -> new InfernalDragonMageArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> WILD_DRAGON_MAGE_HELMET = ITEMS.register("wild_dragon_mage_helmet", () -> new WildDragonMageArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_CHESTPLATE = ITEMS.register("wild_dragon_mage_chestplate", () -> new WildDragonMageArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_LEGGING = ITEMS.register("wild_dragon_mage_leggings", () -> new WildDragonMageArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> WILD_DRAGON_MAGE_BOOTS = ITEMS.register("wild_dragon_mage_boots", () -> new WildDragonMageArmorItem(ArmorItem.Type.BOOTS));

    //Curios
    //public static final RegistryObject<Item> DRAGON_MAGE_TEST_CURIOS = ITEMS.register("dragon_mage_test_curios", () -> new DragonMageCurios(16, "dm_test_curios", new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> AMULET_OF_DRAGON_POWER = ITEMS.register("amulet_of_dragon_power", () -> new DragonMageCuriosItem(32, "amulet_of_dragon_power", new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> RING_OF_POWER = ITEMS.register("ring_of_power", RingOfPowerItem::new);
    public static final RegistryObject<Item> RING_OF_RULING = ITEMS.register("ring_of_ruling", RingOfRulingItem::new);

    public static final RegistryObject<Item> BRACELET_OF_FRIENDSHIP = ITEMS.register("bracelet_of_friendship", () -> new BraceletOfFriendshipItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FACTION_AMULET = ITEMS.register("faction_amulet", ()->new FactionAmuletItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ANGEL_RING = ITEMS.register("angel_ring", ()->new AngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RING_OF_SPELL_STORING = ITEMS.register("ring_of_spell_storing", ()->new RingOfSpellStoringItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FALLEN_ANGEL_RING = ITEMS.register("fallen_angel_ring", ()->new FallenAngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DEVIL_RING = ITEMS.register("devil_ring", ()->new DevilRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VOIDFEATHER_CHARM = ITEMS.register("voidfeather_charm", () -> new VoidfeatherCharmItem((new Item.Properties()).setNoRepair().stacksTo(1).durability(1)));

    /**
     * The user deals twice the amount of damage, but also takes twice the amount of damage
     * @see DamageEventHandler
     */
    public static final RegistryObject<Item> GLASS_CANNON_BELT = ITEMS.register("glass_cannon_belt", () -> new BaseTieredItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    /**
     * The user deals twice the amount of damage, but also takes twice the amount of damage
     * @see DamageEventHandler
     */
    public static final RegistryObject<Item> STURDY_BELT = ITEMS.register("sturdy_belt", () -> new BaseTieredItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CURSE_PROTECTION_AMULET = ITEMS.register("curse_protection_amulet", CurseProtectionAmuletItem::new);


    //Other
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(false, (new Item.Properties())));
    public static final RegistryObject<Item> PURIFIED_MUTANDIS = ITEMS.register("purified_mutandis", () -> new MutandisItem(true, (new Item.Properties())));
    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", ManaCakeItem::new);
    public static final RegistryObject<Item> RIFT_EMITTER_ITEM = ITEMS.register("rift_emitter", () -> new RiftEmitterItem(BlockInit.RIFT_EMITTER.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRIMSTONE_CHALK = ITEMS.register("brimstone_chalk", BrimstoneChalkItem::new);
    public static final RegistryObject<Item> WEATHER_FAIRY_STAFF = ITEMS.register("weather_fairy_staff", WeatherFairyStaffItem::new);
    public static final RegistryObject<Item> THE_CLICKERS_COOKIE = ITEMS.register("the_clickers_cookie", TheClickersCookieItem::new);


    //ARMOR UPGRADES:
    public static final RegistryObject<Item> UPGRADE_SEAL_ANGEL_FLIGHT = ITEMS.register("upgrade_seal_angel_flight", () -> new UpgradeSealItem(RLoc.create("armorupgrade/angel_flight")));
    public static final RegistryObject<Item> UPGRADE_SEAL_BURNING_FRENZY = ITEMS.register("upgrade_seal_burning_frenzy", () -> new UpgradeSealItem(RLoc.create("armorupgrade/burning_frenzy")));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_BOOST = ITEMS.register("upgrade_seal_damage_boost", () -> new UpgradeSealItem(RLoc.create("armorupgrade/damage_boost")));
    public static final RegistryObject<Item> UPGRADE_SEAL_DAMAGE_RESISTANCE = ITEMS.register("upgrade_seal_damage_resistance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/damage_resistance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_DOLPHINS_GRACE = ITEMS.register("upgrade_seal_dolphins_grace", () -> new UpgradeSealItem(RLoc.create("armorupgrade/dolphins_grace")));
    public static final RegistryObject<Item> UPGRADE_SEAL_REACH_DISTANCE = ITEMS.register("upgrade_seal_reach_distance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/reach_distance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_ELYTRA = ITEMS.register("upgrade_seal_elytra", () -> new UpgradeSealItem(RLoc.create("armorupgrade/elytra")));
    public static final RegistryObject<Item> UPGRADE_SEAL_EXPLOSION_RESISTANCE = ITEMS.register("upgrade_seal_explosion_resistance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/explosion_resistance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_FLY = ITEMS.register("upgrade_seal_fly", () -> new UpgradeSealItem(RLoc.create("armorupgrade/fly")));
    public static final RegistryObject<Item> UPGRADE_SEAL_HEALTH_BOOST = ITEMS.register("upgrade_seal_health_boost", () -> new UpgradeSealItem(RLoc.create("armorupgrade/health_boost")));
    public static final RegistryObject<Item> UPGRADE_SEAL_JUMP = ITEMS.register("upgrade_seal_jump", () -> new UpgradeSealItem(RLoc.create("armorupgrade/jump")));
    public static final RegistryObject<Item> UPGRADE_SEAL_KINETIC_RESISTANCE = ITEMS.register("upgrade_seal_kinetic_resistance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/kinetic_resistance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MANA_REGEN = ITEMS.register("upgrade_seal_mana_regen", () -> new UpgradeSealItem(RLoc.create("armorupgrade/mana_regen")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MINOR_FIRE_RESISTANCE = ITEMS.register("upgrade_seal_minor_fire_resistance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/minor_fire_resistance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MAJOR_FIRE_RESISTANCE = ITEMS.register("upgrade_seal_major_fire_resistance", () -> new UpgradeSealItem(RLoc.create("armorupgrade/major_fire_resistance")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MINOR_MANA_BOOST = ITEMS.register("upgrade_seal_mana_boost", () -> new UpgradeSealItem(RLoc.create("armorupgrade/mana_boost")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MAJOR_MANA_BOOST = ITEMS.register("upgrade_seal_major_mana_boost", () -> new UpgradeSealItem(RLoc.create("armorupgrade/major_mana_boost")));
    public static final RegistryObject<Item> UPGRADE_SEAL_METEOR_JUMP = ITEMS.register("upgrade_seal_meteor_jump", () -> new UpgradeSealItem(RLoc.create("armorupgrade/meteor_jump")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MIST_FORM = ITEMS.register("upgrade_seal_mist_form", () -> new UpgradeSealItem(RLoc.create("armorupgrade/mist_form")));
    public static final RegistryObject<Item> UPGRADE_SEAL_MOVEMENT_SPEED = ITEMS.register("upgrade_seal_movement_speed", () -> new UpgradeSealItem(RLoc.create("armorupgrade/movement_speed")));
    public static final RegistryObject<Item> UPGRADE_SEAL_NIGHT_VISION = ITEMS.register("upgrade_seal_night_vision", () -> new UpgradeSealItem(RLoc.create("armorupgrade/night_vision")));
    public static final RegistryObject<Item> UPGRADE_SEAL_PROJECTILE_REFLECTION = ITEMS.register("upgrade_seal_projectile_reflection", () -> new UpgradeSealItem(RLoc.create("armorupgrade/projectile_reflection")));
    public static final RegistryObject<Item> UPGRADE_SEAL_REGENERATION = ITEMS.register("upgrade_seal_regeneration", () -> new UpgradeSealItem(RLoc.create("armorupgrade/regeneration")));
    public static final RegistryObject<Item> UPGRADE_SEAL_SATURATION = ITEMS.register("upgrade_seal_saturation", () -> new UpgradeSealItem(RLoc.create("armorupgrade/saturation")));
    public static final RegistryObject<Item> UPGRADE_SEAL_WATER_BREATHING = ITEMS.register("upgrade_seal_water_breathing", () -> new UpgradeSealItem(RLoc.create("armorupgrade/water_breathing")));
    public static final RegistryObject<Item> UPGRADE_SEAL_SORCERERS_PRIDE = ITEMS.register("upgrade_seal_sorcerers_pride", () -> new UpgradeSealItem(RLoc.create("armorupgrade/sorcerers_pride")));
    public static final RegistryObject<Item> UPGRADE_SEAL_INSIGHT = ITEMS.register("upgrade_seal_insight", () -> new UpgradeSealItem(RLoc.create("armorupgrade/insight")));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
