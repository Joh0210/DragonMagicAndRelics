package de.joh.dragonmagicandrelics;

import com.ma.spells.crafting.SpellRecipe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.Mod;

/**
 * Initialization of the commands. Each function is either a node for command creation or final execution of the command.
 * Registration through CommonEventHandler
 * @see de.joh.dragonmagicandrelics.events.CommonEventHandler
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Commands {
    /**
     * Root for all commands of this mod
     * @param dispatcher Standard parameters for commands
     */
    public Commands(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(net.minecraft.command.Commands.literal(DragonMagicAndRelics.MOD_ID).requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(addUpgrade())
                .then(addSpellToArmor()));
    }

    /**
     * Adds the spell in your main hand to your Dragon Mage Armor.
     * The spell is cast when the wearer of the Dragon Mage Armor takes damage.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    private ArgumentBuilder<CommandSource, ?> addSpellToArmor() {
        return ((LiteralArgumentBuilder) net.minecraft.command.Commands.literal("addSpellToArmor")
                .then(writeSpellToArmor(false)))
                .then(writeSpellToArmor(true));
    }

    /**
     * Actually adding the spell to the Dragon Mage Armor.
     * @param isOffensive Hit the attacker? Doesn't hit you?
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    private ArgumentBuilder<CommandSource, ?> writeSpellToArmor(boolean isOffensive) {
        return net.minecraft.command.Commands.literal(isOffensive ? "offensive" : "defensive").executes((command) -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            ItemStack spellInHand = player.getHeldItem(Hand.MAIN_HAND);
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);

            if (!spellInHand.isEmpty() && SpellRecipe.stackContainsSpell(spellInHand) && !player.world.isRemote){

                if(chest == null || !(chest.getItem() instanceof DragonMageArmor) || !((DragonMageArmor) chest.getItem()).isSetEquipped(player)){
                    command.getSource().sendFeedback(new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.no.armor.equipped.error"), true);
                    return 1;
                }

                String name = spellInHand.getTag().getCompound("display").toString().replace( "{Name:'{\"text\":\"", "").replace( "\"}'}", "");

                CompoundNBT compoundTag = new CompoundNBT();
                compoundTag.put(DragonMagicAndRelics.MOD_ID+(isOffensive ? "spell_other" : "spell_self"), spellInHand.getTag().getCompound("spell"));
                compoundTag.putString(DragonMagicAndRelics.MOD_ID+(isOffensive ? "spell_other" : "spell_self")+"_name", name);
                chest.getTag().merge(compoundTag);

                //Text Output
                TranslationTextComponent component_one = new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.one");
                TranslationTextComponent component_two = new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.two");
                TranslationTextComponent component_three = new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.three");
                TranslationTextComponent component_offensive = new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.offensive");
                TranslationTextComponent component_defensive = new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.defensive");

                command.getSource().sendFeedback(new StringTextComponent(component_one.getString() + name + component_two.getString() + (isOffensive ? component_offensive.getString() : component_defensive.getString()) + component_three.getString()), true);
            }
            else{
                command.getSource().sendFeedback(new TranslationTextComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.no.valid.spell.error"), true);
            }
            return 1;
        });
    }

    /**
     * Adds one of the upgrades to your Dragon Mage Armor.
     */
    private ArgumentBuilder<CommandSource, ?> addUpgrade() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.command.Commands.literal("addUpgrade")
        .then(applyUpgrade("fly"))
        .then(applyUpgrade("saturation")))
        .then(applyUpgrade("movement_speed"))
        .then(applyUpgrade("water_breathing"))
        .then(applyUpgrade("regeneration"))
        .then(applyUpgrade("night_vision"))
        .then(applyUpgrade("dolphins_grace"))
        .then(applyUpgrade("eldrin_sight"))
        .then(applyUpgrade("wellspring_sight"))
        .then(applyUpgrade("mana_boost"))
        .then(applyUpgrade("mana_regen"))
        .then(applyUpgrade("health_boost"))
        .then(applyUpgrade("jump"))
        .then(applyUpgrade("kinetic_resistance"))
        .then(applyUpgrade("explosion_resistance"))
        .then(applyUpgrade("mist_form"))
        .then(applyUpgrade("projectile_reflection"))
        .then(applyUpgrade("meteor_jump"))
        .then(applyUpgrade("fire_resistance"))
        .then(applyUpgrade("elytra"))
        .then(applyUpgrade("damage_resistance"))
        .then(applyUpgrade("damage_boost")));
    }

    /**
     * Adds the selected upgrade to the Dragon Mage Armor
     * In the command there is another level parameter
     * @param upgradeName ID of the corresponding upgrade
     * @see ArmorUpgradeInit
     */
    private ArgumentBuilder<CommandSource, ?> applyUpgrade(String upgradeName) {
        return net.minecraft.command.Commands.literal(upgradeName).then(net.minecraft.command.Commands.argument("level", IntegerArgumentType.integer(0, (ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName) != null ? ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName).getMaxUpgradeLevel() : 0))).executes((command) -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if (chest.getItem() instanceof DragonMageArmor){
                if(ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName) == null){
                    command.getSource().sendFeedback(new TranslationTextComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.upgradedoesnotexist.error"), true);
                } else if (((DragonMageArmor) chest.getItem()).isSetEquipped(player)){
                    TranslationTextComponent component_one = new TranslationTextComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.one");
                    TranslationTextComponent component_two = new TranslationTextComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.two");

                    command.getSource().sendFeedback(new StringTextComponent(component_one.getString() + upgradeName + component_two.getString() + IntegerArgumentType.getInteger(command, "level")), true);
                    ((DragonMageArmor) chest.getItem()).setUpgradeLevel(ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName), IntegerArgumentType.getInteger(command, "level"), player);
                }else{
                    command.getSource().sendFeedback(new TranslationTextComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.no.armor.equipped.error"), true);
                }
            }
            return 1;
        }));
    }
}
