package de.joh.dragonmagicandrelics.armorupgrades.types;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades that add a potion effect to the wearer of Dragon Mage Armor.
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class IArmorUpgradePotionEffect extends IArmorUpgradeOnTick{
    /**
     * Time you want the effect to last. Will be reseted every tick.
     */
    public static final int EFFECT_DURATION = 10;
    private final int factor;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param factor Optional. Factor by which The Potion Effect should be amplified.
     */
    public IArmorUpgradePotionEffect(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int factor, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, supportsOnExtraLevel, upgradeCost);
        this.factor = factor;
    }

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     */
    public IArmorUpgradePotionEffect(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, int upgradeCost) {
        this(registryName, maxUpgradeLevel, 1, isInfStackable, true, upgradeCost);
    }

    public abstract MobEffect getMobEffect();

    /**
     * Adds or updates the Potion Effect for the Dragon Mage Armor wearer.
     * @see DragonMageArmor
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    public void applyPotionAffect(Player player, int level){
        if(!player.hasEffect(getMobEffect()) || player.getEffect(getMobEffect()).getAmplifier() < (level*factor - 1)){
            player.addEffect(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, level*factor - 1, false, false, false));
        }
        else{
            //Update the duration of the effect.
            player.getEffect(getMobEffect()).update(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, level*factor - 1, false, false, false));
        }
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        applyPotionAffect(player, level);
    }
}
