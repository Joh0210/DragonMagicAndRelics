package de.joh.dmnr.common.item;

import com.mna.KeybindInit;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.armor.ISetItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.gui.NamedDragonMageArmorDye;
import de.joh.tnl.api.event.DragonMagicChangeEvent;
import de.joh.tnl.capabilities.CurioBoostProvider;
import de.joh.dmnr.client.item.armor.DragonMageArmorRenderer;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import de.joh.dmnr.common.util.FactionArmorColors;
import de.joh.dmnr.common.util.RLoc;
import de.joh.tnl.networking.ModMessages;
import de.joh.tnl.networking.packet.ToggleCurioBoostEnabledS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * <br>The armor has two colors: the primary color tints the ornaments (base texture), the secondary color the plates
 * (overlay texture). Both are chosen in the dyeing GUI, which opens when the armor is used while holding the M&A GUI
 * modifier key. Armor obtained through the Totem of the Dragon starts in the colors of the faction of the player.
 * <br>In the same GUI the chestplate can get wings ({@link WingDesign}), which have two colors of their own. Like the
 * vanilla Elytra, the wings can show the cape of the wearer instead.
 * @see FactionArmorColors
 * @see DamageEventHandler
 * @see de.joh.dmnr.client.gui.GuiDragonMageArmorDye
 * @author Joh0210
 */
public class DragonMageArmorItem extends ArmorItem implements IForgeItem, ISetItem, GeoItem, IWingItem {
    /**
     * Color of the ornaments if the armor has not been dyed.
     */
    public static final int DEFAULT_PRIMARY_COLOR = 0xFFB736;
    /**
     * Color of the plates if the armor has not been dyed. The overlay texture is the original dark texture stretched to
     * the full brightness range, so this color reproduces it exactly and light colors can still be reached.
     */
    public static final int DEFAULT_SECONDARY_COLOR = 0x212121;
    private static final String DISPLAY_TAG = "display";
    /**
     * Same key as vanilla dyeable armor, so armor that was dyed before the secondary color existed keeps its color.
     */
    private static final String PRIMARY_COLOR_TAG = "color";
    private static final String SECONDARY_COLOR_TAG = "secondary_color";
    private static final String WING_DESIGN_TAG = "wing_design";
    private static final String WING_PRIMARY_COLOR_TAG = "wing_color";
    private static final String WING_SECONDARY_COLOR_TAG = "wing_secondary_color";
    private static final String WING_CAPE_TAG = "wing_cape";

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final String curioType = "curio";
    private final UUID curiosID;
    private final AttributeModifier curiosMod;

    private final ResourceLocation dragonMageArmorSetBonus;

    public DragonMageArmorItem(ArmorItem.Type type) {
        super(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, type, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
        this.dragonMageArmorSetBonus = RLoc.create("dragon_armor_set_bonus");

        String id = DragonMagicAndRelics.MOD_ID + "_dm_armor" + type.getName();
        this.curiosID = UUID.nameUUIDFromBytes(id.getBytes());
        this.curiosMod = new AttributeModifier(curiosID, id, 1, AttributeModifier.Operation.ADDITION);
    }

    /**
     * The armor is rendered by GeckoLib, but vanilla's armor layer still requests the default
     * "dragon_mage_layer_N" texture, which does not exist. Point it to a texture that does.
     */
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return RLoc.create("textures/models/armor/dragon_mage_armor_texture_base.png").toString();
    }

    /**
     * @return the RGB color of the ornaments of this piece of armor
     */
    public static int getPrimaryColor(ItemStack stack) {
        return getColor(stack, PRIMARY_COLOR_TAG, DEFAULT_PRIMARY_COLOR);
    }

    /**
     * @return the RGB color of the plates of this piece of armor
     */
    public static int getSecondaryColor(ItemStack stack) {
        return getColor(stack, SECONDARY_COLOR_TAG, DEFAULT_SECONDARY_COLOR);
    }

    /**
     * Dyes this piece of armor. Default colors are not stored, so undyed armor carries no color tags.
     * @param primaryColor RGB color of the ornaments
     * @param secondaryColor RGB color of the plates
     */
    public static void setColors(ItemStack stack, int primaryColor, int secondaryColor) {
        setColor(stack, PRIMARY_COLOR_TAG, primaryColor & 0xFFFFFF, DEFAULT_PRIMARY_COLOR);
        setColor(stack, SECONDARY_COLOR_TAG, secondaryColor & 0xFFFFFF, DEFAULT_SECONDARY_COLOR);
    }

    /**
     * @return the wings of this piece of armor, only the chestplate can have wings
     */
    public static WingDesign getWingDesign(ItemStack stack) {
        CompoundTag displayTag = stack.getTagElement(DISPLAY_TAG);
        return displayTag != null ? WingDesign.byName(displayTag.getString(WING_DESIGN_TAG)) : WingDesign.NONE;
    }

