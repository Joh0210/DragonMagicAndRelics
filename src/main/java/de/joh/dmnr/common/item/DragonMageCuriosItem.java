package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.event.DamageEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see DamageEventHandler
 * @author Joh0210
 */
public class DragonMageCuriosItem extends Item implements ITieredItem<DragonMageCuriosItem>, ICurioItem {
    private final int maxDragonMagic;
    private final String dmSource;

    private int _tier = -1;

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    public DragonMageCuriosItem(int maxDragonMagic, String dmSource, Properties pProperties) {
        super(pProperties);
        this.maxDragonMagic = maxDragonMagic;
        this.dmSource = dmSource;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }
}
