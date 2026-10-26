package de.joh.dmnr.common.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * The Curios slots the curios of this mod can be equipped in.
 * <br>The slot of a curio is set at its registration in {@link de.joh.dmnr.common.init.ItemInit}; the data generators
 * add the curio to the item tag of the slot ({@code curios:<slot>}), through which Curios allows it in that slot.
 * @author Joh0210
 */
public enum CurioSlot {
    BACK("back"),
    BELT("belt"),
    BRACELET("bracelet"),
    CHARM("charm"),
    HEAD("head"),
    NECKLACE("necklace"),
    RING("ring");

    private final String id;

    CurioSlot(String id) {
        this.id = id;
    }

    /**
     * @return the identifier of the slot in Curios
     */
    public String getId() {
        return id;
    }

    /**
     * @return the item tag that allows items in this slot
     */
    public TagKey<Item> getTag() {
        return ItemTags.create(new ResourceLocation("curios", id));
    }
}
