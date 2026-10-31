package de.joh.dmnr.client.item;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.IWingItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Bakes the wing layers that cannot be drawn as a tinted texture into textures of their own:
 * <br> - gradients ({@link IWingItem.WingLayer#gradient}): every visible pixel of the source gets a color between the
 * dark color (darkest pixel) and the light color (brightest pixel), chosen by its brightness
 * <br> - cape fills ({@link IWingItem.WingLayer#capeFill()}): every visible pixel of a gray layer gets the color of the
 * cape at the same place, darkened by the gray of the layer
 * <br> The alpha of the layer is kept in both cases.
 * <br> Textures are read through the resource manager, so a resource pack that changes them (e.g. the vanilla Elytra)
 * changes the wings as well. Capes of players are no resources, they are read back from the GPU.
 * <br> A texture is baked once per combination of sources and colors. Only the last {@link #MAX_TEXTURES} combinations
 * are kept, the least recently used one is released when another one is needed, e.g. while a color slider is dragged.
 * A resource reload releases all of them.
 * @see WingRenderLayer
 * @author Joh0210
 */
public final class BakedWingTextures implements ResourceManagerReloadListener {
    public static final BakedWingTextures INSTANCE = new BakedWingTextures();
    private static final int MAX_TEXTURES = 64;

    // Access order, so the first entry is the least recently used one
    private final Map<Key, ResourceLocation> textures = new LinkedHashMap<>(16, 0.75F, true);
    private final Map<ResourceLocation, Optional<Source>> sources = new HashMap<>();
    private int nextId = 0;

    private BakedWingTextures() {
    }

    /**
     * Only call on the render thread.
     * @param source texture to recolor
     * @param lightColor RGB color of the brightest pixels
     * @param darkColor RGB color of the darkest pixels
     * @return the recolored texture, or the source itself if it cannot be read
     */
    public ResourceLocation gradient(ResourceLocation source, int lightColor, int darkColor) {
        GradientKey key = new GradientKey(source, lightColor & 0xFFFFFF, darkColor & 0xFFFFFF);
        ResourceLocation baked = getOrBake(key, () -> {
            Source pixels = getSource(source);
            return pixels != null ? pixels.gradient(key.lightColor(), key.darkColor()) : null;
        });
        return baked != null ? baked : source;
    }

    /**
     * Only call on the render thread.
     * @param layer gray texture that gives shape and shading
     * @param cape cape texture of a player, in the vanilla cape layout
     * @param fallbackColor RGB color for the pixels where the cape is transparent
     * @return the filled texture, or null if one of the textures cannot be read (yet)
     */
    @Nullable
    public ResourceLocation capeFill(ResourceLocation layer, ResourceLocation cape, int fallbackColor) {
        CapeFillKey key = new CapeFillKey(layer, cape, fallbackColor & 0xFFFFFF);
        return getOrBake(key, () -> {
            Source layerPixels = getSource(layer);
            Source capePixels = getSource(cape);
            return layerPixels != null && capePixels != null ? layerPixels.capeFill(capePixels, key.fallbackColor()) : null;
        });
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textures.values().forEach(textureManager::release);
        textures.clear();
        sources.clear();
    }

    @Nullable
    private ResourceLocation getOrBake(Key key, Supplier<NativeImage> baker) {
        ResourceLocation baked = textures.get(key);
        if (baked != null) {
            return baked;
        }

        NativeImage image = baker.get();
        if (image == null) {
            return null;
        }

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textures.size() >= MAX_TEXTURES) {
            Iterator<ResourceLocation> leastRecentlyUsed = textures.values().iterator();
            textureManager.release(leastRecentlyUsed.next());
            leastRecentlyUsed.remove();
        }

        baked = RLoc.create("dynamic/wing_" + nextId++);
        textureManager.register(baked, new DynamicTexture(image));
        textures.put(key, baked);
        return baked;
    }

    /**
     * Reads a texture from the resource manager, or from the GPU if it is no resource. A texture that is registered
     * but not uploaded yet is not cached, so it is read again later.
     * @return the pixels of the texture, or null if it cannot be read
     */
    @Nullable
    private Source getSource(ResourceLocation location) {
        Optional<Source> cached = sources.get(location);
        if (cached != null) {
            return cached.orElse(null);
        }

        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(location);
        if (resource.isPresent()) {
            Source source = null;
            try (InputStream stream = resource.get().open(); NativeImage image = NativeImage.read(stream)) {
                source = Source.of(image);
            } catch (IOException e) {
                DragonMagicAndRelics.LOGGER.error("Couldn't read wing texture {}", location, e);
            }
            sources.put(location, Optional.ofNullable(source));
            return source;
        }

        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location, null);
        if (texture == null) {
            DragonMagicAndRelics.LOGGER.error("Couldn't find wing texture {}", location);
            sources.put(location, Optional.empty());
            return null;
        }

        Source downloaded = download(texture);
        if (downloaded != null) {
            sources.put(location, Optional.of(downloaded));
        }
        return downloaded;
    }

    /**
     * Reads the pixels of a texture back from the GPU, like the texture dump of vanilla does.
     * @return the pixels, or null if the texture has not been uploaded yet
     */
    @Nullable
    private static Source download(AbstractTexture texture) {
        RenderSystem.bindTexture(texture.getId());
        int width = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int height = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        if (width <= 0 || height <= 0) {
            return null;
        }

        try (NativeImage image = new NativeImage(width, height, false)) {
            image.downloadTexture(0, false);
            return Source.of(image);
        }
    }

    private sealed interface Key permits GradientKey, CapeFillKey {
    }

    private record GradientKey(ResourceLocation source, int lightColor, int darkColor) implements Key {
    }

    private record CapeFillKey(ResourceLocation layer, ResourceLocation cape, int fallbackColor) implements Key {
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

        private NativeImage gradient(int lightColor, int darkColor) {
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

        /**
         * This source is the gray layer. Both textures have the layout of the vanilla Elytra, so a pixel of the layer
         * takes the pixel of the cape at the same relative position, whatever the resolutions are.
         */
        private NativeImage capeFill(Source cape, int fallbackColor) {
            NativeImage image = new NativeImage(width, height, true);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int i = y * width + x;
                    int alpha = FastColor.ABGR32.alpha(pixels[i]);
                    if (alpha == 0) {
                        continue;
                    }

                    float shade = FastColor.ABGR32.red(pixels[i]) / 255.0F;
                    int capePixel = cape.pixels[(y * cape.height / height) * cape.width + x * cape.width / width];
                    boolean hasCape = FastColor.ABGR32.alpha(capePixel) > 0;
                    int red = hasCape ? FastColor.ABGR32.red(capePixel) : FastColor.ARGB32.red(fallbackColor);
                    int green = hasCape ? FastColor.ABGR32.green(capePixel) : FastColor.ARGB32.green(fallbackColor);
                    int blue = hasCape ? FastColor.ABGR32.blue(capePixel) : FastColor.ARGB32.blue(fallbackColor);
                    image.setPixelRGBA(x, y, FastColor.ABGR32.color(alpha, Math.round(blue * shade), Math.round(green * shade), Math.round(red * shade)));
                }
            }
            return image;
        }
    }
}
