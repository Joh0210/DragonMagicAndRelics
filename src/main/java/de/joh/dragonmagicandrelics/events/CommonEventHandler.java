package de.joh.dragonmagicandrelics.events;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.Commands;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChance;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChanceProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import top.theillusivec4.caelus.api.CaelusApi;

/**
 * These event handlers take care of processing events which are on the server and client. (No damage events)
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

    /**
     * Processing of the jump upgrade.
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (player.isSprinting() && !chest.isEmpty() && chest.getItem() instanceof DragonMageArmor) {
                float boost = ((float)((DragonMageArmor)chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player)/10.0f);
                player.push((float)(event.getEntityLiving().getDeltaMovement().x * boost), boost*2, (float)(event.getEntityLiving().getDeltaMovement().z * boost));
            }
        }
    }

    /**
     * If the player has the Elytra effect, he can fly through this event like with an Elytra.
     * @see de.joh.dragonmagicandrelics.effects.beneficial.EffectElytra
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGlideTick(TickEvent.PlayerTickEvent event){
        IPlayerMagic magic = event.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if(event.player.hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get())
                && (event.player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() != 1 || (magic != null && magic.getCastingResource().hasEnoughAbsolute(event.player, CommonConfigs.getElytraManaCostPerTick())))
                && !event.player.hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.FLY_DISABLED.get())) {
            AttributeInstance attributeInstance = event.player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
            if(attributeInstance != null && !attributeInstance.hasModifier(CaelusApi.getInstance().getElytraModifier()))
                attributeInstance.addTransientModifier(CaelusApi.getInstance().getElytraModifier());
        }
    }

    /**
     * If the player has the elytra effect on level 1 or higher, they get the creative fly and speed boost here.
     * @see de.joh.dragonmagicandrelics.effects.beneficial.EffectElytra
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
        if (!player.hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.FLY_DISABLED.get())
                && player.hasEffect((de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()))
                && player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() >= 1
        ) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                if (player.getAbilities().flying && !player.hasEffect(EffectInit.LEVITATION.get())) {
                    if (!player.hasEffect(EffectInit.MIST_FORM.get()) && !(player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() >= 2)) { //No mana consumption in mist form
                        m.getCastingResource().consume(player, CommonConfigs.getFlyManaCostPerTick());
                    }
                }

                if (!m.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.getFlyManaCostPerTick())) {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                } else {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                    if (!player.isCreative() && !player.isSpectator()) {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, CommonConfigs.getFlySpeedPerLevel());
                    } else {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                    }
                }
            });

            if(player.isFallFlying()) {
                IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                if (player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() != 1 || (magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.getElytraManaCostPerTick()))) {
                    Vec3 look = player.getLookAngle();
                    Vec3 pos;
                    float maxLength;
                    double lookScale;
                    Vec3 scaled_look;
                    if (!player.isShiftKeyDown()) {
                        if(!(player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() >= 2)){
                            magic.getCastingResource().consume(player, CommonConfigs.getElytraManaCostPerTick());
                        }
                        pos = player.getDeltaMovement();
                        maxLength = 1.75F;
                        lookScale = 0.06D;
                        scaled_look = look.scale(lookScale);
                        pos = pos.add(scaled_look);
                        if (pos.length() > (double)maxLength) {
                            pos = pos.scale((double)maxLength / pos.length());
                        }
                        player.setDeltaMovement(pos);
                    } else {
                        if(!(player.getEffect(de.joh.dragonmagicandrelics.effects.EffectInit.ELYTRA.get()).getAmplifier() >= 2)){
                            magic.getCastingResource().consume(player, CommonConfigs.getElytraManaCostPerTick() /2.0f);
                        }
                        pos = player.getDeltaMovement();
                        maxLength = 0.1F;
                        lookScale = -0.01D;
                        scaled_look = look.scale(lookScale);
                        pos = pos.add(scaled_look);
                        if (pos.length() < (double)maxLength) {
                            pos = pos.scale((double)maxLength / pos.length());
                        }
                        player.setDeltaMovement(pos);
                    }

                    if (player.level.isClientSide) {
                        pos = player.position().add(look.scale(3.0D));
                        for(int i = 0; i < 5; ++i) {
                            player.level.addParticle((new MAParticleType(ParticleInit.AIR_VELOCITY.get())).setScale(0.2F).setColor(10, 10, 10), pos.x - 0.5D + Math.random(), pos.y - 0.5D + Math.random(), pos.z - 0.5D + Math.random(), -look.x, -look.y, -look.z);
                        }
                    }
                } else {
                    player.stopFallFlying();
                }
            }
        }
    }

    /**
     * Registration of Commands
     * @see Commands
     */
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new Commands(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    /**
     * Saving the player's position for the Phoenix Ritual when they die
     * @see de.joh.dragonmagicandrelics.rituals.contexts.PhoenixRitual
     * @see PlayerSecondChance
     * @see PlayerSecondChanceProvider
     */
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getPlayer().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> {
                secondChance.setSecondChance(event.getOriginal());
            });
        }
    }

    /**
     * Adding capabilities to players
     */
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).isPresent()) {
                event.addCapability(RLoc.create("properties"), new PlayerSecondChanceProvider());
            }
        }
    }

    /**
     * Registration of the Capabilities
     */
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSecondChance.class);
    }
}
