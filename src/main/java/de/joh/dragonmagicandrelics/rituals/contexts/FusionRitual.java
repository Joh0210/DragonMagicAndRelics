package de.joh.dragonmagicandrelics.rituals.contexts;

import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;
import com.ma.items.ItemInit;
import com.ma.spells.crafting.SpellRecipe;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
        PlayerEntity caster = context.getCaster();
        World world = context.getWorld();
        BlockPos pos = context.getCenter();
        ItemStack chest = caster.getItemStackFromSlot(EquipmentSlotType.CHEST);

        boolean offensive = context.getCollectedReagents((i) -> i.getItem() == Items.NETHERITE_SWORD).size() == 1;

        String type = (offensive) ? "spell_other" : "spell_self";

        List<ItemStack> spell = context.getCollectedReagents((r) -> r.getItem() == ItemInit.SPELL.get());

        if (spell.size() == 1) {
            if (!spell.get(0).isEmpty() && SpellRecipe.stackContainsSpell(spell.get(0)) && !caster.world.isRemote){
                CompoundNBT compoundTag = new CompoundNBT();
                compoundTag.put(DragonMagicAndRelics.MOD_ID+type, spell.get(0).getTag().getCompound("spell"));
                compoundTag.putString(DragonMagicAndRelics.MOD_ID+type+"_name", spell.get(0).getTag().getCompound("display").toString().replace( "{Name:'{\"text\":\"", "").replace( "\"}'}", ""));
                chest.getTag().merge(compoundTag);

                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
                lightningboltentity.setPosition((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D);
                world.addEntity(lightningboltentity);
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
    public TextComponent canRitualStart(IRitualContext context) {
        PlayerEntity caster = context.getCaster();

        if(!(caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof DragonMageArmor) || !((DragonMageArmor)caster.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()).isSetEquipped(caster)){
            return new TranslationTextComponent("dragonmagicandrelics.ritual.output.upgrade.ritual.no.armor.equipped.error");
        }

        return null;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
