package de.joh.dragonmagicandrelics.effects;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.effects.beneficial.EffectUltimateArmor;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Inits of all mod effects.
 * Registration through DragonMagicAndRelics
 * @see DragonMagicAndRelics
 * @author Joh0210
 */
public class EffectInit {
    public static final DeferredRegister<Effect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.POTIONS, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<Effect> ULTIMATE_ARMOR = EFFECTS.register("ultimate_armor", EffectUltimateArmor::new);

    public static void register(IEventBus eventBus){
        EFFECTS.register(eventBus);
    }

}
