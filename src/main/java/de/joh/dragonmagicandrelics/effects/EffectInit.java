package de.joh.dragonmagicandrelics.effects;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.effects.beneficial.EffectUltimateArmor;
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

    public static final RegistryObject<MobEffect> ULTIMATE_ARMOR = EFFECTS.register("ultimate_armor", EffectUltimateArmor::new);

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }

}
