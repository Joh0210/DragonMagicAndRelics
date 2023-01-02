package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Adds the input spell to your Dragon Mage Armor.
 * The spell is cast when the wearer of the Dragon Mage Armor takes damage.
 * If the input includes a netherite sword, the spell is stored as an offensive spell.
 * Offensive spells hit the attacker. Defensive spells hit you.
 * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
 * @author Joh0210
 */
public class FusionRitual extends RitualEffect {

    public FusionRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player caster = context.getCaster();
        Level world = context.getWorld();
        BlockPos pos = context.getCenter();
        ItemStack chest = caster.getItemBySlot(EquipmentSlot.CHEST);

        boolean offensive = context.getCollectedReagents((i) -> {
            return i.getItem() == Items.NETHERITE_SWORD;
        }).size() == 1;

        String type = (offensive) ? "spell_other" : "spell_self";

        List<ItemStack> spell = context.getCollectedReagents((r) -> {
            return r.getItem() == ItemInit.SPELL.get();
        });

        if (spell.size() == 1) {
            if (!spell.get(0).isEmpty() && SpellRecipe.stackContainsSpell(spell.get(0)) && !caster.level.isClientSide){
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put(DragonMagicAndRelics.MOD_ID+type, spell.get(0).getTag().getCompound("spell"));
                compoundTag.putString(DragonMagicAndRelics.MOD_ID+type+"_name", spell.get(0).getTag().getCompound("display").toString().replace( "{Name:'{\"text\":\"", "").replace( "\"}'}", ""));
                chest.getTag().merge(compoundTag);

                LightningBolt lightningboltentity = (LightningBolt) EntityType.LIGHTNING_BOLT.create(world);
                lightningboltentity.setPos((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
                world.addFreshEntity(lightningboltentity);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Player caster = context.getCaster();

        if(!(caster.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor mmaArmor) || !mmaArmor.isSetEquipped(caster)){
            return new TranslatableComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error");
        }

        return null;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
