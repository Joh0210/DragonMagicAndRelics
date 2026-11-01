package de.joh.dmnr.common.dragonmagic;

import com.mna.api.events.SpellCastEvent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.items.ItemInit;
import com.mna.spells.shapes.ShapeProjectile;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Dragon Magic of the Prismatic Amulet of Mana and Artifice, which makes the Projectile shape fire three projectiles
 * in a fan instead of one, for 1 charge.
 * <br> Dragon Magic: the fan gets {@link CommonConfig#PRISMATIC_AMULET_DM_EXTRA_PAIRS} more projectiles on each side
 * (five projectiles by default), without further charge.
 * <br> Mana and Artifice spawns the projectiles in the private method {@code ShapeProjectile.SpawnProjectile}, with the
 * position in the fan as index (-1, 0, 1). There is no hook for it, so it is called by reflection with the further
 * indices (-2, 2, ...). If the method no longer exists after an update of Mana and Artifice, a warning is logged once
 * and only this Dragon Magic is lost.
 * @author Joh0210
 */
public class PrismaticAmuletDragonMagic extends ExternalDragonMagic {
    private static boolean spawnProjectileResolved = false;
    @Nullable
    private static Method spawnProjectile;

    public PrismaticAmuletDragonMagic() {
        super(new ResourceLocation("mna", "projectile_amulet"));
    }

    /**
     * {@link SpellCastEvent} is fired right before Mana and Artifice spawns the projectiles of the spell.
     */
    public static void eventHandleExtraProjectiles(SpellCastEvent event) {
        SpellSource source = event.getSource();
        ISpellDefinition spell = event.getSpell();
        IModifiedSpellPart<Shape> shape = spell.getShape();
        Player player = source.isPlayerCaster() ? source.getPlayer() : null;
        int pairs = CommonConfig.PRISMATIC_AMULET_DM_EXTRA_PAIRS.get();
        if (pairs <= 0
                || shape == null
                || shape.getPart() != Shapes.PROJECTILE
                || player == null
                || player.level().isClientSide()
                // Same condition as Mana and Artifice, which consumes the charge right after this event
                || !ItemInit.PROJECTILE_AMULET.get().isEquippedAndHasMana(player, 1.0f, false)
                || !ExternalDragonMagicInit.PRISMATIC_AMULET.hasDragonMagic(player)) {
            return;
        }

        Method method = getSpawnProjectile();
        if (method == null) {
            return;
        }

        Level level = player.level();
        try {
            for (int i = 2; i <= pairs + 1; i++) {
                method.invoke(shape.getPart(), source, level, shape, spell, (float) -i, null);
                method.invoke(shape.getPart(), source, level, shape, spell, (float) i, null);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            DragonMagicAndRelics.LOGGER.warn("Dragon Magic of the Prismatic Amulet failed to spawn projectiles, it is disabled", e);
            spawnProjectile = null;
        }
    }

    @Nullable
    private static Method getSpawnProjectile() {
        if (!spawnProjectileResolved) {
            spawnProjectileResolved = true;
            try {
                spawnProjectile = ShapeProjectile.class.getDeclaredMethod("SpawnProjectile", SpellSource.class, Level.class, IModifiedSpellPart.class, ISpellDefinition.class, float.class, SpellTarget.class);
                spawnProjectile.setAccessible(true);
            } catch (ReflectiveOperationException | RuntimeException e) {
                DragonMagicAndRelics.LOGGER.warn("ShapeProjectile.SpawnProjectile of Mana and Artifice not found, the Dragon Magic of the Prismatic Amulet is disabled", e);
            }
        }
        return spawnProjectile;
    }
}
