package de.joh.dmnr.client.item;

import com.mojang.blaze3d.platform.NativeImage;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.IWingItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Bakes the gradient layers of wings ({@link IWingItem.WingLayer#gradient}) into textures: every visible pixel of the
 * source texture gets a color between the dark color (darkest pixel) and the light color (brightest pixel), chosen by
 * its brightness. The alpha of the pixels is kept.
 * <br> The source is read through the resource manager, so a resource pack that changes it (e.g. the vanilla Elytra)
 * changes the wings as well.
 * <br> A texture is baked once per combination of source and colors. Only the last {@link #MAX_TEXTURES} combinations
 * are kept, the least recently used one is released when another one is needed, e.g. while a color slider is dragged.
 * A resource reload releases all of them.
 * @see WingRenderLayer
 * @author Joh0210
 */
public final class WingGradientTextures implements ResourceManagerReloadListener {
    public static final WingGradientTextures INSTANCE = new WingGradientTextures();
    private static final int MAX_TEXTURES = 64;

    // Access order, so the first entry is the least recently used one
    private final Map<Key, ResourceLocation> textures = new LinkedHashMap<>(16, 0.75F, true);
    private final Map<ResourceLocation, Optional<Source>> sources = new HashMap<>();
    private int nextId = 0;

    private WingGradientTextures() {
    }

    /**
     * Only call on the render thread.
     * @param source texture to recolor
     * @param lightColor RGB color of the brightest pixels
     * @param darkColor RGB color of the darkest pixels
     * @return the recolored texture, or the source itself if it cannot be read
     */
    public ResourceLocation get(ResourceLocation source, int lightColor, int darkColor) {
        Key key = new Key(source, lightColor & 0xFFFFFF, darkColor & 0xFFFFFF);
        ResourceLocation baked = textures.get(key);
        if (baked != null) {
            return baked;
        }

        Optional<Source> pixels = sources.computeIfAbsent(source, WingGradientTextures::readSource);
        if (pixels.isEmpty()) {
            return source;
        }

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textures.size() >= MAX_TEXTURES) {
            Iterator<ResourceLocation> leastRecentlyUsed = textures.values().iterator();
            textureManager.release(leastRecentlyUsed.next());
            leastRecentlyUsed.remove();
        }

        baked = RLoc.create("dynamic/wing_gradient_" + nextId++);
        textureManager.register(baked, new DynamicTexture(pixels.get().bake(key.lightColor(), key.darkColor())));
        textures.put(key, baked);
        return baked;
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textures.values().forEach(textureManager::release);
        textures.clear();
        sources.clear();
    }

    private static Optional<Source> readSource(ResourceLocation location) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(location);
        if (resource.isEmpty()) {
            DragonMagicAndRelics.LOGGER.error("Couldn't find wing texture {}", location);
            return Optional.empty();
        }

        try (InputStream stream = resource.get().open(); NativeImage image = NativeImage.read(stream)) {
            return Optional.of(Source.of(image));
        } catch (IOException e) {
            DragonMagicAndRelics.LOGGER.error("Couldn't read wing texture {}", location, e);
            return Optional.empty();
        }
    }

    private record Key(ResourceLocation source, int lightColor, int darkColor) {
    }

    /**
     * Pixels of a source texture, in the ABGR format of {@link NativeImage}, and the brightness of each pixel relative
     * to the darkest (0) and the brightest (1) visible pixel.
     */
    private record Source(int width, int height, int[] pixels, float[] brightness) {
        private static Source of(NativeImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            float[] luma = new float[width * height];
            float min = Float.MAX_VALUE;
            float max = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = y * width + x;
                    int abgr = image.getPixelRGBA(x, y);
                    pixels[i] = abgr;
                    luma[i] = 0.299F * FastColor.ABGR32.red(abgr) + 0.587F * FastColor.ABGR32.green(abgr) + 0.114F * FastColor.ABGR32.blue(abgr);
                    if (FastColor.ABGR32.alpha(abgr) > 0) {
                        min = Math.min(min, luma[i]);
                        max = Math.max(max, luma[i]);
                    }
                }
            }

            // A texture with only one brightness gets the light color everywhere
            float range = max - min;
            for (int i = 0; i < luma.length; i++) {
                luma[i] = range > 0 ? Mth.clamp((luma[i] - min) / range, 0, 1) : 1;
            }
            return new Source(width, height, pixels, luma);
        }

        private NativeImage bake(int lightColor, int darkColor) {
            NativeImage image = new NativeImage(width, height, true);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = y * width + x;
                    int alpha = FastColor.ABGR32.alpha(pixels[i]);
                    if (alpha == 0) {
                        continue;
                    }

                    float t = brightness[i];
                    int red = Math.round(Mth.lerp(t, FastColor.ARGB32.red(darkColor), FastColor.ARGB32.red(lightColor)));
                    int green = Math.round(Mth.lerp(t, FastColor.ARGB32.green(darkColor), FastColor.ARGB32.green(lightColor)));
                    int blue = Math.round(Mth.lerp(t, FastColor.ARGB32.blue(darkColor), FastColor.ARGB32.blue(lightColor)));
                    image.setPixelRGBA(x, y, FastColor.ABGR32.color(alpha, blue, green, red));
                }
            }
            return image;
        }
    }
}
