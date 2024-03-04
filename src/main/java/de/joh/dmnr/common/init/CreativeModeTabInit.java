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
                    .noScrollBar()
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

                        pOutput.accept(ItemInit.BRACELET_OF_FRIENDSHIP.get());
                        pOutput.accept(ItemInit.FACTION_AMULET.get());
                        pOutput.accept(ItemInit.RING_OF_SPELL_STORING.get());
                        pOutput.accept(ItemInit.ANGEL_RING.get());
                        pOutput.accept(ItemInit.DEVIL_RING.get());
                        pOutput.accept(ItemInit.FALLEN_ANGEL_RING.get());
                        pOutput.accept(ItemInit.VOIDFEATHER_CHARM.get());

                        pOutput.accept(ItemInit.GLASS_CANNON_BELT.get());
                        pOutput.accept(ItemInit.STURDY_BELT.get());
                        pOutput.accept(ItemInit.CURSE_PROTECTION_AMULET.get());

                        pOutput.accept(ItemInit.MUTANDIS.get());
                        pOutput.accept(ItemInit.PURIFIED_MUTANDIS.get());
                        pOutput.accept(ItemInit.MANA_CAKE.get());
                        pOutput.accept(ItemInit.RIFT_EMITTER_ITEM.get());
                        pOutput.accept(ItemInit.BRIMSTONE_CHALK.get());
                        pOutput.accept(ItemInit.WEATHER_FAIRY_STAFF.get());
                        pOutput.accept(ItemInit.THE_CLICKERS_COOKIE.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> ARMOR_UPGRADE_TAB = CREATIVE_MODE_TABS.register("dmnr_2_armorupgrades",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.UPGRADE_SEAL_ANGEL_FLIGHT.get()))
                    .title(Component.translatable("itemGroup.armorupgrades"))
                    .noScrollBar()
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ItemInit.UPGRADE_SEAL_ANGEL_FLIGHT.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_BURNING_FRENZY.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_DAMAGE_BOOST.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_DOLPHINS_GRACE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_REACH_DISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_ELYTRA.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_EXPLOSION_RESISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_FLY.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_HEALTH_BOOST.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_JUMP.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_KINETIC_RESISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MANA_REGEN.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MINOR_FIRE_RESISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MAJOR_FIRE_RESISTANCE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MINOR_MANA_BOOST.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MAJOR_MANA_BOOST.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_METEOR_JUMP.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MIST_FORM.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_MOVEMENT_SPEED.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_NIGHT_VISION.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_REGENERATION.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_SATURATION.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_WATER_BREATHING.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_SORCERERS_PRIDE.get());
                        pOutput.accept(ItemInit.UPGRADE_SEAL_INSIGHT.get());
                    })
                    .build());

    public static void register(IEventBus pOutputBus) {
        CREATIVE_MODE_TABS.register(pOutputBus);
    }
}
