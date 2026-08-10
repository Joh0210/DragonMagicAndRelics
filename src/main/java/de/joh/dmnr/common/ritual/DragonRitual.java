package de.joh.dmnr.common.ritual;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.rituals.effects.RitualEffectCreateEssence;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * This ritual upgrades faction armor to Dragon Mage Armor.
 * Depending on the faction armor, different initial upgrades will be installed.
 * The armor, conditions, and initial upgrades can be customized by handlers.
 * @see DragonMageArmorItem
 * @author Joh0210
 */
public class DragonRitual extends RitualEffectCreateEssence {
    public DragonRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Player player = context.getCaster();
        final boolean[] isLevel75 = {false};

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> isLevel75[0] = 75 <= m.getMagicLevel());

        return isLevel75[0] ? null : Component.translatable("dmnr.ritual.output.dragonmagearmorritual.to.low.level.error");
    }

    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.FIRE;
    }

    public boolean spawnRitualParticles(IRitualContext context) {
        Vec3 center = new Vec3((double)context.getCenter().getX() + (double)0.5F, (double)context.getCenter().getY() + 0.1, (double)context.getCenter().getZ() + (double)0.5F);
        float radius = (float)context.getRecipe().getLowerBound();

        for (float i = 0.0F; i < 360.0F; i += 10.0F) {
            double angleR = Math.toRadians(i);
            double offsetX = Math.cos(angleR) * radius;
            double offsetZ = Math.sin(angleR) * radius;
            context.getLevel().addParticle(ParticleTypes.DRAGON_BREATH, center.x + offsetX, center.y, center.z + offsetZ, 0, 0.001, 0);
        }

        double time = (double)context.getLevel().getGameTime() * 0.2;
        for (int j = 0; j < 2; j++) {
            double angle = time + (j * Math.PI);
            double x = center.x + Math.cos(angle) * (radius * 0.5);
            double z = center.z + Math.sin(angle) * (radius * 0.5);
            double y = center.y + (Math.sin(time * 0.5) + 1.0) * 1.5;

            context.getLevel().addParticle(ParticleTypes.WITCH, x, y, z, 0, 0.05, 0);
        }

        return true;
    }

    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.DRAGON_TOTEM_EMPTY.get());
    }
}
