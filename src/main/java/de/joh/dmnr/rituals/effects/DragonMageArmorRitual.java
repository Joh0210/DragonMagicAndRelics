package de.joh.dmnr.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.events.additional.DragonUpgradeEvent;
import de.joh.dmnr.events.additional.HasMaxFactionEvent;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

/**
 * This ritual upgrades faction armor to Dragon Mage Armor.
 * Depending on the faction armor, different initial upgrades will be installed.
 * The armor, conditions, and initial upgrades can be customized by handlers.
 * @see DragonUpgradeEvent
 * @see HasMaxFactionEvent
 * @see DragonMageArmor
 * @author Joh0210
 */
public class DragonMageArmorRitual extends RitualEffect {
    public DragonMageArmorRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player player = context.getCaster();
        Level world = context.getWorld();
        BlockPos pos = context.getCenter();

        HasMaxFactionEvent factionEvent = new HasMaxFactionEvent(player);
        MinecraftForge.EVENT_BUS.post(factionEvent);


        DragonUpgradeEvent event = new DragonUpgradeEvent(context.getCaster(), factionEvent.getTargetFaction());
        MinecraftForge.EVENT_BUS.post(event);

        if(event.canBeUpgraded()){
            event.performUpgrade(true);

            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(world);
            lightningbolt.setPos((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
            world.addFreshEntity(lightningbolt);

            return  true;
        }

        return false;
    }


    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Player player = context.getCaster();

        HasMaxFactionEvent event = new HasMaxFactionEvent(player);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.hasMaxFactionArmor()){
            return new TranslatableComponent("dmnr.ritual.output.dragonmagearmorritual.wrong.armor.error");
        }
        final boolean[] isLevel75 = {false};

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> isLevel75[0] = 75 <= m.getMagicLevel());

        return isLevel75[0] ? null : new TranslatableComponent("dmnr.ritual.output.dragonmagearmorritual.to.low.level.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
