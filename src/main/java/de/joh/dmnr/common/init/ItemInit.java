package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.common.item.spellstoring.RingOfCooldownSpellStoringItem;
import de.joh.dmnr.common.item.spellstoring.RingOfNormalSpellStoringItem;
import de.joh.tnl.api.item.CurioRegistry;
import de.joh.tnl.api.item.CurioSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Inits of all mod items
 * @author Joh0210
 */
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);

    /**
     * Must be declared before the items, because it is filled while they are registered.
     */
    private static final CurioRegistry CURIOS = new CurioRegistry(ITEMS);

    public static final RegistryObject<Item> DRAGON_TOTEM = ITEMS.register("dragon_totem", DragonTotemItem::new);
    public static final RegistryObject<Item> DRAGON_TOTEM_EMPTY = ITEMS.register("dragon_totem_empty", DragonTotemItem::new);

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
    public static final RegistryObject<Item> AMULET_OF_DRAGON_POWER = registerCurio("amulet_of_dragon_power", CurioSlot.NECKLACE, DragonPowerAmuletItem::new);
    public static final RegistryObject<Item> RING_OF_RULING = registerCurio("ring_of_ruling", CurioSlot.RING, RingOfRulingItem::new);
    public static final RegistryObject<Item> RING_OF_POWER = registerCurio("ring_of_power", CurioSlot.RING, RingOfPowerItem::new);
    public static final RegistryObject<Item> BRACELET_OF_FRIENDSHIP = registerCurio("bracelet_of_friendship", CurioSlot.BRACELET, () -> new BraceletOfFriendshipItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FACTION_AMULET = registerCurio("faction_amulet", CurioSlot.NECKLACE, ()->new FactionAmuletItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> ANGEL_RING = registerCurio("angel_ring", CurioSlot.RING, ()->new AngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RING_OF_SPELL_STORING = registerCurio("ring_of_spell_storing", CurioSlot.BRACELET, ()->new RingOfNormalSpellStoringItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> RING_OF_SPELL_STORING_COOLDOWN = registerCurio("ring_of_spell_storing_cooldown", CurioSlot.BRACELET, ()->new RingOfCooldownSpellStoringItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FALLEN_ANGEL_RING = registerCurio("fallen_angel_ring", CurioSlot.RING, ()->new FallenAngelRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DEVIL_RING = registerCurio("devil_ring", CurioSlot.RING, ()->new DevilRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VOIDFEATHER_CHARM = registerCurio("voidfeather_charm", CurioSlot.CHARM, () -> new VoidfeatherCharmItem((new Item.Properties()).setNoRepair().stacksTo(1).durability(1)));

    public static final RegistryObject<Item> PROJECTILE_REFLECTION_RING = registerCurio("projectile_reflection_ring", CurioSlot.RING, ProjectileReflectionRingItem::new);
    public static final RegistryObject<Item> OCELOT_RING_MINOR = registerCurio("ocelot_ring_minor", CurioSlot.RING, () -> new OcelotCurioItem(1));
    public static final RegistryObject<Item> OCELOT_RING = registerCurio("ocelot_ring", CurioSlot.RING, () -> new OcelotCurioItem(2));
    public static final RegistryObject<Item> OCELOT_RING_GREATER = registerCurio("ocelot_ring_greater", CurioSlot.RING, () -> new OcelotCurioItem(3));
    public static final RegistryObject<Item> GLASS_CANNON_BELT = registerCurio("glass_cannon_belt", CurioSlot.BELT, () -> new DamageAdjustmentBelt(true));
    public static final RegistryObject<Item> STURDY_BELT = registerCurio("sturdy_belt", CurioSlot.BELT, () -> new DamageAdjustmentBelt(false));
    public static final RegistryObject<Item> CURSE_PROTECTION_AMULET = registerCurio("curse_protection_amulet", CurioSlot.NECKLACE, CurseProtectionAmuletItem::new);
    public static final RegistryObject<Item> BRACELET_OF_WATER = registerCurio("bracelet_of_water", CurioSlot.BRACELET, () -> new WaterBraceletItem(1));
    public static final RegistryObject<Item> BRACELET_OF_WATER_GREATER = registerCurio("bracelet_of_water_greater", CurioSlot.BRACELET, () -> new WaterBraceletItem(2));
    public static final RegistryObject<Item> REACH_RING_MINOR = registerCurio("reach_ring_minor", CurioSlot.RING, () -> new ReachRingItem(1));
    public static final RegistryObject<Item> REACH_RING = registerCurio("reach_ring", CurioSlot.RING, () -> new ReachRingItem(2));
    public static final RegistryObject<Item> REACH_RING_GREATER = registerCurio("reach_ring_greater", CurioSlot.RING, () -> new ReachRingItem(3));
    public static final RegistryObject<Item> FIRE_RESISTANCE_BRACELET = registerCurio("fire_resistance_bracelet", CurioSlot.BRACELET, FireResistanceBraceletItem::new);
    public static final RegistryObject<Item> BELT_OF_LIFE_MINOR = registerCurio("belt_of_life_minor", CurioSlot.BELT, () -> new BeltOfLifeItem(1));
    public static final RegistryObject<Item> BELT_OF_LIFE = registerCurio("belt_of_life", CurioSlot.BELT, () -> new BeltOfLifeItem(2));
    public static final RegistryObject<Item> BELT_OF_LIFE_GREATER = registerCurio("belt_of_life_greater", CurioSlot.BELT, () -> new BeltOfLifeItem(3));
    public static final RegistryObject<Item> REGENERATION_AMULET = registerCurio("regeneration_amulet", CurioSlot.NECKLACE, () -> new RegenerationAmuletItem(1));
    public static final RegistryObject<Item> DEFENSE_BRACELET_MINOR = registerCurio("defense_bracelet_minor", CurioSlot.BRACELET, () -> new DefenseBraceletItem(5, 1));
    public static final RegistryObject<Item> DEFENSE_BRACELET = registerCurio("defense_bracelet", CurioSlot.BRACELET, () -> new DefenseBraceletItem(8, 4));
    public static final RegistryObject<Item> DEFENSE_BRACELET_GREATER = registerCurio("defense_bracelet_greater", CurioSlot.BRACELET, () -> new DefenseBraceletItem(14, 8));
    public static final RegistryObject<Item> HYDRA_CROWN = registerCurio("hydra_crown", CurioSlot.HEAD, HydraCrownItem::new);
    public static final RegistryObject<Item> AMULET_OF_HELLFIRE = registerCurio("amulet_of_hellfire", CurioSlot.NECKLACE, AmuletOfHellfire::new);
    public static final RegistryObject<Item> NIGHT_GOGGLES = registerCurio("night_goggles", CurioSlot.HEAD, NightGogglesItem::new);
    public static final RegistryObject<Item> COLLECTORS_AMULET = registerCurio("collectors_amulet", CurioSlot.NECKLACE, () -> new CollectorItem("ring", 3));
    public static final RegistryObject<Item> DISAPPEARING_TIARA = registerCurio("disappearing_tiara", CurioSlot.HEAD, DisappearingTiaraItem::new);
    public static final RegistryObject<Item> POTION_OF_INFINITY = ITEMS.register("potion_of_infinity", PotionOfInfinityItem::new);

    public static final RegistryObject<Item> REVENGE_CHARM_FIRE = registerCurio("revenge_charm_fire", CurioSlot.CHARM, () -> new FireRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_FIRE_MAJOR = registerCurio("revenge_charm_fire_major", CurioSlot.CHARM, () -> new FireRevengeCharmItem(2));
    public static final RegistryObject<Item> REVENGE_CHARM_REFLECT = registerCurio("revenge_charm_reflect", CurioSlot.CHARM, () -> new ForceRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_REFLECT_MAJOR = registerCurio("revenge_charm_reflect_major", CurioSlot.CHARM, () -> new ForceRevengeCharmItem(2));
    public static final RegistryObject<Item> REVENGE_CHARM_DMG = registerCurio("revenge_charm_dmg", CurioSlot.CHARM, () -> new DmgRevengeCharmItem(1));
    public static final RegistryObject<Item> REVENGE_CHARM_DMG_MAJOR = registerCurio("revenge_charm_dmg_major", CurioSlot.CHARM, () -> new DmgRevengeCharmItem(2));


    //Other
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(false, (new Item.Properties())));
    public static final RegistryObject<Item> PURIFIED_MUTANDIS = ITEMS.register("purified_mutandis", () -> new MutandisItem(true, (new Item.Properties())));
    public static final RegistryObject<Item> MANA_CAKE = ITEMS.register("mana_cake", ManaCakeItem::new);
    public static final RegistryObject<Item> RIFT_EMITTER_ITEM = ITEMS.register("rift_emitter", () -> new RiftEmitterItem(BlockInit.RIFT_EMITTER.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRIMSTONE_CHALK = ITEMS.register("brimstone_chalk", BrimstoneChalkItem::new);
    public static final RegistryObject<Item> BRIMSTONE_COAL = ITEMS.register("brimstone_coal", BrimstoneCoalItem::new);
    public static final RegistryObject<Item> BATTLE_MAGE_RING = registerCurio("battle_mage_ring", CurioSlot.RING, BattleMageRingItem::new);
    public static final RegistryObject<Item> WEATHER_FAIRY_STAFF = ITEMS.register("weather_fairy_staff", WeatherFairyStaffItem::new);
    public static final RegistryObject<Item> THE_CLICKERS_COOKIE = ITEMS.register("the_clickers_cookie", TheClickersCookieItem::new);
    public static final RegistryObject<Item> KEY_OF_HOMESTEAD = ITEMS.register("key_of_homestead", KeyOfHomestead::new);
    public static final RegistryObject<Item> JOURNAL_OF_MYSTERIES = registerCurio("journal_of_mysteries", CurioSlot.BACK, JournalOfMysteriesItem::new);

    /**
     * Registers an item that can be equipped in the given Curios slot. The data generators add it to the item tag of
     * the slot, and they fail for every ICurioItem that is registered without a slot.
     */
    private static RegistryObject<Item> registerCurio(String name, CurioSlot slot, Supplier<? extends Item> item) {
        return CURIOS.register(name, slot, item);
    }

    /**
     * @return the Curios slot of every item that was registered with {@link #registerCurio}, in registration order
     */
    public static Map<RegistryObject<Item>, CurioSlot> getCurioSlots() {
        return CURIOS.getSlots();
    }

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
