package de.joh.dmnr.common.init;


import com.mna.Registries;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.spell.component.*;
import de.joh.dmnr.common.spell.shape.AtMarkShape;
import de.joh.dmnr.common.spell.shape.CurseShape;
import de.joh.dmnr.common.spell.shape.TrueTouchShape;
import de.joh.dmnr.common.util.RLoc;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Register all spell-components and shapes. Call via the event bus.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpellInit {

    public static Shape ATMARK;
    public static Shape TRUE_TOUCH;
    public static Shape CURSE;

    public static SpellEffect ALTERNATIVERECALL;
    public static SpellEffect FORCE_DAMAGE;
    public static SpellEffect SUNRISE;
    public static SpellEffect MOONRISE;
    public static SpellEffect ULTIMATEARMOR;
    public static SpellEffect BANISH_RAIN;
    public static SpellEffect CONJURE_STORM;
    public static SpellEffect CONJURE_WATER;
    public static SpellEffect CONJURE_LAVA;
    public static SpellEffect SATURATE;
    public static SpellEffect MARK;

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.Shape.get().getRegistryKey(), (helper) -> {
            helper.register(RLoc.create("shapes/atmark"), SpellInit.ATMARK);
            helper.register(RLoc.create("shapes/true_touch"), SpellInit.TRUE_TOUCH);
            helper.register(RLoc.create("shapes/curse"), SpellInit.CURSE);
        });

        event.register(Registries.SpellEffect.get().getRegistryKey(), (helper) -> {
            helper.register(RLoc.create("components/sunrise"), SpellInit.SUNRISE);
            helper.register(RLoc.create("components/moonrise"), SpellInit.MOONRISE);
            helper.register(RLoc.create("components/ultimatearmor"), SpellInit.ULTIMATEARMOR);
            helper.register(RLoc.create("components/banishrain"), SpellInit.BANISH_RAIN);
            helper.register(RLoc.create("components/conjurestorm"), SpellInit.CONJURE_STORM);
            helper.register(RLoc.create("components/conjurewater"), SpellInit.CONJURE_WATER);
            helper.register(RLoc.create("components/conjurelava"), SpellInit.CONJURE_LAVA);
            helper.register(RLoc.create("components/saturate"), SpellInit.SATURATE);
            helper.register(RLoc.create("components/mark"), SpellInit.MARK);
            helper.register(RLoc.create("components/forcedamage"), SpellInit.ALTERNATIVERECALL);
            helper.register(RLoc.create("components/alternativerecall"), SpellInit.FORCE_DAMAGE);
        });
    }

    static {
        SpellInit.ATMARK = new AtMarkShape(RLoc.create("textures/spell/shape/atmark.png"));
        SpellInit.TRUE_TOUCH = new TrueTouchShape(RLoc.create("textures/spell/shape/true_touch.png"));
        SpellInit.CURSE = new CurseShape(RLoc.create("textures/spell/shape/curse.png"));

        SpellInit.SUNRISE = new SunriseComponent(RLoc.create("textures/spell/component/sunrise.png"));
        SpellInit.MOONRISE = new MoonriseComponent(RLoc.create("textures/spell/component/moonrise.png"));
        SpellInit.ULTIMATEARMOR = new UltimateArmorComponent(RLoc.create("textures/spell/component/ultimatearmor.png"));
        SpellInit.BANISH_RAIN = new BanishRainComponent(RLoc.create("textures/spell/component/banishrain.png"));
        SpellInit.CONJURE_STORM = new ConjureStormComponent(RLoc.create("textures/spell/component/conjurestorm.png"));
        SpellInit.CONJURE_WATER = new ConjureWaterComponent(RLoc.create("textures/spell/component/conjurewater.png"));
        SpellInit.CONJURE_LAVA = new ConjureLavaComponent(RLoc.create("textures/spell/component/conjurelava.png"));
        SpellInit.SATURATE = new SaturateComponent(RLoc.create("textures/spell/component/saturate.png"));
        SpellInit.MARK = new MarkComponent(RLoc.create("textures/spell/component/mark.png"));
        SpellInit.FORCE_DAMAGE = new ForceDamageComponent(RLoc.create("textures/spell/component/forcedamage.png"));
        SpellInit.ALTERNATIVERECALL = new AlternativeRecallComponent(RLoc.create("textures/spell/component/alternativerecall.png"));
    }
}
