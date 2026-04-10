package de.joh.dmnr.common.ritual;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * This ritual upgrades faction armor to Dragon Mage Armor.
 * Depending on the faction armor, different initial upgrades will be installed.
 * The armor, conditions, and initial upgrades can be customized by handlers.
 * @see DragonMageArmorItem
 * @author Joh0210
 */
public class DragonMageArmorRitual extends RitualEffect {
    public DragonMageArmorRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player player = context.getCaster();
        Level world = context.getLevel();
        BlockPos pos = context.getCenter();

        // todo:

        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(world);
        lightningbolt.setPos((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
        world.addFreshEntity(lightningbolt);

        return  true;
    }


    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
