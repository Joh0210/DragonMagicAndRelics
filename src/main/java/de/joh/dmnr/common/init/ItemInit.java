package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.common.item.spellstoring.RingOfCooldownSpellStoringItem;
import de.joh.dmnr.common.item.spellstoring.RingOfNormalSpellStoringItem;
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

    public static final RegistryObject<Item> DRAGON_MAGE_HELMET = ITEMS.register("dragon_mage_helmet", () -> new DragonMageArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> DRAGON_MAGE_CHESTPLATE = ITEMS.register("dragon_mage_chestplate", () -> new DragonMageArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> DRAGON_MAGE_LEGGING = ITEMS.register("dragon_mage_leggings", () -> new DragonMageArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> DRAGON_MAGE_BOOTS = ITEMS.register("dragon_mage_boots", () -> new DragonMageArmorItem(ArmorItem.Type.BOOTS));

    public static final RegistryObject<Item> RUNE_HELMET = ITEMS.register("rune_helmet", () -> new RuneArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> RUNE_CHESTPLATE = ITEMS.register("rune_chestplate", () -> new RuneArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> RUNE_LEGGING = ITEMS.register("rune_legging", () -> new RuneArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> RUNE_BOOTS = ITEMS.register("rune_boots", () -> new RuneArmorItem(ArmorItem.Type.BOOTS));


    //Curios
    public static final RegistryObject<Item> AMULET_OF_DRAGON_POWER = ITEMS.register("amulet_of_dragon_power", DragonPowerAmuletItem::new);
    public static final RegistryObject<Item> RING_OF_RULING = ITEMS.register("ring_of_ruling", RingOfRulingItem::new);
    public static final RegistryObject<Item> RING_OF_POWER = ITEMS.register("ring_of_power", RingOfPowerItem::new);
    public static final RegistryObject<Item> BRACELET_OF_FRIENDSHIP = ITEMS.register("bracelet_of_friendship", () -> new BraceletOfFriendshipItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FACTION_AMULET = ITEMS.register("faction_amulet", ()->new FactionAmuletItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ANGEL_RING = ITEMS.register("angel_ring", ()->new AngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RING_OF_SPELL_STORING = ITEMS.register("ring_of_spell_storing", ()->new RingOfNormalSpellStoringItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RING_OF_SPELL_STORING_COOLDOWN = ITEMS.register("ring_of_spell_storing_cooldown", ()->new RingOfCooldownSpellStoringItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FALLEN_ANGEL_RING = ITEMS.register("fallen_angel_ring", ()->new FallenAngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DEVIL_RING = ITEMS.register("devil_ring", ()->new DevilRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VOIDFEATHER_CHARM = ITEMS.register("voidfeather_charm", () -> new VoidfeatherCharmItem((new Item.Properties()).setNoRepair().stacksTo(1).durability(1)));

    public static final RegistryObject<Item> PROJECTILE_REFLECTION_RING = ITEMS.register("projectile_reflection_ring", ProjectileReflectionRingItem::new);
    public static final RegistryObject<Item> OCELOT_RING_MINOR = ITEMS.register("ocelot_ring_minor", () -> new OcelotCurioItem(1));
    public static final RegistryObject<Item> OCELOT_RING = ITEMS.register("ocelot_ring", () -> new OcelotCurioItem(2));
    public static final RegistryObject<Item> OCELOT_RING_GREATER = ITEMS.register("ocelot_ring_greater", () -> new OcelotCurioItem(3));
    public static final RegistryObject<Item> GLASS_CANNON_BELT = ITEMS.register("glass_cannon_belt", () -> new DamageAdjustmentBelt(2.0f));
    public static final RegistryObject<Item> STURDY_BELT = ITEMS.register("sturdy_belt", () -> new DamageAdjustmentBelt(0.5f));
    public static final RegistryObject<Item> CURSE_PROTECTION_AMULET = ITEMS.register("curse_protection_amulet", CurseProtectionAmuletItem::new);
    public static final RegistryObject<Item> BRACELET_OF_WATER = ITEMS.register("bracelet_of_water", () -> new WaterBraceletItem(1));
    public static final RegistryObject<Item> BRACELET_OF_WATER_GREATER = ITEMS.register("bracelet_of_water_greater", () -> new WaterBraceletItem(2));
    public static final RegistryObject<Item> REACH_RING_MINOR = ITEMS.register("reach_ring_minor", () -> new ReachRingItem(1));
    public static final RegistryObject<Item> REACH_RING = ITEMS.register("reach_ring", () -> new ReachRingItem(2));
    public static final RegistryObject<Item> REACH_RING_GREATER = ITEMS.register("reach_ring_greater", () -> new ReachRingItem(3));
    public static final RegistryObject<Item> FIRE_RESISTANCE_BRACELET = ITEMS.register("fire_resistance_bracelet", FireResistanceBraceletItem::new);
    public static final RegistryObject<Item> BELT_OF_LIFE_MINOR = ITEMS.register("belt_of_life_minor", () -> new BeltOfLifeItem(1));
    public static final RegistryObject<Item> BELT_OF_LIFE = ITEMS.register("belt_of_life", () -> new BeltOfLifeItem(2));
    public static final RegistryObject<Item> BELT_OF_LIFE_GREATER = ITEMS.register("belt_of_life_greater", () -> new BeltOfLifeItem(3));
    public static final RegistryObject<Item> REGENERATION_AMULET = ITEMS.register("regeneration_amulet", () -> new RegenerationAmuletItem(1));
    public static final RegistryObject<Item> DEFENSE_BRACELET_MINOR = ITEMS.register("defense_bracelet_minor", () -> new DefenseBraceletItem(5, 1));
    public static final RegistryObject<Item> DEFENSE_BRACELET = ITEMS.register("defense_bracelet", () -> new DefenseBraceletItem(8, 4));
    public static final RegistryObject<Item> DEFENSE_BRACELET_GREATER = ITEMS.register("defense_bracelet_greater", () -> new DefenseBraceletItem(14, 8));
    public static final RegistryObject<Item> HYDRA_CROWN = ITEMS.register("hydra_crown", HydraCrownItem::new);
    public static final RegistryObject<Item> AMULET_OF_HELLFIRE = ITEMS.register("amulet_of_hellfire", AmuletOfHellfire::new);
    public static final RegistryObject<Item> NIGHT_GOGGLES = ITEMS.register("night_goggles", NightGogglesItem::new);
    public static final RegistryObject<Item> COLLECTORS_AMULET = ITEMS.register("collectors_amulet", () -> new CollectorItem("ring", 3));
    public static final RegistryObject<Item> DISAPPEARING_TIARA = ITEMS.register("disappearing_tiara", DisappearingTiaraItem::new);
    public static final RegistryObject<Item> POTION_OF_INFINITY = ITEMS.register("potion_of_infinity", PotionOfInfinityItem::new);

    public static final RegistryObject<Item> REVENGE_CHARM_FIRE = ITEMS.register("revenge_charm_fire", () -> new FireRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_FIRE_MAJOR = ITEMS.register("revenge_charm_fire_major", () -> new FireRevengeCharmItem(2));
    public static final RegistryObject<Item> REVENGE_CHARM_REFLECT = ITEMS.register("revenge_charm_reflect", () -> new ForceRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_REFLECT_MAJOR = ITEMS.register("revenge_charm_reflect_major", () -> new ForceRevengeCharmItem(2));
    public static final RegistryObject<Item> REVENGE_CHARM_DMG = ITEMS.register("revenge_charm_dmg", () -> new DmgRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_DMG_MAJOR = ITEMS.register("revenge_charm_dmg_major", () -> new DmgRevengeCharmItem(2));


    //Other
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(false, (new Item.Properties())));
    public static final RegistryObject<Item> PURIFIED_MUTANDIS = ITEMS.register("purified_mutandis", () -> new MutandisItem(true, (new Item.Properties())));
    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", ManaCakeItem::new);
    public static final RegistryObject<Item> RIFT_EMITTER_ITEM = ITEMS.register("rift_emitter", () -> new RiftEmitterItem(BlockInit.RIFT_EMITTER.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRIMSTONE_CHALK = ITEMS.register("brimstone_chalk", BrimstoneChalkItem::new);
    public static final RegistryObject<Item> BRIMSTONE_COAL = ITEMS.register("brimstone_coal", BrimstoneCoalItem::new);
    public static final RegistryObject<Item> BATTLE_MAGE_RING = ITEMS.register("battle_mage_ring", BattleMageRingItem::new);
    public static final RegistryObject<Item> WEATHER_FAIRY_STAFF = ITEMS.register("weather_fairy_staff", WeatherFairyStaffItem::new);
    public static final RegistryObject<Item> THE_CLICKERS_COOKIE = ITEMS.register("the_clickers_cookie", TheClickersCookieItem::new);
    public static final RegistryObject<Item> KEY_OF_HOMESTEAD = ITEMS.register("key_of_homestead", KeyOfHomestead::new);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
