package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.item.items.UpgradeSeal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
        Player caster = context.getCaster();
        Level world = context.getWorld();
        BlockPos pos = context.getCenter();



        List<ItemStack> upgradeSealList = context.getCollectedReagents((r) -> {
            return r.getItem() instanceof UpgradeSeal;
        });

        if (upgradeSealList.size() == 1) {
            if(upgradeSealList.get(0).getItem() instanceof UpgradeSeal upgradeSeal){
                if(caster.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor mmaArmor){
                    if(!mmaArmor.isSetEquipped(caster)){
                        errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error"), context);
                        return false;
                    }

                    if(upgradeSeal.getUpgradeLevel() < mmaArmor.getUpgradeLevel(upgradeSeal.getArmorUpgrade(), caster)){
                        errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.already.installed.error"), context);
                        return false;
                    }

                    if(mmaArmor.getUpgradeLevel(upgradeSeal.getArmorUpgrade(), caster) + 1 != upgradeSeal.getUpgradeLevel()){
                        TranslatableComponent component1 = new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.missing.predecessor.one.error");
                        TranslatableComponent component2 = new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.missing.predecessor.two.error");

                        errorOccurred(new TextComponent(component1.getString() + (upgradeSeal.getUpgradeLevel() -1) + component2.getString()), context);
                        return false;
                    }
                    mmaArmor.setUpgradeLevel(upgradeSeal.getArmorUpgrade(), upgradeSeal.getUpgradeLevel(), caster);

                    LightningBolt lightningboltentity = (LightningBolt) EntityType.LIGHTNING_BOLT.create(world);
                    lightningboltentity.setPos((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
                    world.addFreshEntity(lightningboltentity);

                    return true;

                }
            }
        }
        errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.wrong.ritual.arrangement.error"), context);
        return false;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Player caster = context.getCaster();
        if(!(caster.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor mmaArmor) || !mmaArmor.isSetEquipped(caster)){
            return new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error");
        }

        return null;
    }

    /**
     * An error has occurred and the ritual ends.
     * Any item used in the ritual will be returned and the error will be thrown.
     * @param text output text
     */
    private void errorOccurred(Component text, IRitualContext context){
        List<ItemStack> reagents = context.getCollectedReagents();
        Player caster = context.getCaster();
        Level world = context.getWorld();

        caster.displayClientMessage(text, false);
        for(ItemStack itemStack : reagents){
            ItemEntity item = new ItemEntity(world, caster.getX(), caster.getY(), caster.getZ(), itemStack);
            world.addFreshEntity(item);
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
