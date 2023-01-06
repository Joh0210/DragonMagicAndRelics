package de.joh.dragonmagicandrelics.armorupgrades;

import com.mna.effects.EffectInit;
import de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.*;
import de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped.IArmorUpgradeOnFullyEquipped;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An initialization of all upgrades for the Dragon Mage Armor.
 * Each new upgrade must also be entered getAllUpgrades or in one of the arrays!
 * Each new upgrade must also be listed in the configs for the initial upgrades!
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.config.CommonConfigs
 * @author Joh0210
 */
public class ArmorUpgradeInit {
    /**
     * By what percentage is the damage reduced by the damage resistance upgrade.
     */
    public static final float DAMAGE_REDUCTION_PER_LEVEL = 0.2f;

    /**
     * How much mana does the fire resistance upgrade consume for one tick of fire damage?
     */
    public static final int MANA_PER_FIRE_DAMAGE = 20;

    /**
     * How many mana is consumed every tick when the wearer flies with the Elytra upgrade
     */
    public static final float ELYTRA_MANA_COST_PER_TICK = 0.75F;

    /**
     * List of all upgrades with a function performed on each tick
     * @see DragonMageArmor
     * @see IArmorUpgradeOnArmorTick
     */
    public static IArmorUpgradeOnArmorTick[] ARMOR_UPGRADE_ON_ARMOR_TICK = new IArmorUpgradeOnArmorTick[]{
            new ArmorUpgradeFly("fly", 2),
            new ArmorUpgradeSaturation("saturation", 1),
            new ArmorUpgradeSpeed("movement_speed", 3),
            new ArmorUpgradeWaterbreathing("water_breathing", 2),
            new ArmorUpgradeMeteorJump("meteor_jump", 1)
    };

    /**
     * List of upgrades that add a potion effect to the wearer of Dragon Mage Armor.
     * @see DragonMageArmor
     * @see IArmorUpgradeOnArmorTick
     */
    public static ArmorUpgradePotionEffect[] ARMOR_UPGRADE_POTION_EFFECT = new ArmorUpgradePotionEffect[]{
            new ArmorUpgradePotionEffect("dolphins_grace", 2, MobEffects.DOLPHINS_GRACE),
            new ArmorUpgradePotionEffect("regeneration", 1, MobEffects.REGENERATION),
            new ArmorUpgradePotionEffect("wellspring_sight", 1, EffectInit.WELLSPRING_SIGHT.get()),
            new ArmorUpgradePotionEffect("eldrin_sight", 1, EffectInit.ELDRIN_SIGHT.get()),

            //Todo: Outsource to ARMOR_UPGRADE_ON_FULLY_EQUIPPED:
            new ArmorUpgradePotionEffect("mana_boost", 5, EffectInit.MANA_BOOST.get(), 4),
            new ArmorUpgradePotionEffect("mana_regen", 5, EffectInit.MANA_REGEN.get()),
            new ArmorUpgradePotionEffect("health_boost", 5, MobEffects.HEALTH_BOOST)

    };

    /**
     * List of upgrades that add a permanent effect to the wearer.
     * @see DragonMageArmor
     */
    public static IArmorUpgradeOnFullyEquipped[] ARMOR_UPGRADE_ON_FULLY_EQUIPPED = new IArmorUpgradeOnFullyEquipped[]{
            //new ArmorUpgradeManaBoost("mana_boost", 4),
            //new ArmorUpgradeManaRegeneration("mana_regen", 4),
            //new ArmorUpgradeHealthBoost("health_boost", 4)
    };

    /**
     * This upgrade reduces the damage the wearer receives, regardless of the source. Currently 20% reduction per level.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_RESISTANCE = new ArmorUpgrade("damage_resistance", 3);

    /**
     * This upgrade increases the damage dealt by the wearer of the Dragon Mage Armor. Both spells and normal weapons.
     * Currently 20% reduction per level. Adjustable by: DAMAGE_REDUCTION_PER_LEVEL
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_BOOST = new ArmorUpgrade("damage_boost", 4);

    /**
     * This upgrade protects you from fire damage. Level 1 Consumes mana instead. Level 2 protects you completely.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade FIRE_RESISTANCE = new ArmorUpgrade("fire_resistance", 2);

    /**
     * This upgrade protects you from fall- and kinetic damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade KINETIC_RESISTANCE = new ArmorUpgrade("kinetic_resistance", 1);

    /**
     * This upgrade protects you from explosion damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade EXPLOSION_RESISTANCE = new ArmorUpgrade("explosion_resistance", 1);


    /**
     * This upgrade increases the jump height of the wearer of the Dragon Mage Armor.
     * Effect only applies while sprinting.
     * @see de.joh.dragonmagicandrelics.events.CommonEventHandler
     */
    public static ArmorUpgrade JUMP = new ArmorUpgrade("jump", 3);

    /**
     * This upgrade allows the wearer to use Elytra Flight. Level 2 gives a permanent boost, but needs mana.
     * Increasing the maximum level has no effect without further adjustments.
     * @see DragonMageArmor
     */
    public static ArmorUpgrade ELYTRA = new ArmorUpgrade("elytra", 2);

    /**
     * Allows the armor wearer to gain night vision with a button press.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.utils.KeybindInit
     * @see DragonMageArmor
     * @see de.joh.dragonmagicandrelics.networking.packet.ToggleNightVisionC2SPacket
     */
    public static ArmorUpgrade NIGHT_VISION = new ArmorUpgrade("night_vision", 1);

    /**
     * Each new upgrade must also be entered here one of the arrays!
     * @return A list of all upgrades
     */
    public static ArmorUpgrade[] getAllUpgrades(){
        ArrayList<ArmorUpgrade> upgradeList = new ArrayList<>();
        upgradeList.addAll(List.of(ARMOR_UPGRADE_ON_ARMOR_TICK));
        upgradeList.addAll(List.of(ARMOR_UPGRADE_POTION_EFFECT));
        upgradeList.addAll(List.of(ARMOR_UPGRADE_ON_FULLY_EQUIPPED));

        upgradeList.add(KINETIC_RESISTANCE);
        upgradeList.add(EXPLOSION_RESISTANCE);
        upgradeList.add(JUMP);
        upgradeList.add(FIRE_RESISTANCE);
        upgradeList.add(ELYTRA);
        upgradeList.add(DAMAGE_RESISTANCE);
        upgradeList.add(DAMAGE_BOOST);
        upgradeList.add(NIGHT_VISION);

        upgradeList.sort(Comparator.comparing(ArmorUpgrade::getUpgradeId));
        return upgradeList.toArray(new ArmorUpgrade[0]);
    }

    //todo: use Enum instead of String

    /**
     * Call as infrequently as possible.
     * @return the corresponding upgrade given the UpgradeId
     */
    public static ArmorUpgrade getArmorUpgradeFromString(String upgradeId){
        for(ArmorUpgrade upgrade : getAllUpgrades()){
            if(upgrade.getUpgradeId().equals(upgradeId)){
                return upgrade;
            }
        }
        return null;
    }



}