    /**
     * @return the primary RGB color of the wings (membrane, leaves or tips), the color of the ornaments if none is stored
     */
    public static int getWingPrimaryColor(ItemStack stack) {
        return getColor(stack, WING_PRIMARY_COLOR_TAG, getPrimaryColor(stack));
    }

    /**
     * @return the secondary RGB color of the wings (bones, branches or the part near the back), the color of the plates
     * if none is stored
     */
    public static int getWingSecondaryColor(ItemStack stack) {
        return getColor(stack, WING_SECONDARY_COLOR_TAG, getSecondaryColor(stack));
    }

    /**
     * @return whether a wearer with a visible cape shows it on the wings of this piece of armor instead of the design
     * @see IWingItem#usesCape(ItemStack)
     */
    public static boolean isWingCapeEnabled(ItemStack stack) {
        CompoundTag displayTag = stack.getTagElement(DISPLAY_TAG);
        return displayTag != null && displayTag.getBoolean(WING_CAPE_TAG);
    }

    /**
     * Gives a chestplate wings, other pieces of armor are not changed. Without wings (no design and no cape) no wing
     * tags are stored.
     * @param primaryColor RGB color of the membrane, leaves or tips
     * @param secondaryColor RGB color of the bones, branches or the part near the back
     * @param useCape whether a wearer with a visible cape shows it on the wings instead of the design
     */
    public static void setWings(ItemStack stack, WingDesign design, int primaryColor, int secondaryColor, boolean useCape) {
        if (!(stack.getItem() instanceof DragonMageArmorItem armor) || armor.getType() != Type.CHESTPLATE) {
            return;
        }

        if (design != WingDesign.NONE || useCape) {
            CompoundTag displayTag = stack.getOrCreateTagElement(DISPLAY_TAG);
            displayTag.putString(WING_DESIGN_TAG, design.getName());
            displayTag.putInt(WING_PRIMARY_COLOR_TAG, primaryColor & 0xFFFFFF);
            displayTag.putInt(WING_SECONDARY_COLOR_TAG, secondaryColor & 0xFFFFFF);
            if (useCape) {
                displayTag.putBoolean(WING_CAPE_TAG, true);
            } else {
                displayTag.remove(WING_CAPE_TAG);
            }
            return;
        }

        CompoundTag displayTag = stack.getTagElement(DISPLAY_TAG);
        if (displayTag != null) {
            displayTag.remove(WING_DESIGN_TAG);
            displayTag.remove(WING_PRIMARY_COLOR_TAG);
            displayTag.remove(WING_SECONDARY_COLOR_TAG);
            displayTag.remove(WING_CAPE_TAG);
            if (displayTag.isEmpty()) {
                stack.removeTagKey(DISPLAY_TAG);
            }
        }
    }

    @Override
    public boolean usesCape(ItemStack stack) {
        return this.getType() == Type.CHESTPLATE && isWingCapeEnabled(stack);
    }

    /**
     * The wings of the chestplate in its wing colors.
     * @see WingDesign#getLayers(int, int)
     */
    @Override
    public @NotNull List<WingLayer> getWingLayers(ItemStack stack) {
        if (this.getType() != Type.CHESTPLATE) {
            return List.of();
        }
        return getWingDesign(stack).getLayers(getWingPrimaryColor(stack), getWingSecondaryColor(stack));
    }

    private static int getColor(ItemStack stack, String key, int defaultColor) {
        CompoundTag displayTag = stack.getTagElement(DISPLAY_TAG);
        return displayTag != null && displayTag.contains(key, Tag.TAG_ANY_NUMERIC) ? displayTag.getInt(key) & 0xFFFFFF : defaultColor;
    }

    private static void setColor(ItemStack stack, String key, int color, int defaultColor) {
        if (color != defaultColor) {
            stack.getOrCreateTagElement(DISPLAY_TAG).putInt(key, color);
            return;
        }

        CompoundTag displayTag = stack.getTagElement(DISPLAY_TAG);
        if (displayTag != null) {
            displayTag.remove(key);
            if (displayTag.isEmpty()) {
                stack.removeTagKey(DISPLAY_TAG);
            }
        }
    }

