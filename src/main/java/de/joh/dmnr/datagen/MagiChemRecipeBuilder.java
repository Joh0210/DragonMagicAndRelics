package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Base of the builders for the recipes of MagiChem. They are only loaded if MagiChem is installed.
 * <p>
 * MagiChem is not loaded during the data run (its own data generators break it), so its materia and items are checked
 * against its jar instead of the registries. {@code build.gradle} passes the path of the jar as
 * {@value #MAGICHEM_JAR_PROPERTY}, so the check always matches the MagiChem version of {@code gradle.properties}.
 * @param <T> type of the concrete builder, returned by the fluent methods
 * @author Joh0210
 */
public abstract class MagiChemRecipeBuilder<T extends MagiChemRecipeBuilder<T>> extends ModRecipeBuilder<T> {
    public static final String MAGICHEM = "magichem";
    private static final String MAGICHEM_JAR_PROPERTY = "dmnr.magichem_jar";

    @Nullable
    private static JarContent jarContent = null;

    private final Item object;

    protected MagiChemRecipeBuilder(ResourceLocation type, ItemLike object) {
        super(type);
        this.object = object.asItem();
        whenModLoaded(MAGICHEM);
    }

    protected record Materia(ResourceLocation id, int count) {
    }

    private record JarContent(Set<ResourceLocation> materia, Set<ResourceLocation> items) {
    }

    public Item getObject() {
        return object;
    }

    protected abstract void addMateria(Materia materia);

    public T essentia(String name, int count) {
        addMateria(new Materia(new ResourceLocation(MAGICHEM, "essentia_" + name), count));
        return self();
    }

    public T admixture(String name, int count) {
        addMateria(new Materia(new ResourceLocation(MAGICHEM, "admixture_" + name), count));
        return self();
    }

    protected JsonObject serializeObject(@Nullable Integer count) {
        JsonObject json = new JsonObject();
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(object).toString());
        if (count != null) {
            json.addProperty("count", count);
        }
        return json;
    }

    protected static JsonArray serializeMateria(List<Materia> materia) {
        JsonArray array = new JsonArray();
        for (Materia entry : materia) {
            JsonObject json = new JsonObject();
            json.addProperty("item", entry.id().toString());
            json.addProperty("count", entry.count());
            array.add(json);
        }
        return array;
    }

    protected static void validateMateria(List<String> problems, String field, List<Materia> materia, int max) {
        checkSize(problems, field, materia, max);

        Set<ResourceLocation> listed = new HashSet<>();
        for (Materia entry : materia) {
            if (!jarContent().materia().contains(entry.id())) {
                problems.add(field + ": " + entry.id() + " is not an essentia or admixture of MagiChem");
            }
            if (!listed.add(entry.id())) {
                problems.add(field + ": " + entry.id() + " is listed more than once");
            }
            if (entry.count() < 1) {
                problems.add(field + ": count " + entry.count() + " of " + entry.id() + " is not positive");
            }
        }
    }

    protected static boolean isKnownItem(ResourceLocation id) {
        return id.getNamespace().equals(MAGICHEM) ? jarContent().items().contains(id) : isRegisteredItem(id);
    }

    private static JarContent jarContent() {
        if (jarContent != null) {
            return jarContent;
        }

        String jarPath = System.getProperty(MAGICHEM_JAR_PROPERTY);
        if (jarPath == null) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": system property " + MAGICHEM_JAR_PROPERTY
                    + " with the path of the MagiChem jar is not set, see build.gradle");
        }

        Set<ResourceLocation> materia = new HashSet<>();
        Set<ResourceLocation> items = new HashSet<>();
        try (ZipFile jar = new ZipFile(jarPath)) {
            // The files MagiChem registers its materia from
            readMateria(jar, "data/magichem/generator/essentia.json", "essentia", "essentia_", materia);
            readMateria(jar, "data/magichem/generator/admixtures.json", "admixtures", "admixture_", materia);

            // MagiChem generates an item model for each of its items; the subfolders contain parts of other models
            String modelFolder = "assets/" + MAGICHEM + "/models/item/";
            for (ZipEntry entry : Collections.list(jar.entries())) {
                String name = entry.getName();
                if (name.startsWith(modelFolder) && name.endsWith(".json") && name.indexOf('/', modelFolder.length()) < 0) {
                    items.add(new ResourceLocation(MAGICHEM, name.substring(modelFolder.length(), name.length() - ".json".length())));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(DragonMagicAndRelics.MOD_ID + ": could not read the MagiChem jar " + jarPath, e);
        }

        jarContent = new JarContent(Set.copyOf(materia), Set.copyOf(items));
        return jarContent;
    }

    private static void readMateria(ZipFile jar, String path, String arrayName, String prefix, Set<ResourceLocation> materia) throws IOException {
        ZipEntry entry = jar.getEntry(path);
        if (entry == null) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": " + path + " is missing in " + jar.getName());
        }

        try (Reader reader = new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            for (JsonElement element : GsonHelper.getAsJsonArray(json, arrayName)) {
                materia.add(new ResourceLocation(MAGICHEM, prefix + GsonHelper.getAsString(element.getAsJsonObject(), "name")));
            }
        }
    }
}
