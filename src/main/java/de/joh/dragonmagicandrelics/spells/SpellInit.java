package de.joh.dragonmagicandrelics.spells;


import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.spells.components.*;
import de.joh.dragonmagicandrelics.spells.shapes.ShapeAtMark;
import de.joh.dragonmagicandrelics.spells.shapes.ShapeTrueTouch;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Register all spell-components and shapes. Call via the event bus.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpellInit {

    public static Shape ATMARK;
    public static Shape TRUE_TOUCH;

    public static SpellEffect ALTERNATIVERECALL;
    public static SpellEffect SUNRISE;
    public static SpellEffect MOONRISE;
    public static SpellEffect ULTIMATEARMOR;
    public static SpellEffect BANISHRAIN;
    public static SpellEffect CONJURESTORM;
    public static SpellEffect CONJUREWATER;
    public static SpellEffect CONJURELAVA;
    public static SpellEffect SATURATE;
    public static SpellEffect MARK;

    @SubscribeEvent
    public static void registerShapes(final RegistryEvent.Register<Shape> event) {
        event.getRegistry().register(SpellInit.ATMARK);
        event.getRegistry().register(SpellInit.TRUE_TOUCH);
    }

    @SubscribeEvent
    public static void registerComponents(final RegistryEvent.Register<SpellEffect> event) {
        event.getRegistry().register(SpellInit.SUNRISE);
        event.getRegistry().register(SpellInit.MOONRISE);
        event.getRegistry().register(SpellInit.ULTIMATEARMOR);
        event.getRegistry().register(SpellInit.BANISHRAIN);
        event.getRegistry().register(SpellInit.CONJURESTORM);
        event.getRegistry().register(SpellInit.CONJUREWATER);
        event.getRegistry().register(SpellInit.CONJURELAVA);
        event.getRegistry().register(SpellInit.SATURATE);
        event.getRegistry().register(SpellInit.MARK);
        event.getRegistry().register(SpellInit.ALTERNATIVERECALL);
    }

    static {
        SpellInit.ATMARK = new ShapeAtMark(RLoc.create("shapes/atmark"), RLoc.create("textures/spell/shape/atmark.png"));
        SpellInit.TRUE_TOUCH = new ShapeTrueTouch(RLoc.create("shapes/true_touch"), RLoc.create("textures/spell/shape/true_touch.png"));

        SpellInit.SUNRISE = new ComponentSunrise(RLoc.create("components/sunrise"), RLoc.create("textures/spell/component/sunrise.png"));
        SpellInit.MOONRISE = new ComponentMoonrise(RLoc.create("components/moonrise"), RLoc.create("textures/spell/component/moonrise.png"));
        SpellInit.ULTIMATEARMOR = new ComponentUltimateArmor(RLoc.create("components/ultimatearmor"), RLoc.create("textures/spell/component/ultimatearmor.png"));
        SpellInit.BANISHRAIN = new ComponentBanishRain(RLoc.create("components/banishrain"), RLoc.create("textures/spell/component/banishrain.png"));
        SpellInit.CONJURESTORM = new ComponentConjureStorm(RLoc.create("components/conjurestorm"), RLoc.create("textures/spell/component/conjurestorm.png"));
        SpellInit.CONJUREWATER = new ComponentConjureWater(RLoc.create("components/conjurewater"), RLoc.create("textures/spell/component/conjurewater.png"));
        SpellInit.CONJURELAVA = new ComponentConjureLava(RLoc.create("components/conjurelava"), RLoc.create("textures/spell/component/conjurelava.png"));
        SpellInit.SATURATE = new ComponentSaturate(RLoc.create("components/saturate"), RLoc.create("textures/spell/component/saturate.png"));
        SpellInit.MARK = new ComponentMark(RLoc.create("components/mark"), RLoc.create("textures/spell/component/mark.png"));
        SpellInit.ALTERNATIVERECALL = new ComponentAlternativeRecall(RLoc.create("components/alternativerecall"), RLoc.create("textures/spell/component/alternativerecall.png"));
    }
}
