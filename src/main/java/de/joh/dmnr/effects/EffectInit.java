package de.joh.dmnr.effects;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.effects.beneficial.EffectElytra;
import de.joh.dmnr.effects.beneficial.EffectUltimateArmor;
import de.joh.dmnr.effects.beneficial.PeaceEffect;
import de.joh.dmnr.effects.harmful.BrokenPeaceEffect;
import de.joh.dmnr.effects.neutral.EffectFlyDisabled;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Inits of all mod effects.
 * Registration through DragonMagicAndRelics
 * @see DragonMagicAndRelics
 * @author Joh0210
 */
public class EffectInit {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DragonMagicAndRelics.MOD_ID);

    //beneficial
    public static final RegistryObject<MobEffect> ELYTRA = EFFECTS.register("elytra", EffectElytra::new);
    public static final RegistryObject<MobEffect> ULTIMATE_ARMOR = EFFECTS.register("ultimate_armor", EffectUltimateArmor::new);
    public static final RegistryObject<MobEffect> PEACE_EFFECT = EFFECTS.register("peace", PeaceEffect::new);

    //neutral
    public static final RegistryObject<MobEffect> FLY_DISABLED = EFFECTS.register("fly_disabled", EffectFlyDisabled::new);

    //harmful
    public static final RegistryObject<MobEffect> BROKEN_PEACE_EFFECT = EFFECTS.register("broken_peace", BrokenPeaceEffect::new);

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }

}
