package de.joh.dmnr.common.dragonmagic;

import de.joh.tnl.api.item.DragonMagicRegistry;
import de.joh.tnl.api.item.IDragonMagicItem;
import net.minecraft.resources.ResourceLocation;

/**
 * Dragon Magic of an item of another mod (e.g. Mana and Artifice), registered in {@link DragonMagicRegistry}.
 * Tomes And Libs adds the tooltip line and the enchantment glint; the effect is implemented by event handlers, which
 * check {@link #hasDragonMagic(net.minecraft.world.entity.LivingEntity)}.
 * <br> The translation key of the description is {@code item.<namespace>.<item_id>.dragonmagic}.
 * @author Joh0210
 */
public abstract class ExternalDragonMagic implements IDragonMagicItem {
    private final ResourceLocation itemId;
    private final String dragonMagicID;

    protected ExternalDragonMagic(ResourceLocation itemId) {
        this.itemId = itemId;
        this.dragonMagicID = "item." + itemId.getNamespace() + "." + itemId.getPath() + ".dragonmagic";
    }

    /**
     * @return the ID of the item that gets this Dragon Magic
     */
    public ResourceLocation getItemId() {
        return itemId;
    }

    @Override
    public String dragonMagicID() {
        return dragonMagicID;
    }
}
