package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.spell.component.ForceDamageComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the damage type tags. The damage types themselves are maintained by hand ({@code data/dmnr/damage_type}).
 * @author Joh0210
 */
public class ModDamageTypeTagsProvider extends TagsProvider<DamageType> {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        List<ResourceKey<DamageType>> unblockable = List.of(ForceDamageComponent.getForceDamage(),
                ForceDamageComponent.getRevengeDamage(), HellfireMobEffect.getHellfireDamage());
        unblockable.forEach(tag(DamageTypeTags.BYPASSES_ARMOR)::add);
        unblockable.forEach(tag(DamageTypeTags.BYPASSES_ENCHANTMENTS)::add);
    }
}
