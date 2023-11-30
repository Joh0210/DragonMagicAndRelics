package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.effects.beneficial.ElytraMobEffect;
import de.joh.dmnr.common.effects.beneficial.UltimateArmorMobEffect;
import de.joh.dmnr.common.effects.beneficial.PeaceMobEffect;
import de.joh.dmnr.common.effects.harmful.BrokenPeaceMobEffect;
import de.joh.dmnr.common.effects.neutral.FlyDisabledMobEffect;
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
    public static final RegistryObject<MobEffect> ELYTRA = EFFECTS.register("elytra", ElytraMobEffect::new);
    public static final RegistryObject<MobEffect> ULTIMATE_ARMOR = EFFECTS.register("ultimate_armor", UltimateArmorMobEffect::new);
    public static final RegistryObject<MobEffect> PEACE_EFFECT = EFFECTS.register("peace", PeaceMobEffect::new);

    //neutral
    public static final RegistryObject<MobEffect> FLY_DISABLED = EFFECTS.register("fly_disabled", FlyDisabledMobEffect::new);

    //harmful
    public static final RegistryObject<MobEffect> BROKEN_PEACE_EFFECT = EFFECTS.register("broken_peace", BrokenPeaceMobEffect::new);

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }

}
