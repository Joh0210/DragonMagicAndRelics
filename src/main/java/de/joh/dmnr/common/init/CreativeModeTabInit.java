package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Creation of a creative tap for the items of this mod.
 * @author Joh0210
 */
public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DMNR_TAB = CREATIVE_MODE_TABS.register("dmnr_1_dmnr",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get()))
                    .title(Component.translatable("itemGroup.dmnr"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ItemInit.DRAGON_CORE.get());

                        pOutput.accept(ItemInit.ABYSSAL_DRAGON_MAGE_HELMET.get());
                        pOutput.accept(ItemInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE.get());
                        pOutput.accept(ItemInit.ABYSSAL_DRAGON_MAGE_LEGGING.get());
                        pOutput.accept(ItemInit.ABYSSAL_DRAGON_MAGE_BOOTS.get());

                        pOutput.accept(ItemInit.ARCH_DRAGON_MAGE_HELMET.get());
                        pOutput.accept(ItemInit.ARCH_DRAGON_MAGE_CHESTPLATE.get());
                        pOutput.accept(ItemInit.ARCH_DRAGON_MAGE_LEGGING.get());
                        pOutput.accept(ItemInit.ARCH_DRAGON_MAGE_BOOTS.get());

                        pOutput.accept(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get());
                        pOutput.accept(ItemInit.INFERNAL_DRAGON_MAGE_CHESTPLATE.get());
                        pOutput.accept(ItemInit.INFERNAL_DRAGON_MAGE_LEGGING.get());
                        pOutput.accept(ItemInit.INFERNAL_DRAGON_MAGE_BOOTS.get());

                        pOutput.accept(ItemInit.WILD_DRAGON_MAGE_HELMET.get());
                        pOutput.accept(ItemInit.WILD_DRAGON_MAGE_CHESTPLATE.get());
                        pOutput.accept(ItemInit.WILD_DRAGON_MAGE_LEGGING.get());
                        pOutput.accept(ItemInit.WILD_DRAGON_MAGE_BOOTS.get());

                        pOutput.accept(ItemInit.AMULET_OF_DRAGON_POWER.get());
                        pOutput.accept(ItemInit.RING_OF_POWER.get());
                        pOutput.accept(ItemInit.RING_OF_RULING.get());


                        pOutput.accept(ItemInit.BATTLE_MAGE_RING.get());
                        pOutput.accept(ItemInit.WEATHER_FAIRY_STAFF.get());
                        pOutput.accept(ItemInit.THE_CLICKERS_COOKIE.get());
                        pOutput.accept(ItemInit.KEY_OF_HOMESTEAD.get());

                        pOutput.accept(ItemInit.BRACELET_OF_FRIENDSHIP.get());
                        pOutput.accept(ItemInit.FACTION_AMULET.get());
                        pOutput.accept(ItemInit.RING_OF_SPELL_STORING.get());
                        pOutput.accept(ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get());
                        pOutput.accept(ItemInit.ANGEL_RING.get());
                        pOutput.accept(ItemInit.DEVIL_RING.get());
                        pOutput.accept(ItemInit.FALLEN_ANGEL_RING.get());
                        pOutput.accept(ItemInit.VOIDFEATHER_CHARM.get());

                        pOutput.accept(ItemInit.GLASS_CANNON_BELT.get());
                        pOutput.accept(ItemInit.STURDY_BELT.get());
                        pOutput.accept(ItemInit.CURSE_PROTECTION_AMULET.get());
                        pOutput.accept(ItemInit.BRACELET_OF_WATER.get());
                        pOutput.accept(ItemInit.BRACELET_OF_WATER_GREATER.get());
                        pOutput.accept(ItemInit.REACH_RING_MINOR.get());
                        pOutput.accept(ItemInit.REACH_RING.get());
                        pOutput.accept(ItemInit.REACH_RING_GREATER.get());
                        pOutput.accept(ItemInit.FIRE_RESISTANCE_BRACELET.get());
                        pOutput.accept(ItemInit.BELT_OF_LIFE_MINOR.get());
                        pOutput.accept(ItemInit.BELT_OF_LIFE.get());
                        pOutput.accept(ItemInit.BELT_OF_LIFE_GREATER.get());
                        pOutput.accept(ItemInit.REGENERATION_AMULET.get());
                        pOutput.accept(ItemInit.DEFENSE_BRACELET_MINOR.get());
                        pOutput.accept(ItemInit.DEFENSE_BRACELET.get());
                        pOutput.accept(ItemInit.DEFENSE_BRACELET_GREATER.get());
                        pOutput.accept(ItemInit.AMULET_OF_HELLFIRE.get());
                        pOutput.accept(ItemInit.HYDRA_CROWN.get());
                        pOutput.accept(ItemInit.NIGHT_GOGGLES.get());
                        pOutput.accept(ItemInit.COLLECTORS_AMULET.get());
                        pOutput.accept(ItemInit.PROJECTILE_REFLECTION_RING.get());
                        pOutput.accept(ItemInit.OCELOT_RING_MINOR.get());
                        pOutput.accept(ItemInit.OCELOT_RING.get());
                        pOutput.accept(ItemInit.OCELOT_RING_GREATER.get());


                        pOutput.accept(ItemInit.DISAPPEARING_TIARA.get());
                        pOutput.accept(ItemInit.POTION_OF_INFINITY.get());

                        pOutput.accept(ItemInit.REVENGE_CHARM_FIRE.get());
                        pOutput.accept(ItemInit.REVENGE_CHARM_FIRE_MAJOR.get());
                        pOutput.accept(ItemInit.REVENGE_CHARM_REFLECT.get());
                        pOutput.accept(ItemInit.REVENGE_CHARM_REFLECT_MAJOR.get());
                        pOutput.accept(ItemInit.REVENGE_CHARM_DMG.get());
                        pOutput.accept(ItemInit.REVENGE_CHARM_DMG_MAJOR.get());

                        pOutput.accept(ItemInit.MUTANDIS.get());
                        pOutput.accept(ItemInit.PURIFIED_MUTANDIS.get());
                        pOutput.accept(ItemInit.MANA_CAKE.get());
                        pOutput.accept(ItemInit.RIFT_EMITTER_ITEM.get());
                        pOutput.accept(ItemInit.BRIMSTONE_CHALK.get());
                        pOutput.accept(ItemInit.BRIMSTONE_COAL.get());
                    })
                    .build());

    public static void register(IEventBus pOutputBus) {
        CREATIVE_MODE_TABS.register(pOutputBus);
    }
}
