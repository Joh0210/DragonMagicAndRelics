package de.joh.dmnr.events;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.events.SpellCastEvent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.Shape;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.capabilities.secondchance.PlayerSecondChance;
import de.joh.dmnr.capabilities.secondchance.PlayerSecondChanceProvider;
import de.joh.dmnr.commands.Commands;
import de.joh.dmnr.config.CommonConfigs;
import de.joh.dmnr.item.ItemInit;
import de.joh.dmnr.item.items.CurseProtectionAmulet;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleBurningFrenzyS2CPacket;
import de.joh.dmnr.networking.packet.ToggleMajorFireResS2CPacket;
import de.joh.dmnr.utils.RLoc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
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
     * @see de.joh.dmnr.armorupgrades.init.ArmorUpgradeJump
     */
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof Player player && !player.getLevel().isClientSide()){
            if (player.isSprinting() && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) >= 1) {
                float boost = ((float)ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP)/10.0f) + ((float)ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY)/4.0f);
                player.push((float)(player.getDeltaMovement().x * boost * 1.5F), boost * 2, (float)(player.getDeltaMovement().z * boost * 1.5F));
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
     * @see de.joh.dmnr.effects.beneficial.EffectElytra
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGlideTick(TickEvent.PlayerTickEvent event){
        IPlayerMagic magic = event.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if(event.player.hasEffect(de.joh.dmnr.effects.EffectInit.ELYTRA.get())
                && (magic != null && magic.getCastingResource().hasEnoughAbsolute(event.player, CommonConfigs.getElytraManaCostPerTick()))
                && !event.player.hasEffect(de.joh.dmnr.effects.EffectInit.FLY_DISABLED.get())) {
            AttributeInstance attributeInstance = event.player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
            if(attributeInstance != null && !attributeInstance.hasModifier(CaelusApi.getInstance().getElytraModifier()))
                attributeInstance.addTransientModifier(CaelusApi.getInstance().getElytraModifier());
        }
    }

    /**
     * This event performs each of the player's OnTick Upgrade
     * @see de.joh.dmnr.effects.beneficial.EffectElytra
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
     * @see de.joh.dmnr.rituals.effects.PhoenixRitual
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

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket((ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE)) >= 1), player);
                ModMessages.sendToPlayer(new ToggleBurningFrenzyS2CPacket((ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY)) >= 1), player);
            }
        }
    }

    /**
     * Processing of {@link CurseProtectionAmulet}
     */
    @SubscribeEvent
    public static void onPotionAdded(PotionEvent.PotionApplicableEvent event){
        if(event.getPotionEffect().getDuration() < 6000
                && event.getPotionEffect().getDuration() > 0
                && event.getPotionEffect().getEffect().getCategory() == MobEffectCategory.HARMFUL
                && event.getPotionEffect().getEffect().getCurativeItems().stream().anyMatch(s -> s.getItem() == Items.MILK_BUCKET)
        ){
            int amount = (int) Math.min(event.getPotionEffect().getDuration() * (event.getPotionEffect().getAmplifier() +1) / 20.0f, 150.0f);
            if(((CurseProtectionAmulet) ItemInit.CURSE_PROTECTION_AMULET.get()).isEquippedAndHasMana(event.getEntityLiving(), amount, true)){
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * Processing of {@link ArmorUpgradeInit#SORCERERS_PRIDE}
     */
    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event){
        Player caster = event.getCaster();
        IModifiedSpellPart<Shape> shape = event.getSpell().getShape();

        int level = ArmorUpgradeHelper.getUpgradeLevel(caster, ArmorUpgradeInit.SORCERERS_PRIDE);
        if(level > 0 && shape != null){
            shape.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.MAGNITUDE)
                    .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + Math.round(level * 0.5f)));
            shape.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.DAMAGE)
                    .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + level * 3));
            shape.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.DURATION)
                    .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) * (1 + level * 0.3f)));

            event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.MAGNITUDE)
                    .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + Math.round(level * 0.5f))));
            event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.DAMAGE)
                    .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + level * 3)));
            event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                    .filter(attribute -> attribute == Attribute.DURATION)
                    .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) * (1 + level * 0.3f))));
        }
    }
}
