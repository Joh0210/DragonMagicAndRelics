package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import com.mna.api.sound.SFX;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * An Ring that allows the user to walk at an accelerated speed, jump higher and walk over blocks
 * The boost increases after 5 seconds of running
 * @author Joh0210
 */
public class OcelotCurioItem extends TieredItem implements ICurioItem {
    private final static String SPRINT_TIME_ID = DragonMagicAndRelics.MOD_ID + "OcelotSprintTime";
    private final AttributeModifier baseSpeed;
    private final AttributeModifier upgradeSpeed1;
    private final AttributeModifier upgradeSpeed2;
    private final AttributeModifier baseStep;
    private final AttributeModifier upgradeStep1;
    private final AttributeModifier upgradeStep2;
    private final int level;

    // todo Upgrade: multiplies the total speed of the player
    public OcelotCurioItem(int level) {
        super(new Properties().stacksTo(1).rarity(level >= 3 ? Rarity.RARE : Rarity.COMMON));

//        this.baseSpeed = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotBaseSpeed", 0.017f * level, AttributeModifier.Operation.ADDITION);
//        this.upgradeSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeSpeed1", 0.02f * level, AttributeModifier.Operation.ADDITION);
//        this.upgradeSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeSpeed2", 0.067f * level, AttributeModifier.Operation.ADDITION);
        this.baseSpeed = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotBaseSpeed", 0.0218f * level, AttributeModifier.Operation.ADDITION);
        this.upgradeSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeSpeed1", 0.026f * level, AttributeModifier.Operation.ADDITION);
        this.upgradeSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeSpeed2", 0.08f * level, AttributeModifier.Operation.ADDITION);

        this.baseStep = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotBaseStep", 0.5f * level, AttributeModifier.Operation.ADDITION);
        this.upgradeStep1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeStep1", 0.5f * level, AttributeModifier.Operation.ADDITION);
        this.upgradeStep2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "OcelotUpgradeStep2", 1f * level, AttributeModifier.Operation.ADDITION);
        this.level = level;

    }

    public static void ocelotJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntity() instanceof Player player){
            CuriosApi.getCuriosInventory(player).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof OcelotCurioItem ocelotCurioItem) {
                        boolean isSprinting = player.isSprinting();
                        int level = ocelotCurioItem.level;

                        Vec3 motion = player.getDeltaMovement();

                        double forwardBoost = 1 + 0.20D * level;
                        double heightBoost = 0.16D * (1 + 0.2D * (level-1))
                                * (isSprinting ? 1.2f  : 1);

                        if(!isSprinting){
                            heightBoost = Math.min(heightBoost, 0.3);
                        }

                        player.setDeltaMovement(
                                motion.x * forwardBoost,
                                motion.y + heightBoost,
                                motion.z * forwardBoost
                        );
                    }
                }
            }));
        }
    }

    public static boolean eventHandleKineticProtection(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if(event.getEntity() instanceof Player player &&(source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL))){
            CuriosApi.getCuriosInventory(player).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof OcelotCurioItem ocelotCurioItem) {
                        float amount = event.getAmount() - ocelotCurioItem.level * 5;
                        amount /= 1 + ocelotCurioItem.level;

                        if (amount < 1) {
                            event.setCanceled(true);
                        }
                    }
                }
            }));
        }

        return event.isCanceled();
    }

    public static boolean eventHandleKineticProtection(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if(event.getEntity() instanceof Player player &&(source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL))){
            CuriosApi.getCuriosInventory(player).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof OcelotCurioItem ocelotCurioItem) {
                        float amount = event.getAmount() - ocelotCurioItem.level * 5;
                        amount /= 1 + ocelotCurioItem.level;

                        event.setAmount(amount);
                        if (amount < 1) {
                            event.setCanceled(true);
                        }
                    }
                }
            }));
        }

        return event.isCanceled();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.ocelot_ring.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance speedAtt = slotContext.entity().getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance stepAtt = slotContext.entity().getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
        if(speedAtt != null) {
            if (!speedAtt.hasModifier(baseSpeed)){
                speedAtt.addTransientModifier(baseSpeed);
            }
        }
        if(stepAtt != null) {
            if (!stepAtt.hasModifier(baseStep)){
                stepAtt.addTransientModifier(baseStep);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance speedAtt = slotContext.entity().getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance stepAtt = slotContext.entity().getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
        if(speedAtt != null) {
            speedAtt.removeModifier(baseSpeed);
            speedAtt.removeModifier(upgradeSpeed1);
            speedAtt.removeModifier(upgradeSpeed2);
        }
        if(stepAtt != null) {
            stepAtt.removeModifier(baseStep);
            stepAtt.removeModifier(upgradeStep1);
            stepAtt.removeModifier(upgradeStep2);
        }
        slotContext.entity().getPersistentData().remove(SPRINT_TIME_ID);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        AttributeInstance speedAtt = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance stepAtt = slotContext.entity().getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

        if(speedAtt != null && stepAtt != null) {
            int sprint_time = entity.getPersistentData().getInt(SPRINT_TIME_ID);
            if (entity.isSprinting()) {
                if (!speedAtt.hasModifier(upgradeSpeed1)){
                        speedAtt.addTransientModifier(upgradeSpeed1);
                        stepAtt.addTransientModifier(upgradeStep1);
                }

                if (!speedAtt.hasModifier(upgradeSpeed2) && sprint_time > 100){
                    speedAtt.addTransientModifier(upgradeSpeed2);
                    stepAtt.addTransientModifier(upgradeStep2);
                    entity.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 1.0F);
                }

                ++sprint_time;
                entity.getPersistentData().putInt(SPRINT_TIME_ID, sprint_time);
            } else {
                speedAtt.removeModifier(upgradeSpeed1);
                speedAtt.removeModifier(upgradeSpeed2);
                stepAtt.removeModifier(upgradeStep1);
                stepAtt.removeModifier(upgradeStep2);
                entity.getPersistentData().remove(SPRINT_TIME_ID);
            }

        }
    }
}

