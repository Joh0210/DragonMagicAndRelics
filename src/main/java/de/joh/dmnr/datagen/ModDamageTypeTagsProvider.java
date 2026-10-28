package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.spell.component.ForceDamageComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Joh0210
 */
public class ModDamageTypeTagsProvider extends TagsProvider<DamageType> {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        for (TagKey<DamageType> unblockable : List.of(DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            tag(unblockable).add(ForceDamageComponent.getForceDamage(), ForceDamageComponent.getRevengeDamage(),
                    HellfireMobEffect.getHellfireDamage());
        }
    }
}
