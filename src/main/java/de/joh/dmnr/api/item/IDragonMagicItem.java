package de.joh.dmnr.api.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IDragonMagicItem {
    String dragonMagicID();


    @OnlyIn(Dist.CLIENT)
    default boolean hasDragonMagic(){
        var player = Minecraft.getInstance().player;

        if (player != null) {
            return player.hasEffect(MobEffects.INVISIBILITY);
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    default boolean isEnabled(){
        //todo: test that the ID is not blacklisted
        return this.hasDragonMagic();
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
