package de.joh.dmnr.common.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.IOnEquippedArmorUpgrade;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleBurningFrenzyS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * A combined and stronger version of Speed and Jump.
 * @see SpeedArmorUpgrade
 * @author Joh0210
 */
public class BurningFrenzyArmorUpgrade extends OnTickArmorUpgrade implements IOnEquippedArmorUpgrade {
    public static final AttributeModifier runSpeed_1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_1", 0.05D,AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier runSpeed_2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_2", 0.1D,AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier runSpeed_3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_3", 0.1D,AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier runSpeed_4 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_4", 0.1D,AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier runSpeed_5 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_5", 0.1D,AttributeModifier.Operation.ADDITION);


    private static final AttributeModifier stepMod1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_step_bonus_1", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_step_bonus_2", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_step_bonus_3", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod4 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_step_bonus_4", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod5 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_step_bonus_5", 0.5f, AttributeModifier.Operation.ADDITION);

    public BurningFrenzyArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 1, upgradeSealItem, false, true, upgradeCost);
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic.getCastingResource().hasEnoughAbsolute(player, 0.07F)) {
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_1)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_1);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod1);
                DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_2", 60, player, this::addDelayedRunSpeed));
                DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_3", 120, player, this::addDelayedRunSpeed));
                if(level > 1) {
                    DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_4", 180, player, this::addDelayedRunSpeed));
                    DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_5", 240, player, this::addDelayedRunSpeed));
                }
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 0.8F, 0);
            }

            magic.getCastingResource().consume(player, 0.07F);
        }
        else  {
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_1);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_3);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_4);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed_5);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod1);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod2);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod3);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod4);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod5);
        }

        //todo: Particles --> isClientSide is always false!
//        if (world.isClientSide && showParticles) {
//            Vec3 motion = player.getDeltaMovement();
//            Vec3 look = player.getForward().cross(new Vec3(0.0D, 1.0D, 0.0D));
//            float offset = (float)(Math.random() * 0.2D);
//            float yOffset = 0.2F;
//            look = look.scale(offset);
//
//            for(int i = 0; i < 5; ++i) {
//                world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getX() + look.x + Math.random() * motion.x * 2.0D, player.getY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getZ() + look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
//                world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getX() - look.x + Math.random() * motion.x * 2.0D, player.getY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getZ() - look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
//            }
//        }
    }

    public void addDelayedRunSpeed(String identifier, Player player) {
        if (player.isSprinting()) {
            if (identifier.equals(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_2") && !player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_2)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_2);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod2);
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 1.0F, 0);
            } else if (identifier.equals(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_3") && !player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_3)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_3);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod3);
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 1.2F, 0);
            } else if (identifier.equals(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_4") && !player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_4)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_4);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod4);
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 1.4F, 0);
            } else if (identifier.equals(DragonMagicAndRelics.MOD_ID + "_armor_burning_frenzy_5") && !player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed_5)) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed_5);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod5);
                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 1.6F, 0);
            }
        }
    }

    @Override
    public void onEquip(Player player, int level) {
        if(!this.hasStrongerAlternative() && level >= 1 && player instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleBurningFrenzyS2CPacket(true), (ServerPlayer) player);
        }
    }

    @Override
    public void onRemove(Player player) {
        if(!this.hasStrongerAlternative() && player instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleBurningFrenzyS2CPacket(false), (ServerPlayer) player);
        }
    }

    @Override
    public ArmorUpgrade getArmorUpgrade() {
        return this;
    }
}
