package de.joh.dmnr.commands;

import com.mna.api.faction.IFaction;
import com.mna.factions.Factions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.Registries;
import de.joh.dmnr.armorupgrades.types.ArmorUpgrade;
import de.joh.dmnr.events.additional.DragonUpgradeEvent;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dmnr.item.util.IDragonMagicContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * Initialization of the commands. Each function is either a node for command creation or final execution of the command.
 * Registration through CommonEventHandler
 * @see de.joh.dmnr.events.CommonEventHandler
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
                .then(changeDragonMageArmor()));
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
                command.getSource().sendSuccess(Component.translatable("dmnr.commands.output.changeDragonMageArmorTarget.no.valid.armor.error"), true);
            }
            return 1;
        }));
    }

    /**
     * Adds the selected upgrade to the Dragon Mage Armor
     * The command contains another (int) level parameter
     */
    private static ArgumentBuilder<CommandSourceStack, ?> addUpgrade() {
        return net.minecraft.commands.Commands.literal("addUpgrade").then(net.minecraft.commands.Commands.argument("armor_upgrade", new ArmorUpgradeArgument()).then(net.minecraft.commands.Commands.argument("level", IntegerArgumentType.integer(0, 255)).then(net.minecraft.commands.Commands.argument("force", BoolArgumentType.bool()).executes((command) -> {
            ServerPlayer player = command.getSource().getPlayerOrException();

            if(!Registries.ARMOR_UPGRADE.get().containsKey(ResourceLocationArgument.getId(command, "armor_upgrade"))){
                command.getSource().sendSuccess((Component.translatable("dmnr.commands.output.callApplyUpgrade.upgradedoesnotexist.error")), true);
            } else {
                ItemStack item = player.getItemBySlot(EquipmentSlot.MAINHAND);
                if(item.getItem() instanceof IDragonMagicContainer){
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(ResourceLocationArgument.getId(command, "armor_upgrade"));

                    int level = IntegerArgumentType.getInteger(command, "level");

                    if(!BoolArgumentType.getBool(command, "force")) {
                        level = Math.min(level, armorUpgrade.maxUpgradeLevel);
                    } else if(!armorUpgrade.isInfStackable){
                        level = Math.min(level, armorUpgrade.supportsOnExtraLevel ? armorUpgrade.maxUpgradeLevel + 1 : armorUpgrade.maxUpgradeLevel);
                    }

                    ((IDragonMagicContainer) item.getItem()).addDragonMagicToItem(item, armorUpgrade, level, BoolArgumentType.getBool(command, "force"));
                    MutableComponent component_one = Component.translatable("dmnr.commands.output.callApplyUpgrade.success.one");
                    MutableComponent component_two = Component.translatable("dmnr.commands.output.callApplyUpgrade.success.two");

                    command.getSource().sendSuccess(Component.literal(component_one.getString() + armorUpgrade.getRegistryName().toString() + component_two.getString() + level), true);
                } else {
                    command.getSource().sendSuccess(Component.translatable("dmnr.commands.output.callApplyUpgrade.no.armor.equipped.error"), true);
                }
            }
            return 1;
        }))));
    }
}
