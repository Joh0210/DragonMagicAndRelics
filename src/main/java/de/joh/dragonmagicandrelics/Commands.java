package de.joh.dragonmagicandrelics;

import com.mna.spells.crafting.SpellRecipe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
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
    public Commands(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(net.minecraft.commands.Commands.literal(DragonMagicAndRelics.MOD_ID).requires((commandSource) -> commandSource.hasPermission(2))
                .then(addUpgrade())
                .then(addSpellToArmor()));
    }

    /**
     * Adds the spell in your main hand to your Dragon Mage Armor.
     * The spell is cast when the wearer of the Dragon Mage Armor takes damage.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    private ArgumentBuilder<CommandSourceStack, ?> addSpellToArmor() {
        return ((LiteralArgumentBuilder) net.minecraft.commands.Commands.literal("addSpellToArmor")
                .then(writeSpellToArmor(false)))
                .then(writeSpellToArmor(true));
    }

    /**
     * Actually adding the spell to the Dragon Mage Armor.
     * @param isOffensive Hit the attacker? Doesn't hit you?
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    private ArgumentBuilder<CommandSourceStack, ?> writeSpellToArmor(boolean isOffensive) {
        return net.minecraft.commands.Commands.literal(isOffensive ? "offensive" : "defensive").executes((command) -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            ItemStack spellInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            if (!spellInHand.isEmpty() && SpellRecipe.stackContainsSpell(spellInHand) && !player.level.isClientSide){

                if(chest == null || !(chest.getItem() instanceof DragonMageArmor) || !((DragonMageArmor) chest.getItem()).isSetEquipped(player)){
                    command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.no.armor.equipped.error"), true);
                    return 1;
                }

                String name = spellInHand.getTag().getCompound("display").toString().replace( "{Name:'{\"text\":\"", "").replace( "\"}'}", "");

                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put(DragonMagicAndRelics.MOD_ID+(isOffensive ? "spell_other" : "spell_self"), spellInHand.getTag().getCompound("spell"));
                compoundTag.putString(DragonMagicAndRelics.MOD_ID+(isOffensive ? "spell_other" : "spell_self")+"_name", name);
                chest.getTag().merge(compoundTag);

                //Text Output
                TranslatableComponent component_one = new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.one");
                TranslatableComponent component_two = new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.two");
                TranslatableComponent component_three = new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.three");
                TranslatableComponent component_offensive = new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.offensive");
                TranslatableComponent component_defensive = new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.success.defensive");

                command.getSource().sendSuccess(new TextComponent(component_one.getString() + name + component_two.getString() + (isOffensive ? component_offensive.getString() : component_defensive.getString()) + component_three.getString()), true);
            }
            else{
                command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.writeSpellToArmor.no.valid.spell.error"), true);
            }
            return 1;
        });
    }

    /**
     * Adds one of the upgrades to your Dragon Mage Armor.
     */
    private ArgumentBuilder<CommandSourceStack, ?> addUpgrade() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) net.minecraft.commands.Commands.literal("addUpgrade")
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
    private ArgumentBuilder<CommandSourceStack, ?> applyUpgrade(String upgradeName) {
        return net.minecraft.commands.Commands.literal(upgradeName).then(net.minecraft.commands.Commands.argument("level", IntegerArgumentType.integer(0, (ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName) != null ? ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName).getMaxUpgradeLevel() : 0))).executes((command) -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.getItem() instanceof DragonMageArmor){
                if(ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName) == null){
                    command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.upgradedoesnotexist.error"), true);
                } else if (((DragonMageArmor) chest.getItem()).isSetEquipped(player)){
                    TranslatableComponent component_one = new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.one");
                    TranslatableComponent component_two = new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.two");

                    command.getSource().sendSuccess(new TextComponent(component_one.getString() + upgradeName + component_two.getString() + IntegerArgumentType.getInteger(command, "level")), true);
                    ((DragonMageArmor) chest.getItem()).setUpgradeLevel(ArmorUpgradeInit.getArmorUpgradeFromString(upgradeName), IntegerArgumentType.getInteger(command, "level"), player);
                }else{
                    command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.no.armor.equipped.error"), true);
                }
            }
            return 1;
        }));
    }
}
