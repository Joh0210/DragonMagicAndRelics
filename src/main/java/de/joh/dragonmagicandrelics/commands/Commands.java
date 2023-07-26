package de.joh.dragonmagicandrelics.commands;

import com.mna.api.faction.IFaction;
import com.mna.factions.Factions;
import com.mna.spells.crafting.SpellRecipe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.events.additional.DragonUpgradeEvent;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.item.util.DragonMagicContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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
                .then(changeDragonMageArmor())
                .then(addSpellToArmor()));
    }

    private ArgumentBuilder<CommandSourceStack, ?> changeDragonMageArmor() {
        return ((LiteralArgumentBuilder) net.minecraft.commands.Commands.literal("changeDragonMageArmor")
                .then(changeDragonMageArmorTarget(Factions.UNDEAD)))
                .then(changeDragonMageArmorTarget(Factions.DEMONS))
                .then(changeDragonMageArmorTarget(Factions.COUNCIL))
                .then(changeDragonMageArmorTarget(Factions.FEY));
    }

    private ArgumentBuilder<CommandSourceStack, ?> changeDragonMageArmorTarget(IFaction faction){
        String factionString = "none";
        if (Factions.DEMONS.equals(faction)) {
            factionString = "demons";
        } else if (Factions.FEY.equals(faction)) {
            factionString = "fey";
        } else if (Factions.COUNCIL.equals(faction)) {
            factionString = "council";
        } else if (Factions.UNDEAD.equals(faction)) {
            factionString = "undead";
        }

        return net.minecraft.commands.Commands.literal(factionString).then(net.minecraft.commands.Commands.argument("target", EntityArgument.player()).executes((command) -> {
            ServerPlayer player = EntityArgument.getPlayer(command, "target");
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            if(chest.getItem() instanceof DragonMageArmor && ((DragonMageArmor) chest.getItem()).isSetEquipped(player)) {
                DragonUpgradeEvent event = new DragonUpgradeEvent(player, faction);
                MinecraftForge.EVENT_BUS.post(event);

                if (event.canBeUpgraded()) {
                    event.performUpgradeFromDMArmor();
                }
            } else {
                command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.changeDragonMageArmorTarget.no.valid.armor.error"), true);
            }
            return 1;
        }));
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
     * Adds the selected upgrade to the Dragon Mage Armor
     * The command contains another (int) level parameter
     */
    private static ArgumentBuilder<CommandSourceStack, ?> addUpgrade() {
        return net.minecraft.commands.Commands.literal("addUpgrade").then(net.minecraft.commands.Commands.argument("armor_upgrade", new ArmorUpgradeArgument()).then(net.minecraft.commands.Commands.argument("level", IntegerArgumentType.integer(0, 255)).then(net.minecraft.commands.Commands.argument("force", BoolArgumentType.bool()).executes((command) -> {
            ServerPlayer player = command.getSource().getPlayerOrException();

            if(!Registries.ARMOR_UPGRADE.get().containsKey(ResourceLocationArgument.getId(command, "armor_upgrade"))){
                command.getSource().sendSuccess((new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.upgradedoesnotexist.error")), true);
            } else {
                ItemStack item = player.getItemBySlot(EquipmentSlot.MAINHAND);
                if(item.getItem() instanceof DragonMagicContainer){
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(ResourceLocationArgument.getId(command, "armor_upgrade"));

                    int level = IntegerArgumentType.getInteger(command, "level");

                    if(!BoolArgumentType.getBool(command, "force") || !armorUpgrade.isInfStackable) {
                        level = Math.min(level, armorUpgrade.maxUpgradeLevel);
                    }

                    ((DragonMagicContainer) item.getItem()).addDragonMagicToItem(item, armorUpgrade, level, BoolArgumentType.getBool(command, "force"));
                    TranslatableComponent component_one = new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.one");
                    TranslatableComponent component_two = new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.success.two");

                    command.getSource().sendSuccess(new TextComponent(component_one.getString() + armorUpgrade.getRegistryName().toString() + component_two.getString() + level), true);
                } else {
                    command.getSource().sendSuccess(new TranslatableComponent("dragonmagicandrelics.commands.output.callApplyUpgrade.no.armor.equipped.error"), true);
                }
            }
            return 1;
        }))));
    }
}
