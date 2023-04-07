package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.armor.*;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.config.InitialUpgradesConfigs;
import de.joh.dragonmagicandrelics.events.additional.DragonUpgradeEvent;
import de.joh.dragonmagicandrelics.events.additional.HasMaxFactionEvent;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

        HasMaxFactionEvent factionEvent = new HasMaxFactionEvent(player);
        MinecraftForge.EVENT_BUS.post(factionEvent);


        DragonUpgradeEvent event = new DragonUpgradeEvent(context.getCaster(), factionEvent.getTargetFaction());
        MinecraftForge.EVENT_BUS.post(event);

        if(event.canBeUpgraded()){
            event.performUpgrade(true);
        }

        return  true;
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

        if (event.hasMaxFactionArmor()){
            return new TranslatableComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.wrong.armor.error");
        }
        final boolean[] isLevel75 = {false};

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> isLevel75[0] = 75 <= m.getMagicLevel());

        return isLevel75[0] ? null : new TranslatableComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.to.low.level.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
