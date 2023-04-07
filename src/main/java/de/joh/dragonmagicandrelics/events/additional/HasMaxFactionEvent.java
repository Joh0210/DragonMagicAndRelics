package de.joh.dragonmagicandrelics.events.additional;

import com.mna.api.capabilities.Faction;
import com.mna.api.items.IFactionSpecific;
import com.mna.items.armor.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * If the Dragon Mage Armor Ritual is to be started, the wearer must be wearing appropriate armor.
 * By default, only the default M&A armor is recognized, if you intercept this function in handlers,
 * you can determine whether he is wearing armor from a new faction and which faction it is.
 * @see de.joh.dragonmagicandrelics.rituals.contexts.DragonMageArmorRitual
 * @author Joh0210
 */
public class HasMaxFactionEvent extends PlayerEvent {
    private boolean hasMaxFactionArmor;
    private Faction targetFaction;

    public HasMaxFactionEvent(Player player) {
        super(player);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        hasMaxFactionArmor = (chest.getItem() instanceof BoneArmorItem || chest.getItem() instanceof CouncilArmorItem || chest.getItem() instanceof DemonArmorItem || chest.getItem() instanceof FeyArmorItem) && ((ISetItem) chest.getItem()).isSetEquipped(player);
        if(chest.getItem() instanceof IFactionSpecific){
            targetFaction = ((IFactionSpecific)chest.getItem()).getFaction();
        }
    }

    /**
     * Revising the results of this event
     * @param hasMaxFactionArmor Is the player wearing maximum faction armor?
     * @param targetFaction Which faction does this armor belong to?
     */
    public void setValues(boolean hasMaxFactionArmor, Faction targetFaction){
        this.hasMaxFactionArmor = hasMaxFactionArmor;
        this.targetFaction = targetFaction;
    }

    public boolean hasMaxFactionArmor() {
        return hasMaxFactionArmor;
    }

    public Faction getTargetFaction(){
        return targetFaction;
    }
}
