package de.joh.dragonmagicandrelics.events;

import com.mojang.blaze3d.systems.RenderSystem;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.capabilities.client.ClientPlayerDragonMagic;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dragonmagicandrelics.item.client.armor.WingLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class overhauls the player renderer
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerRenderHandler {
    private static float fogAmount = 0.0F;
    private static boolean addedDragonWingLayer = false;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        if (!addedDragonWingLayer) {
            event.getRenderer().addLayer(new WingLayer<>(event.getRenderer(), Minecraft.getInstance().getEntityModels()));
            addedDragonWingLayer = true;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderOverlay(RenderBlockOverlayEvent event) {
        if (event.getBlockState().getBlock() == Blocks.FIRE) {
            if (ClientPlayerDragonMagic.hasMajorFireResistance()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onFogRenderEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (event.getCamera().getFluidInCamera() == FogType.LAVA) {
            if (ClientPlayerDragonMagic.hasMajorFireResistance()) {
                event.setNearPlaneDistance(-8.0F);
                event.setFarPlaneDistance(192.0F);
                event.setCanceled(true);
            }
        }

        if (fogAmount > 0.0F) {
            float f1 = Mth.lerp(Math.min(1.0F, fogAmount), event.getFarPlaneDistance(), 5.0F);
            float f2 = 0.0F;
            float f3 = f1 * 0.8F;
            RenderSystem.setShaderFogStart(f2);
            RenderSystem.setShaderFogEnd(f3);
        }
    }
}
