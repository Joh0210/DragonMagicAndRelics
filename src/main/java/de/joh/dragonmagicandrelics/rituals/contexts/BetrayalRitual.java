package de.joh.dragonmagicandrelics.rituals.contexts;

import com.ma.api.capabilities.Faction;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import com.ma.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

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
        PlayerEntity caster = context.getCaster();
        BlockPos pos = context.getCenter();

        caster.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
            p.setFactionStanding(0);
            p.setAlliedFaction(Faction.NONE, caster);
            p.setTier(2, caster);
        });

        caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.setMagicLevel(29));

        LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(context.getWorld());
        lightningboltentity.setPosition((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
        context.getWorld().addEntity(lightningboltentity);

        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
