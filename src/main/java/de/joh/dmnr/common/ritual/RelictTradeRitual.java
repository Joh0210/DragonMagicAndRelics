package de.joh.dmnr.common.ritual;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.PresentItem;
import de.joh.dmnr.api.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RelictTradeRitual extends RitualEffect {

    public RelictTradeRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getCenter();

        PresentItem item = new PresentItem(world, (double)pos.getX() + 0.5D, pos.getY() + 1 , (double)pos.getZ() + 0.5D, new ItemStack(ModTags.getRandomItem(new ResourceLocation("mna", "relics"))));
        world.addFreshEntity(item);

        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