    /**
     * Collects the pieces of Dragon Mage Armor the dyeing GUI works on: the piece in the given hand and every piece the
     * player wears. The held piece takes the place of a worn piece of the same type.
     * @param hand hand holding the piece the GUI was opened with
     * @return the pieces by armor type, ordered helmet, chestplate, leggings, boots
     */
    public static Map<ArmorItem.Type, ItemStack> getDyeTargets(Player player, InteractionHand hand) {
        Map<ArmorItem.Type, ItemStack> targets = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            ItemStack worn = player.getItemBySlot(type.getSlot());
            if (worn.getItem() instanceof DragonMageArmorItem armor && armor.getType() == type) {
                targets.put(type, worn);
            }
        }

        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() instanceof DragonMageArmorItem armor) {
            targets.put(armor.getType(), held);
        }
        return targets;
    }

    /**
     * Opens the dyeing GUI if the M&A GUI modifier key is held, otherwise equips the armor as usual.
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        boolean modifierPressed = player.getCapability(PlayerMagicProvider.MAGIC).map(IPlayerMagic::isModifierPressed).orElse(false);
        if (modifierPressed) {
            if (player instanceof ServerPlayer serverPlayer) {
                NamedDragonMageArmorDye menuProvider = new NamedDragonMageArmorDye(hand, FactionArmorColors.getColors(serverPlayer));
                NetworkHooks.openScreen(serverPlayer, menuProvider, menuProvider::writeOpenData);
            }
            return InteractionResultHolder.sidedSuccess(held, level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    public void onEquip(ItemStack itemStack, LivingEntity entity) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(
                iCurioStacksHandler -> iCurioStacksHandler.addTransientModifier(curiosMod));
    }

    public void onDiscard(ItemStack itemStack, LivingEntity entity) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(iCurioStacksHandler -> iCurioStacksHandler.removeModifier(curiosID));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dmnr:dragon_armor_bonus").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        this.addSetTooltip(tooltip);
        Component modifierKey = ((KeyMapping) KeybindInit.InventoryItemOpen.get()).getTranslatedKeyMessage();
        tooltip.add(Component.translatable("item.dmnr.dragon_mage_armor.dye", modifierKey).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    /**
     * Armor does not break
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return dragonMageArmorSetBonus;
    }

    @Override
    public int itemsForSetBonus() {
        return 4;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DragonMageArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new DragonMageArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void applySetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        if (entity instanceof Player player) {
            if(entity instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new ToggleCurioBoostEnabledS2CPacket(true), (ServerPlayer) entity);
                player.getCapability(CurioBoostProvider.CURIO_BOOST).ifPresent(magic -> magic.setEnabled(true));
            }

            DragonMagicChangeEvent event = new DragonMagicChangeEvent(player, true);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Override
    public void removeSetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        if (entity instanceof Player player) {
            if(entity instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new ToggleCurioBoostEnabledS2CPacket(false), (ServerPlayer) entity);
                player.getCapability(CurioBoostProvider.CURIO_BOOST).ifPresent(magic -> magic.setEnabled(false));
            }

            DragonMagicChangeEvent event = new DragonMagicChangeEvent(player, false);
            MinecraftForge.EVENT_BUS.post(event);
        }


    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return false;
    }

    /**
     * Wings the chestplate can have.
     * <br> Dragon and wild wings have a base texture (membrane or leaves) tinted with the primary wing color and an
     * overlay texture (bones or branches) tinted with the secondary one, both gray.
     * <br> Elytra wings use the texture of the vanilla Elytra (or of the active resource pack) as a gradient: the
     * darkest pixels near the back get the secondary color, the brightest at the tips the primary color.
     * <br> If the wings show the cape of the wearer, the cape fills the membrane or leaves of dragon and wild wings,
     * and replaces elytra wings completely.
     */
    public enum WingDesign {
        NONE("none"),
        DRAGON("dragon"),
        WILD("wild"),
        ELYTRA("elytra");

        private static final ResourceLocation VANILLA_ELYTRA = new ResourceLocation("textures/entity/elytra.png");

        private final String name;
        private final ResourceLocation baseTexture;
        private final ResourceLocation overlayTexture;

        WingDesign(String name) {
            this.name = name;
            this.baseTexture = RLoc.create("textures/models/armor/dragon_mage_wings_" + name + "_base.png");
            this.overlayTexture = RLoc.create("textures/models/armor/dragon_mage_wings_" + name + "_overlay.png");
        }

        /**
         * @return the name stored on the chestplate and used in translation keys
         */
        public String getName() {
            return name;
        }

        /**
         * @param primaryColor RGB color of the membrane, leaves or tips
         * @param secondaryColor RGB color of the bones, branches or the part near the back
         * @return the layers of these wings, empty for {@link #NONE}
         */
        public List<WingLayer> getLayers(int primaryColor, int secondaryColor) {
            return switch (this) {
                case NONE -> List.of();
                case ELYTRA -> List.of(WingLayer.gradient(VANILLA_ELYTRA, primaryColor, secondaryColor));
                default -> List.of(new WingLayer(baseTexture, primaryColor).withCapeFill(), new WingLayer(overlayTexture, secondaryColor));
            };
        }

        /**
         * @return the design with this name, {@link #NONE} if there is none
         */
        public static WingDesign byName(String name) {
            for (WingDesign design : values()) {
                if (design.name.equals(name)) {
                    return design;
                }
            }
            return NONE;
        }
    }
}
