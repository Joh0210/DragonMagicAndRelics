package de.joh.dragonmagicandrelics.spells;


import com.ma.api.spells.parts.Component;
import com.ma.api.spells.parts.Shape;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.spells.components.*;
import de.joh.dragonmagicandrelics.spells.shapes.ShapeAtMark;
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

    public static Component ALTERNATIVERECALL;
    public static Component SUNRISE;
    public static Component MOONRISE;
    public static Component ULTIMATEARMOR;
    public static Component BANISHRAIN;
    public static Component CONJURESTORM;
    public static Component CONJUREWATER;
    public static Component CONJURELAVA;
    public static Component SATURATE;
    //public static Component MARK;

    @SubscribeEvent
    public static void registerShapes(final RegistryEvent.Register<Shape> event) {
        event.getRegistry().register(SpellInit.ATMARK);
    }

    @SubscribeEvent
    public static void registerComponents(final RegistryEvent.Register<Component> event) {
        event.getRegistry().register(SpellInit.SUNRISE);
        event.getRegistry().register(SpellInit.MOONRISE);
        event.getRegistry().register(SpellInit.ULTIMATEARMOR);
        event.getRegistry().register(SpellInit.BANISHRAIN);
        event.getRegistry().register(SpellInit.CONJURESTORM);
        event.getRegistry().register(SpellInit.CONJUREWATER);
        event.getRegistry().register(SpellInit.CONJURELAVA);
        event.getRegistry().register(SpellInit.SATURATE);
        //event.getRegistry().register(SpellInit.MARK);
        event.getRegistry().register(SpellInit.ALTERNATIVERECALL);
    }

    static {
        SpellInit.ATMARK = new ShapeAtMark(RLoc.create("shapes/atmark"), RLoc.create("textures/spell/shape/atmark.png"));

        SpellInit.SUNRISE = new ComponentSunrise(RLoc.create("components/sunrise"), RLoc.create("textures/spell/component/sunrise.png"));
        SpellInit.MOONRISE = new ComponentMoonrise(RLoc.create("components/moonrise"), RLoc.create("textures/spell/component/moonrise.png"));
        SpellInit.ULTIMATEARMOR = new ComponentUltimateArmor(RLoc.create("components/ultimatearmor"), RLoc.create("textures/spell/component/ultimatearmor.png"));
        SpellInit.BANISHRAIN = new ComponentBanishRain(RLoc.create("components/banishrain"), RLoc.create("textures/spell/component/banishrain.png"));
        SpellInit.CONJURESTORM = new ComponentConjureStorm(RLoc.create("components/conjurestorm"), RLoc.create("textures/spell/component/conjurestorm.png"));
        SpellInit.CONJUREWATER = new ComponentConjureWater(RLoc.create("components/conjurewater"), RLoc.create("textures/spell/component/conjurewater.png"));
        SpellInit.CONJURELAVA = new ComponentConjureLava(RLoc.create("components/conjurelava"), RLoc.create("textures/spell/component/conjurelava.png"));
        SpellInit.SATURATE = new ComponentSaturate(RLoc.create("components/saturate"), RLoc.create("textures/spell/component/saturate.png"));
        //SpellInit.MARK = new ComponentMark(RLoc.create("components/mark"), RLoc.create("textures/spell/component/mark.png"));
        SpellInit.ALTERNATIVERECALL = new ComponentAlternativeRecall(RLoc.create("components/alternativerecall"), RLoc.create("textures/spell/component/alternativerecall.png"));
    }
}
