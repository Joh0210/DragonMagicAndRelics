package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.PresentItem;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dragonmagicandrelics.item.items.UpgradeSeal;
import de.joh.dragonmagicandrelics.item.util.IDragonMagicContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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
        Level world = context.getWorld();
        BlockPos pos = context.getCenter();

        List<ItemStack> upgradeSealList = context.getCollectedReagents((r) -> r.getItem() instanceof UpgradeSeal);
        List<ItemStack> dMContainerList = context.getCollectedReagents((r) -> r.getItem() instanceof IDragonMagicContainer);

        if (upgradeSealList.size() == 1 && dMContainerList.size() == 1) {
            if(upgradeSealList.get(0).getItem() instanceof UpgradeSeal upgradeSeal && dMContainerList.get(0).getItem() instanceof IDragonMagicContainer dMContainer){
                int currentLevel = dMContainer.getUpgradeLevel(dMContainerList.get(0), upgradeSeal.getArmorUpgrade());
                if(upgradeSeal.getArmorUpgrade().maxUpgradeLevel <= currentLevel){
                    errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.already_at_max.error"), context);
                    return false;
                }

                if(!dMContainer.addDragonMagicToItem(dMContainerList.get(0), upgradeSeal.getArmorUpgrade(), currentLevel + 1, false)){
                    errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.unexpected.error"), context);
                    return false;
                }

                PresentItem item = new PresentItem(world, (double)pos.getX() + 0.5D, pos.getY() + 1 , (double)pos.getZ() + 0.5D, dMContainerList.get(0));
                world.addFreshEntity(item);

                return true;
            }
        }
        errorOccurred(new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.wrong.ritual.arrangement.error"), context);
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
