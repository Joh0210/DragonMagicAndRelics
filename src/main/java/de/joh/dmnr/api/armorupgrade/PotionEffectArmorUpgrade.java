package de.joh.dmnr.api.armorupgrade;

import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades that add a potion effect to the wearer of Dragon Mage Armor.
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class PotionEffectArmorUpgrade extends OnEquippedArmorUpgrade { //extends IArmorUpgradeOnTick{

    /**
     * Time you want the effect to last. Will be reseted every tick.
     */
    public static final int EFFECT_DURATION = -1;
    private final int factor;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param factor Optional. Factor by which The Potion Effect should be amplified.
     */
    public PotionEffectArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, int factor, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, isInfStackable, supportsOnExtraLevel, upgradeCost);
        this.factor = factor;
    }

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     */
    public PotionEffectArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, boolean isInfStackable, int upgradeCost) {
        this(registryName, maxUpgradeLevel, upgradeSealItem, 1, isInfStackable, true, upgradeCost);
    }

    @NotNull
    public abstract MobEffect getMobEffect();

    /**
     * Adds or updates the Potion Effect for the Dragon Mage Armor wearer.
     * @see DragonMageArmorItem
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    public void applyPotionAffect(Player player, int level){
        MobEffectInstance instance = player.getEffect(getMobEffect());
        if(instance == null || instance.getAmplifier() < (level*factor - 1)){
            player.addEffect(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, level*factor - 1, false, false, false));
        }
        else{
            //Update the duration of the effect.
            instance.update(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, level*factor - 1, false, false, false));
        }
    }

    @Override
    public void onRemove(Player player) {
        player.removeEffect(getMobEffect());
    }

    //todo reapply the Effekt if it was removed while the glyph is aktive
//    @Override
//    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
//        applyPotionAffect(player, level);
//    }

    @Override
    public void onEquip(Player player, int level){
        applyPotionAffect(player, level);
    }
}
