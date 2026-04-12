package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import com.mna.tools.ProjectileHelper;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * This Item Reflects a couple of projectiles every few seconds
 * @author Joh0210
 */
public class ProjectileReflectionRingItem extends TieredItem implements ICurioItem, IFactionSpecific {
    private final static String PROJECTILE_CHARGE_ID = DragonMagicAndRelics.MOD_ID + "ProjectileReflectionRingCharge";

    // todo Upgrade: multiplies the total speed of the player
    public ProjectileReflectionRingItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.projectile_reflection_ring.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    /**
     * This function is called by the Dragon Mage Armor every tick (if the upgrade is installed) to recharge the reflections
     */
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player) {

            int[] reflections = getReflectCharges(player);

            for(int i = 0; i < reflections.length; ++i) {
                if (reflections[i] > 0) {
                    reflections[i]--;
                }
            }

            updateReflectCharges(player, reflections);
        }
    }

    /**
     * If yes, start the cooldown of the reflection.
     * @see DamageEventHandler
     * @param player wearer of the armor
     * @return Can the player reflect a projectile?
     */
    public static boolean consumeReflectCharge(Player player) {
        int[] reflections = getReflectCharges(player);

        for(int i = 0; i < reflections.length; ++i) {
            if (reflections[i] <= 0) {
                reflections[i] = CommonConfig.PROJECTILE_REFLECTION_TICKS_PER_CHARGE.get();
                updateReflectCharges(player, reflections);
                return true;
            }
        }

        return false;
    }

    private static int[] getReflectCharges(Player player) {
        int[] reflections;
        if (!player.getPersistentData().contains(PROJECTILE_CHARGE_ID)) {
            reflections = new int[3];
        } else {
            reflections = player.getPersistentData().getIntArray(PROJECTILE_CHARGE_ID);
        }

        if (reflections.length != 3) {
            reflections = new int[3];
        }
        return reflections;
    }

    public static boolean tryReflect(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if(event.getEntity() instanceof Player player){
            if (!player.level().isClientSide) {
                //Projectile Reflection
                if(source.getDirectEntity() instanceof Projectile &&  CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.PROJECTILE_REFLECTION_RING.get()).isPresent() && ProjectileReflectionRingItem.consumeReflectCharge(player)){
                    event.setCanceled(true);
                    ProjectileHelper.ReflectProjectile(player, (Projectile)source.getDirectEntity(), true, 10.0F);
                    return true;
                }
            }

        }
        return false;
    }
    
    private static void updateReflectCharges(Player player, int[] reflections) {
        player.getPersistentData().putIntArray(PROJECTILE_CHARGE_ID, reflections);
    }
}
