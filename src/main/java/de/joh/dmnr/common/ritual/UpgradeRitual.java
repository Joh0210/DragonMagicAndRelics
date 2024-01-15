package de.joh.dmnr.common.ritual;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.PresentItem;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.item.UpgradeSealItem;
import de.joh.dmnr.api.item.IDragonMagicContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * This ritual adds the upgrade from the Dragon Mage Armor Input Upgrade Seal.
 * @see ArmorUpgradeInit
 * @see UpgradeSealItem
 * @see DragonMageArmorItem
 * @author Joh0210
 */
public class UpgradeRitual extends RitualEffect {

    public UpgradeRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getCenter();

        List<ItemStack> upgradeSealList = context.getCollectedReagents((r) -> r.getItem() instanceof UpgradeSealItem);
        List<ItemStack> dMContainerList = context.getCollectedReagents((r) -> r.getItem() instanceof IDragonMagicContainerItem);

        if (upgradeSealList.size() == 1 && dMContainerList.size() == 1) {
            if(upgradeSealList.get(0).getItem() instanceof UpgradeSealItem upgradeSeal && dMContainerList.get(0).getItem() instanceof IDragonMagicContainerItem dMContainer){
                int currentLevel = dMContainer.getUpgradeLevel(dMContainerList.get(0), upgradeSeal.getArmorUpgrade());
                if(upgradeSeal.getArmorUpgrade().maxUpgradeLevel <= currentLevel){
                    errorOccurred(Component.translatable("dmnr.ritual.output.upgrade.ritual.already_at_max.error"), context);
                    return false;
                }

                if(!dMContainer.addDragonMagicToItem(dMContainerList.get(0), upgradeSeal.getArmorUpgrade(), currentLevel + 1, false)){
                    errorOccurred(Component.translatable("dmnr.ritual.output.upgrade.ritual.unexpected.error"), context);
                    return false;
                }

                PresentItem item = new PresentItem(world, (double)pos.getX() + 0.5D, pos.getY() + 1 , (double)pos.getZ() + 0.5D, dMContainerList.get(0));
                world.addFreshEntity(item);

                return true;
            }
        }
        errorOccurred(Component.translatable("dmnr.ritual.output.upgrade.ritual.wrong.ritual.arrangement.error"), context);
        return false;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    /**
     * An error has occurred and the ritual ends.
     * Any item used in the ritual will be returned and the error will be thrown.
     * @param text output text
     */
    private void errorOccurred(Component text, IRitualContext context){
        List<ItemStack> reagents = context.getCollectedReagents();
        Player caster = context.getCaster();
        Level world = context.getLevel();

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
