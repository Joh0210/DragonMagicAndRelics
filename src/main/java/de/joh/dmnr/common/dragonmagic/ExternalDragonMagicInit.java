package de.joh.dmnr.common.dragonmagic;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.adjusters.DefaultAdjusters;
import de.joh.tnl.api.item.DragonMagicRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

/**
 * Dragon Magic of the items of other mods.
 * @author Joh0210
 */
public class ExternalDragonMagicInit {
    public static final WardingAmuletDragonMagic WARDING_AMULET = register(new WardingAmuletDragonMagic());
    public static final FortifyingAmuletDragonMagic FORTIFYING_AMULET = register(new FortifyingAmuletDragonMagic());
    public static final ArcaneCrownDragonMagic ARCANE_CROWN = register(new ArcaneCrownDragonMagic());
    public static final RingOfSilkDragonMagic RING_OF_SILK = register(new RingOfSilkDragonMagic());
    public static final RingOfBrittleBoneDragonMagic RING_OF_BRITTLE_BONE = register(new RingOfBrittleBoneDragonMagic());
    public static final BattlemageAmuletDragonMagic BATTLEMAGE_AMULET = register(new BattlemageAmuletDragonMagic());
    public static final EldritchOrbDragonMagic ELDRITCH_ORB = register(new EldritchOrbDragonMagic());
    public static final PrismaticAmuletDragonMagic PRISMATIC_AMULET = register(new PrismaticAmuletDragonMagic());
    public static final EnderguardAmuletDragonMagic ENDERGUARD_AMULET = register(new EnderguardAmuletDragonMagic());
    public static final AntidoteBraceletDragonMagic ANTIDOTE_BRACELET = register(new AntidoteBraceletDragonMagic());
    public static final WitherguardAmuletDragonMagic WITHERGUARD_AMULET = register(new WitherguardAmuletDragonMagic());

    private static <T extends ExternalDragonMagic> T register(T dragonMagic) {
        DragonMagicRegistry.register(dragonMagic.getItemId(), dragonMagic);
        return dragonMagic;
    }

    /**
     * Registers the Dragon Magic of all items above in {@link DragonMagicRegistry}. Must be called in the constructor of
     * the mod, because Tomes And Libs adds the enchantment glint while the item models are baked.
     * <br> Loading this class registers the Dragon Magic through the initializers of the fields above.
     */
    public static void register(IEventBus eventBus) {
        eventBus.addListener(ExternalDragonMagicInit::onLoadComplete);
    }

    /**
     * Mana and Artifice only provides its spell helper in FMLLoadCompleteEvent, so the spell adjusters are registered
     * as enqueued work, after the listener of Mana and Artifice.
     */
    private static void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> ManaAndArtificeMod.getSpellHelper().registerSpellAdjuster(DefaultAdjusters::adjustOnToolTipAndCastOnly, BattlemageAmuletDragonMagic::adjustSpell));
    }
}
