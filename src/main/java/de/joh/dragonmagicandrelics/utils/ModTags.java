package de.joh.dragonmagicandrelics.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Initialization of the tags. Used for the UpgradeSeals.
 * @author Joh0210
 */
public class ModTags {
    public static boolean isItemEqual(ItemStack stack, ResourceLocation rLoc) {
        if (ForgeRegistries.ITEMS.containsKey(rLoc)) {
            return ForgeRegistries.ITEMS.getValue(rLoc) == stack.getItem();
        } else {
            return isItemIn(stack.getItem(), rLoc);
        }
    }

    public static List<Item> getItemTagContents(ResourceLocation tagID) {
        try {
            ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(ForgeRegistries.ITEMS.tags().createTagKey(tagID));
            if (tag != null) {
                return (List)tag.stream().collect(Collectors.toList());
            }
        } catch (Exception var2) {
        }

        return new ArrayList();
    }

    public static List<Block> getBlockTagContents(ResourceLocation tagID) {
        try {
            ITag<Block> tag = ForgeRegistries.BLOCKS.tags().getTag(ForgeRegistries.BLOCKS.tags().createTagKey(tagID));
            if (tag != null) {
                return (List)tag.stream().collect(Collectors.toList());
            }
        } catch (Exception var2) {
        }

        return new ArrayList();
    }

    public static List<Item> smartLookupItem(ResourceLocation rLoc) {
        if (rLoc == null) {
            return new ArrayList();
        } else {
            return ForgeRegistries.ITEMS.containsKey(rLoc) ? Arrays.asList((Item)ForgeRegistries.ITEMS.getValue(rLoc)) : getItemTagContents(rLoc);
        }
    }

    public static ItemStack lookupItem(ResourceLocation rLoc) {
        return ForgeRegistries.ITEMS.containsKey(rLoc) ? new ItemStack((ItemLike)ForgeRegistries.ITEMS.getValue(rLoc)) : ItemStack.EMPTY;
    }

    public static boolean isBlockIn(Block block, ResourceLocation tag) {
        try {
            return getBlockTagContents(tag).contains(block);
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean isItemIn(Item item, ResourceLocation tag) {
        try {
            return getItemTagContents(tag).contains(item);
        } catch (Exception var3) {
            return false;
        }
    }

    public static class Items {

        public static ResourceLocation UPGRADE_SEAL_TIER_I = RLoc.create("upgrade_seal_tier_i");
        public static ResourceLocation UPGRADE_SEAL_TIER_II = RLoc.create("upgrade_seal_tier_ii");
        public static ResourceLocation UPGRADE_SEAL_TIER_III = RLoc.create("upgrade_seal_tier_iii");
        public static ResourceLocation UPGRADE_SEAL_TIER_IV = RLoc.create("upgrade_seal_tier_iv");

        public Items() {
        }
    }
}
