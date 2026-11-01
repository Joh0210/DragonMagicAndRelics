package de.joh.dmnr.common.dragonmagic;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemSpell;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Dragon Magic of the Amulet of the Battlemage of Mana and Artifice, which increases the damage of spells cast with a
 * sword in the other hand by 30% and the melee damage with a spell in the other hand by up to 5, the more the lower the
 * mana of its wearer is.
 * <br> Dragon Magic: both bonuses are granted once more, multiplied by
 * {@link CommonConfig#BATTLEMAGE_AMULET_DM_BONUS} (100% by default, i.e. +60% spell damage and up to +10 melee damage).
 * Mana and Artifice applies the bonuses inside its own code, so this class repeats them with the same conditions.
 * @author Joh0210
 */
public class BattlemageAmuletDragonMagic extends ExternalDragonMagic {
    private static final float SPELL_DAMAGE_BONUS = 0.3f;
    private static final float MELEE_DAMAGE_BONUS = 5.0f;

    public BattlemageAmuletDragonMagic() {
        super(new ResourceLocation("mna", "battlemage_amulet"));
    }

    /**
     * Spell adjuster, registered with Mana and Artifice like its own one for the amulet (tooltip and casting only).
     */
    public static void adjustSpell(SpellAdjustingContext context) {
        LivingEntity caster = context.caster;
        if (caster == null || !isEquipped(caster) || !ExternalDragonMagicInit.BATTLEMAGE_AMULET.hasDragonMagic(caster)) {
            return;
        }

        Item mainHand = caster.getMainHandItem().getItem();
        Item offHand = caster.getOffhandItem().getItem();
        boolean swordInMainHand = MATags.isItemIn(mainHand, MATags.Items.BATTLEMAGE_AMULET_SWORDS) && (context.curioCast || offHand instanceof ICanContainSpell);
        boolean swordInOffHand = MATags.isItemIn(offHand, MATags.Items.BATTLEMAGE_AMULET_SWORDS) && mainHand instanceof ICanContainSpell;
        if (swordInMainHand || swordInOffHand) {
            float bonus = SPELL_DAMAGE_BONUS * bonusFactor();
            context.spell.getComponents().forEach(part -> part.setMultiplier(Attribute.DAMAGE, part.getMultiplier(Attribute.DAMAGE) + bonus));
        }
    }

    public static void eventHandleMeleeDamage(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)
                || source.is(DamageTypeTags.WITCH_RESISTANT_TO)
                || source.is(DamageTypeTags.IS_PROJECTILE)
                || source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.IS_EXPLOSION)
                || source.is(DamageTypeTags.IS_FALL)
                || player.distanceTo(event.getEntity()) > GeneralConfigValues.MeleeDistance) {
            return;
        }

        Item mainHand = player.getMainHandItem().getItem();
        Item offHand = player.getOffhandItem().getItem();
        boolean swordInMainHand = isMeleeWeapon(mainHand) && offHand instanceof ItemSpell;
        boolean swordInOffHand = isMeleeWeapon(offHand) && mainHand instanceof ItemSpell;
        if ((swordInMainHand || swordInOffHand)
                && isEquipped(player)
                && ExternalDragonMagicInit.BATTLEMAGE_AMULET.hasDragonMagic(player)) {
            player.getCapability(PlayerMagicProvider.MAGIC).map(IPlayerMagic::getCastingResource).ifPresent(resource -> {
                float missingMana = 1 - resource.getAmount() / resource.getMaxAmount();
                event.setAmount(event.getAmount() + MELEE_DAMAGE_BONUS * missingMana * bonusFactor());
            });
        }
    }

    private static boolean isMeleeWeapon(Item item) {
        return item instanceof SwordItem || item == ItemInit.ASTRO_BLADE.get() || item == ItemInit.BONE_SCYTHE.get();
    }

    private static boolean isEquipped(LivingEntity entity) {
        Item amulet = ItemInit.BATTLEMAGE_AMULET.get();
        return CuriosApi.getCuriosInventory(entity).map(curios -> curios.findFirstCurio(amulet).isPresent()).orElse(false);
    }

    private static float bonusFactor() {
        return CommonConfig.BATTLEMAGE_AMULET_DM_BONUS.get() / 100f;
    }
}
