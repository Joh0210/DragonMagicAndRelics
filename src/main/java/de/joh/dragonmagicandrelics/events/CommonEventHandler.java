package de.joh.dragonmagicandrelics.events;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChance;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChanceProvider;
import de.joh.dragonmagicandrelics.commands.Commands;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
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
     * @see de.joh.dragonmagicandrelics.armorupgrades.init.ArmorUpgradeJump
     */
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide()){
            if (player.isSprinting()) {
                float boost = ((float)ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP)/10.0f) + ((float)ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY)/4.0f);
                player.push((float)(player.getDeltaMovement().x * boost), boost * 2, (float)(player.getDeltaMovement().z * boost));
                player.hurtMarked = true;
            }
        }
    }

    /**
     * Causes DMArmor effects to be removed when armor is removed.
     */
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event){
        if(event.getEntity() instanceof LivingEntity entity && event.getSlot().getType() == EquipmentSlot.Type.ARMOR){
            Item fromItem = event.getFrom().getItem();
            Item toItem = event.getTo().getItem();

            if(fromItem instanceof DragonMageArmor){
                ((DragonMageArmor)fromItem).onDiscard(event.getFrom(), entity);
            }
            if(toItem instanceof DragonMageArmor){
                ((DragonMageArmor)toItem).onEquip(event.getTo(), entity);
            }

            //apply/remove Dragon Magic Set
            if(fromItem instanceof DragonMageArmor && ((DragonMageArmor)fromItem).wouldSetBeEquipped(entity,fromItem)){
                ((DragonMageArmor)fromItem).removeDragonMagicSetBonus(entity);
            }
            if(toItem instanceof DragonMageArmor && ((DragonMageArmor)toItem).isSetEquipped(entity)){
                ((DragonMageArmor)toItem).applyDragonMagicSetBonus(entity);
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
                && (magic != null && magic.getCastingResource().hasEnoughAbsolute(event.player, CommonConfigs.getElytraManaCostPerTick()))
                && !event.player.hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.FLY_DISABLED.get())) {
            AttributeInstance attributeInstance = event.player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
            if(attributeInstance != null && !attributeInstance.hasModifier(CaelusApi.getInstance().getElytraModifier()))
                attributeInstance.addTransientModifier(CaelusApi.getInstance().getElytraModifier());
        }
    }

    /**
     * This event performs each of the player's OnTick Upgrade
     * @see de.joh.dragonmagicandrelics.effects.beneficial.EffectElytra
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        ArmorUpgradeHelper.applyOnTickUpgrade(event.player);
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
            event.getPlayer().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> secondChance.setSecondChance(event.getOriginal()));
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
