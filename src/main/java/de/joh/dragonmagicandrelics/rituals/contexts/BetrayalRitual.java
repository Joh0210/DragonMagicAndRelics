package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.capabilities.Faction;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Ritual that frees the caster from his faction. The caster will be reset to tier 2 and level 29.
 * @author Joh0210
 */
public class BetrayalRitual extends RitualEffect {

    public BetrayalRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player caster = context.getCaster();
        BlockPos pos = context.getCenter();

        caster.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
            p.setFactionStanding(0);
            p.setAlliedFaction(Faction.NONE, caster);
            p.setTier(2, caster);
        });

        caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
            m.setMagicLevel(29);
        });

        LightningBolt lightningboltentity = (LightningBolt) EntityType.LIGHTNING_BOLT.create(context.getWorld());
        lightningboltentity.setPos((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
        context.getWorld().addFreshEntity(lightningboltentity);

        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
