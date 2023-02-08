package de.joh.dragonmagicandrelics.rituals.contexts;

import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.item.items.UpgradeSeal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import java.util.List;

/**
 * This ritual adds the upgrade from the Dragon Mage Armor Input Upgrade Seal.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @see UpgradeSeal
 * @see DragonMageArmor
 * @author Joh0210
 */
public class UpgradeRitual extends RitualEffect {

    public UpgradeRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        PlayerEntity caster = context.getCaster();
        World world = context.getWorld();
        BlockPos pos = context.getCenter();



        List<ItemStack> upgradeSealList = context.getCollectedReagents((r) -> r.getItem() instanceof UpgradeSeal);

        if (upgradeSealList.size() == 1) {
            if(upgradeSealList.get(0).getItem() instanceof UpgradeSeal){
                UpgradeSeal upgradeSeal = (UpgradeSeal) upgradeSealList.get(0).getItem();
                if(caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof DragonMageArmor){
                    DragonMageArmor mmaArmor = (DragonMageArmor) caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem();
                    if(!mmaArmor.isSetEquipped(caster)){
                        errorOccurred(new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error"), context);
                        return false;
                    }

                    if(upgradeSeal.getUpgradeLevel() < mmaArmor.getUpgradeLevel(upgradeSeal.getArmorUpgrade(), caster)){
                        errorOccurred(new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.already.installed.error"), context);
                        return false;
                    }

                    if(mmaArmor.getUpgradeLevel(upgradeSeal.getArmorUpgrade(), caster) + 1 != upgradeSeal.getUpgradeLevel()){
                        TranslationTextComponent component1 = new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.missing.predecessor.one.error");
                        TranslationTextComponent component2 = new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.missing.predecessor.two.error");

                        errorOccurred(new StringTextComponent(component1.getString() + (upgradeSeal.getUpgradeLevel() -1) + component2.getString()), context);
                        return false;
                    }
                    mmaArmor.setUpgradeLevel(upgradeSeal.getArmorUpgrade(), upgradeSeal.getUpgradeLevel(), caster);

                    LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningboltentity.setPosition((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
                    world.addEntity(lightningboltentity);

                    return true;

                }
            }
        }
        errorOccurred(new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.wrong.ritual.arrangement.error"), context);
        return false;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public TextComponent canRitualStart(IRitualContext context) {
        PlayerEntity caster = context.getCaster();
        if(!(caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof DragonMageArmor) || !((DragonMageArmor)caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()).isSetEquipped(caster)){
            return new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error");
        }

        return null;
    }

    /**
     * An error has occurred and the ritual ends.
     * Any item used in the ritual will be returned and the error will be thrown.
     * @param text output text
     */
    private void errorOccurred(TextComponent text, IRitualContext context){
        List<ItemStack> reagents = context.getCollectedReagents();
        PlayerEntity caster = context.getCaster();
        World world = context.getWorld();

        caster.sendStatusMessage(text, false);
        for(ItemStack itemStack : reagents){
            ItemEntity item = new ItemEntity(world, caster.getPosX(), caster.getPosY(), caster.getPosZ(), itemStack);
            world.addEntity(item);
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
