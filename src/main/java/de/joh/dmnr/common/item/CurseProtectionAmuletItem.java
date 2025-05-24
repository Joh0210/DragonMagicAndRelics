package de.joh.dmnr.common.item;

import com.mna.api.items.ChargeableItem;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.spell.shape.CurseShape;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) and of the {@link CurseShape Curse Shape}
 * @author Joh0210
 */
public class CurseProtectionAmuletItem extends ChargeableItem implements IPreEnchantedItem<CurseProtectionAmuletItem> {
    public CurseProtectionAmuletItem() {
        super((new Item.Properties()).setNoRepair().rarity(Rarity.EPIC), 2000.0F);
    }

    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    protected boolean tickCurio() {
        return false;
    }

    /**
     * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) if amulet is equipped
     */
    public static void eventHandleDenyHarmful(MobEffectEvent.Applicable event){
        if(event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL
                && event.getEffectInstance().getDuration() < 6000
                && event.getEffectInstance().getDuration() > 0
                && event.getEffectInstance().getEffect().getCurativeItems().stream().anyMatch(s -> s.getItem() == Items.MILK_BUCKET)
        ){
            int amount = (int) Math.min(event.getEffectInstance().getDuration() * (event.getEffectInstance().getAmplifier() +1) / 20.0f, 150.0f);
            if(((CurseProtectionAmuletItem) ItemInit.CURSE_PROTECTION_AMULET.get()).isEquippedAndHasMana(event.getEntity(), amount, true)){
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
