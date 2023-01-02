package de.joh.dragonmagicandrelics.utils;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import net.minecraft.resources.ResourceLocation;

/**
 * Creation of the ResourceLocations for this mod.
 * @author Joh0210
 */
public class RLoc
{
    public static ResourceLocation create(final String path) {
        return new ResourceLocation(DragonMagicAndRelics.MOD_ID, path);
    }
}

