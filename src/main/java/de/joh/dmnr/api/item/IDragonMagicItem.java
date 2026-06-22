package de.joh.dmnr.api.item;

import de.joh.dmnr.capabilities.client.ClientCurioBoost;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IDragonMagicItem {
    String dragonMagicID();


    @OnlyIn(Dist.CLIENT)
    default boolean hasDragonMagic(){

        return ClientCurioBoost.isEnabled();
    }

    @OnlyIn(Dist.CLIENT)
    default boolean isEnabled(){
        return !ClientCurioBoost.isBlacklisted(dragonMagicID());
    }


    @OnlyIn(Dist.CLIENT)
    default void tooltipAddition(@NotNull List<Component> tooltip){
        MutableComponent combined = Component.translatable("dmnr.dragonmagic.prefix").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GOLD);
        if(this.hasDragonMagic()){
            if(this.isEnabled()) {
                combined.append(Component.translatable(this.dragonMagicID()).withStyle(ChatFormatting.GRAY));
            }else{
                combined.append(Component.translatable(this.dragonMagicID()).withStyle(ChatFormatting.STRIKETHROUGH).withStyle(ChatFormatting.GRAY));
                combined.append(Component.translatable("dmnr.dragonmagic.disabled").withStyle(ChatFormatting.DARK_RED));
            }
        } else {
            combined.append(Component.translatable(this.dragonMagicID()).withStyle(style -> style.withFont(new ResourceLocation("minecraft", "alt"))).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(combined);
    }
}
