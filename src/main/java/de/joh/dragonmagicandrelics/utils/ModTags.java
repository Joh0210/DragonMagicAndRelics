package de.joh.dragonmagicandrelics.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Initialization of the tags. Used for the UpgradeSeals.
 * @author Joh0210
 */
public class ModTags {
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

    public static Block getRandomBlocks(ResourceLocation tag) {
    //public static Block getRandomBlocks(ResourceLocation tag, ResourceLocation additionalTag) {
        try {
            Random random = new Random();
            List<Block> list = getBlockTagContents(tag);
//            if(additionalTag != null){
//                list.addAll(getBlockTagContents(additionalTag));
//            }
            return list.get(random.nextInt(list.size()));
        } catch (Exception var3) {
            return null;
        }
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

    public static class Blocks {
        public static ResourceLocation TALL_FLOWERS = new ResourceLocation("minecraft","tall_flowers");
        public static ResourceLocation MUTANDIS_PLANTS = RLoc.create("mutandis_plants");
        public static ResourceLocation MNA_FLOWERS = RLoc.create("mna_flowers");

        public Blocks() {
        }
    }

    public static class Items {

        public static ResourceLocation UPGRADE_SEAL_TIER = RLoc.create("upgrade_seal_tier");
        public static ResourceLocation DRAGON_MAGIC_CONTAINER = RLoc.create("dragon_magic_container");

        public Items() {
        }
    }
}
