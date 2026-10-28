# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Git rules (mandatory)

- **Claude must not commit or push changes.** No `git commit`, no `git push`, no `git merge`/`rebase` onto remote branches, no creating tags or pull requests – not even when asked as part of another task.
- Claude only edits files in the working tree. Committing and pushing is done by the maintainer (Joh0210) personally.
- Read-only git commands (`git status`, `git diff`, `git log`, `git show`, `git blame`) are allowed.

## Language rule (mandatory)

- **All text created within this repository must be written exclusively in English.** This includes code, identifiers, comments, Javadoc, log messages, config comments, documentation, Markdown files, JSON content and Codex/guide entries.
- **Only exception: translations.** Localized files for other languages (e.g. `assets/dmnr/lang/zh_cn.json`, `ko_kr.json`, `ru_ru.json`, `assets/dmnr/guide/base/zh_cn.json`, `ko_kr.json`) are of course written in their respective language.
- This rule applies regardless of the language used in the conversation with Claude.

## Project context

**Dragon Magic and Relics** (mod ID `dmnr`, package `de.joh.dmnr`) is a Minecraft mod and an endgame add-on for **Mana and Artifice (M&A)**. It extends the M&A late game with powerful gear, spell components and shapes, rituals, effects and blocks. Its main target audience is modpacks.


- Core concept **"Dragon Magic"**: items can have an additional Dragon Magic ability that only becomes active under certain conditions (`hasDragonMagic(...)`). Its foundation lives in the **Tomes And Libs** library (see below).
- In-game documentation is provided via the M&A Codex/guidebook (`assets/dmnr/guide/base/*.json`; entries about optional-mod content live in their own folder, e.g. `guide/ae2`, which is only registered if that mod is installed); complete content only exists in `en_us`.

### Tech stack
| Component | Version |
|---|---|
| Minecraft | 1.20.1 |
| Loader | NeoForge 47.1.65 (the 1.20.1 NeoForge fork: mod ID `forge`, Java packages `net.minecraftforge`) |
| Build | Gradle 8.14.6 (wrapper), ModDevGradle Legacy `net.neoforged.moddev.legacyforge` |
| Java | 17 (toolchain, provisioned automatically); Gradle itself runs on Java 17–24 |
| Mappings | Official Mojang mappings + Parchment `2023.09.03` (1.20.1) |
| Required dependencies | Mana and Artifice (`mna`), Curios, Caelus, GeckoLib 4.2.2, Tomes And Libs (`tnl`) |
| Optional | MagiChem (recipes only, generated: distillation and sublimation), Applied Energistics 2 and Create (code integrations in `de.joh.dmnr.compat`), JEI (development runs only) |

### Build configuration

- `gradle.properties` is the **single source of truth** for all versions and mod metadata. `*_version` is the artifact used in development, `*_version_range` the range written into `mods.toml` (`mna_version`, `magichem_version`, `jei_version` are CurseMaven file IDs).
- `src/main/templates/META-INF/mods.toml` and `src/main/templates/pack.mcmeta` are templates. The `generateModMetadata` task in `build.gradle` expands their `${...}` placeholders; every placeholder must be listed in its `replaceProperties` map. Never put `META-INF/mods.toml` or `pack.mcmeta` into `src/main/resources`.
- Mod dependencies are declared with the remapping configurations `modImplementation`, `modCompileOnly`, `modRuntimeOnly`, `modLocalRuntime` (development runs only) and `modLocalGameRuntime` (development runs except `runData`, for mods whose own data generators break it, currently MagiChem). `magichemData` only resolves the MagiChem jar as a file for `runData` (see "MagiChem recipes"). They are **not transitive**; every required mod has to be listed explicitly. Every repository has a `content { includeGroup ... }` filter.
- A new required mod needs: version + range in `gradle.properties`, a dependency in `build.gradle`, an entry in the `replaceProperties` map and a `[[dependencies.${mod_id}]]` block in the `mods.toml` template.
- There is no Mixin setup. If a Mixin ever becomes necessary, it needs the Mixin annotation processor, a `mixin { }` block and the `MixinConfigs` manifest attribute (see the ModDevGradle `LEGACY.md`).

### Tomes And Libs (`lib/tnl-<tnl_version>.jar`)

- The author's own library (mod ID `tnl`, package `de.joh.tnl`), a regular (SRG-obfuscated) mod jar that is included via the `flatDir` repository and `modImplementation "de.joh:tnl:${tnl_version}"`, so ModDevGradle remaps it.
- Contains the extracted Dragon Magic API: `de.joh.tnl.api.item.IDragonMagicItem`, `BaseDragonMagicItem`, `ChargeableDragonMagicItem`, `ScrollableItem`, `de.joh.tnl.api.event.DragonMagicChangeEvent`, as well as the Curio Boost capability (`de.joh.tnl.capabilities.CurioBoostProvider`).
- Also contains the data generation code shared with Factions & Curiosities: `de.joh.tnl.api.item.CurioSlot` and `CurioRegistry` (used by `ItemInit.registerCurio`), and in `de.joh.tnl.api.datagen` all recipe builders (`ModRecipeBuilder`, `ItemAndPatternRecipeBuilder`, `ManaweavingRecipeBuilder`, `EldrinAltarRecipeBuilder`, `SpellPartRecipeBuilder`, `MagiChem*RecipeBuilder`), `ManaweavePatterns`, the provider bases `MnaRecipeProvider` (recipe ID scheme, `save` overloads, `ALL_AFFINITIES`, `magichemItem`), `MnaItemTagsProvider` (Curios slot tags, `mna:relics`), `DefaultItemModelProvider` (default item models, `handheldItem`) and `DataGenValidation.validateRegisters`. Changes to these classes are made in TnL; the providers in `de.joh.dmnr.datagen` extend them.
- The jar is version-controlled (exception `!lib/*.jar` in `.gitignore`). To update TnL, replace the jar and adjust `tnl_version`/`tnl_version_range`.
- Note: older references (e.g. `.junie/skills/convert-to-dm/SKILL.md`) still mention `de.joh.dmnr.api.item.*`. These classes no longer exist in DM&R – the correct package is `de.joh.tnl.api.item.*`.

### Branches

- `master`: main branch, should always contain a working version.
- `1.21.X-prep`: preparation/rework (including the new Dragon Magic system and removal of the old upgrade seals); the code base is currently still 1.20.1.
- `1.18`, `1.19`: states for older Minecraft versions.

## Build & run

On Windows use `gradlew.bat`, or `./gradlew` in Git Bash. The initial Gradle setup can take 10+ minutes.

```bash
./gradlew build             # Compiles and produces the reobfuscated release jar in build/libs (unobfuscated jar: build/devlibs)
./gradlew runClient         # Starts a dev client (game directory run/client)
./gradlew runServer         # Dedicated dev server (--nogui)
./gradlew runData           # Datagen into src/generated/resources
./gradlew runGameTestServer # Runs registered GameTests
```

## Project structure

```
build.gradle, settings.gradle     Build logic (ModDevGradle Legacy)
gradle.properties                  All versions and mod metadata
lib/tnl-<version>.jar              Tomes And Libs
icon-files/                        Source icons (not part of the mod)
run/                               Gradle run directories (local)
CREDITS.md, CONTRIBUTING.md        Credits for assets/translations, contribution guide
src/main/java/de/joh/dmnr/
├── DragonMagicAndRelics.java      Entry point (@Mod), registers DeferredRegisters, config, networking, guidebook
├── api/                           API usable by other mods (e.g. ConjureFluidComponent, ModTags, MarkSave, filters)
├── capabilities/                  Player data capability PlayerDragonMagic (marks per dimension) + client mirror
├── client/
│   ├── block/entity, item/        GeckoLib models/renderers (Rift Emitter, Dragon Mage Armor incl. wing layer)
│   ├── event/                     Client events (Forge and mod bus), player rendering
│   ├── gui/                       Containers/screens/MenuProviders (Ring of Spell Storing, Ring of Power, Bracelet of Friendship, …)
│   └── init/                      ContainerInit (MenuTypes), GuiInit (screen registration)
├── common/
│   ├── init/                      Registrations: ItemInit, BlockInit, BlockEntitieInit, EffectInit, CreativeModeTabInit,
│   │                              KeybindInit, SpellInit (M&A shapes/components), RitualInit (M&A rituals)
│   ├── item/                      All items, mainly curios (ICurioItem); spellstoring/ and material/ (ArmorMaterials)
│   ├── block/                     Blocks + block entities (Rift Emitter)
│   ├── effects/                   MobEffects, grouped into beneficial/harmful/neutral
│   ├── spell/component, shape/    Custom M&A spell components and shapes
│   ├── ritual/                    Custom M&A ritual effects
│   ├── event/                     Forge bus handlers: CommonEventHandler, DamageEventHandler, MagicEventHandler
│   └── util/                      CommonConfig (dmnr-common.toml), RLoc (ResourceLocation helper)
├── datagen/                       Data generators (GatherDataEvent): blockstates, block models, item models, tags, block loot tables
└── networking/                    ModMessages (SimpleChannel) and packet/ (S2C/C2S packets)
src/main/templates/                META-INF/mods.toml, pack.mcmeta (expanded from gradle.properties)
src/main/resources/
├── assets/dmnr/                   lang/ (en_us, zh_cn, ko_kr, ru_ru), guide/ (Codex entries), textures/,
│                                  models/block/<block_id>/ (hand-made parts: element models, OBJ wrappers + .obj,
│                                  Flywheel partial models), models/item/obj/ (OBJ models), geo/ + animations/ (GeckoLib)
└── data/dmnr/                     recipes/ (rituals, progression),
                                   advancements/, damage_type/
src/generated/resources/           Datagen output (./gradlew runData), committed, never edited by hand:
├── assets/dmnr/blockstates/       Blockstates (one variant per block)
├── assets/dmnr/models/block/      Top-level block models <block_id>.json (the subfolders are hand-made, see above)
├── assets/dmnr/models/item/       Item models
├── data/dmnr/recipes/             Manaweaving, Eldrin Altar, spell part and MagiChem recipes: manaweaving/,
│                                  eldrin_altar/, components/, shapes/, ae2/, create/ and magichem/ (distillation/,
│                                  sublimation/) (integrations, forge:conditional)
└── data/                          loot_tables/ (blocks) and all tags: dmnr/, curios/ (curio slots),
                                   mna/ (e.g. relics), minecraft/ (mineable, damage_type), create/ (non_movable)
```

## Conventions

- **Registration**: new content is registered in the matching `*Init` class via `DeferredRegister`/`RegistryObject` (`ItemInit.ITEMS.register("snake_case_id", ...)`). Curios are registered with `ItemInit.registerCurio("snake_case_id", CurioSlot.<SLOT>, ...)`, which also assigns them to their Curios slot (see "Data generation"). Spells and rituals are registered in `SpellInit`/`RitualInit` via `RegisterEvent` on the mod bus; always create `ResourceLocation`s via `RLoc.create(...)`.
- **Event handlers**: classes annotated with `@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = ...)` and static `@SubscribeEvent` methods. Keep client code strictly in `client/` or guard it with `@OnlyIn(Dist.CLIENT)`/`Dist.CLIENT`.
- **Dragon Magic items**: extend `BaseDragonMagicItem` / `ChargeableDragonMagicItem` or implement `IDragonMagicItem` (package `de.joh.tnl.api.item`). `dragonMagicID()` returns the translation key `item.dmnr.<item_id>.dragonmagic`; the effect is implemented via `hasDragonMagic(entity)` or `onDMEquip`/`onDMDiscard`. Details: `.junie/skills/convert-to-dm/SKILL.md` (package names there are outdated, see above).
- **Translations**: every new key belongs at least in `assets/dmnr/lang/en_us.json`; tooltips use `item.dmnr.<id>.description`. New content should get a Codex entry in `assets/dmnr/guide/base/en_us.json` (or the folder of the optional mod).
- **Data generation**: blockstates, block models (`models/block/<block_id>.json`), item models, tags (incl. curio slots), block loot tables and the manaweaving, Eldrin Altar, spell component, shape and MagiChem recipes are generated by the providers in `de.joh.dmnr.datagen` into `src/generated/resources`. Change the provider and run `./gradlew runData` instead of editing these files, and commit the regenerated output. New items get a flat item model with the texture `item/<item_id>` (block items: their block model) automatically; special models are added in the provider. Most tag entries are listed explicitly in the tag providers, except two tags derived from the items: the Curios slot tags (`curios:<slot>`) come from the slot passed to `ItemInit.registerCurio`, and `mna:relics` contains every item that implements `IRelic`. `runData` fails for an `ICurioItem` registered without a slot; curios that do not implement `ICurioItem` (effects handled by events) are not checked, so they must not be forgotten. A new block needs a blockstate and a model in `ModBlockStateProvider` (the blockstate is created automatically, the model must be defined there explicitly) and a loot table in `ModBlockLootProvider`, otherwise `runData` fails. Only the files `models/block/<block_id>.json` are generated; the subfolders of `models/block` (element models, OBJ wrappers, Flywheel partial models that are loaded by path) are maintained by hand in `src/main/resources`. A file must never exist in both `src/main/resources` and `src/generated/resources`. Providers that handle every item or block of the mod take them from `DataGenerators.itemRegisters()`/`blockRegisters()`; a new `DeferredRegister` (e.g. of a new integration) must be added there, otherwise `runData` fails.
- **Recipe builders**: all generated recipes come from `ModRecipeProvider`, written with builders based on `ModRecipeBuilder`. It collects every problem of a recipe in `validate` and fails `runData` with the full list in `save(writer, id, existingFileHelper)`, writes the `type` itself (serializers of MagiChem are not registered during `runData`), generates no advancements, and wraps the recipe in `forge:conditional` when it has conditions (`whenModLoaded(modId)`). Shared checks live there too (`checkRange`, `checkSize`, `checkItemOutput`). The M&A builders extend `ItemAndPatternRecipeBuilder`, which takes the item and pattern limits of the crafting station as constructor arguments; `item(ItemLike, count)` adds an item several times.
- **Manaweaving recipes** (`mna:manaweaving-recipe`) are written with `ManaweavingRecipeBuilder`. IDs: `dmnr:manaweaving/<name>`; recipes of an integration are `dmnr:<mod_id>/<name>`, wrapped in `forge:conditional` with `forge:mod_loaded` (`saveIntegration`). Never change a recipe ID: the Codex references the recipes by ID. M&A resolves an entry of `items` as an item if one with that ID exists, otherwise as an item tag, so tags are written without `#`; `items` is matched shapeless, `patterns` in order. Pattern IDs are always the full form `mna:manaweave_patterns/<name>` (constants in `ModRecipeProvider`). `quantity` is always written (M&A default: 1). The builder fails `runData` if the tier is outside 1–5, `items`/`patterns` are empty or too long (9/6), the output or an item is not registered, a tag uses an item ID, or a tag/pattern does not exist according to the `ExistingFileHelper`. Not checked: the faction ID, whether a tag contains items, and the tier of the ingredients.
- **Eldrin Altar recipes** (`mna:eldrin-altar`) come from `ModRecipeProvider` as well and are written with `EldrinAltarRecipeBuilder` (same base and the same rules for `items`, tier, faction and `quantity`). IDs: `dmnr:eldrin_altar/<name>`. The altar has no patterns, so `patterns` is not written (the base class omits it when the pattern limit is 0). Instead `power_requirements` lists the Eldrin power per affinity (`power(amount, Affinity...)`). Eldrin recipes sit next to the manaweaving recipes of the same item group in the provider, so upgrade stages stay together. Additionally to the shared checks, the builder fails `runData` if `power_requirements` is empty, an affinity is not one of `Affinity.CoreSix()` (there are no Eldrin conduits for the others) or is listed twice, or an amount is not positive. `colors` is read by M&A but never used, so the builder does not support it.
- **Spell component and shape recipes** (`mna:component`, `mna:shape`) come from `ModRecipeProvider` as well and are written with one builder for both, `SpellPartRecipeBuilder` (`component(SpellEffect, tier)` / `shape(Shape, tier)`; same base and the same rules for `items`, `patterns`, tier and faction, limits 9/9). `output` is the registry ID of the spell part, `quantity` is not written. The recipe ID is always the registry ID of the spell part (`dmnr:components/<name>`, `dmnr:shapes/<name>`), also for integrations (e.g. `dmnr:components/spectral_motor`, made conditional with `whenModLoaded`): M&A takes the tier of a spell part from the recipe with the same ID and the Codex (`"type": "spell_part"`) finds it by that ID, so the builder derives the ID itself and fails for any other ID or an unregistered spell part.
- **MagiChem recipes** (distillation and sublimation) come from `ModRecipeProvider` as well, written with `MagiChemDistillationRecipeBuilder` and `MagiChemSublimationRecipeBuilder` (shared base `MagiChemRecipeBuilder`). The ID is derived from the item: `dmnr:magichem/<distillation|sublimation>/<item_id>` (`save` overloads in the provider), so every item has at most one of each; the base adds `whenModLoaded("magichem")` itself, so the recipe is always wrapped in `forge:conditional`. Materia are added with `essentia(name, count)` / `admixture(name, count)` (`magichem:essentia_<name>`, `magichem:admixture_<name>`), in order; a materia listed twice, a non-positive count or more than 5 materia per list fail `runData`. MagiChem is not loaded during `runData`, so the base reads two things from the MagiChem jar: the materia from MagiChem's own definitions (`data/magichem/generator/essentia.json`, `admixtures.json`) and its item IDs from its item models (`assets/magichem/models/item/*.json`). `build.gradle` resolves the jar through the configuration `magichemData` (same `magichem_version`, never on a classpath) and passes its path to the data run as the system property `tnl.magichem_jar`. Items of MagiChem are referenced by ID (`magichemItem(...)` in the provider). Advancement fields (`required_advancement`, `forbidden_advancement`, `granted_advancement`) are not supported.
  - **Distillation** (`magichem:distillation_fabrication`, `distillation(item)`): the materia go into `components`, at most 5. Defaults: `wisdom` 6 (distill only, no fabrication; 0–5 would allow it), `categories` `CRAFTABLE` (bitmask enum `Category`, `categories(...)` replaces it), `batch_size` 1, `output_rate` 1.0. Also fails for wisdom outside 0–6, batch size outside 1–127, output rate outside 0 (exclusive)–1 or empty categories.
  - **Sublimation** (`magichem:sublimation`, `sublimation(item, tier)`, Alchemical Nexus): `stage(experience)` starts a stage; the following `items(...)` / `item(id)` (single items, repeated for several) and `essentia`/`admixture` calls belong to it. MagiChem reads a `tag` in `components` but ignores it, so tags are not supported. `object.count` (`quantity(...)`, default 1) is always written. Defaults: `wisdom` 0 (wisdom the Nexus needs to offer the recipe). Also fails for tier outside 1–5, wisdom outside 0–5, quantity outside 1–max stack size, no or more than 4 stages, negative experience, no or more than 5 components per stage, or a component that is neither registered nor a MagiChem item. The limits are those of MagiChem's own recipes.
- **Recipes/data**: all other JSON files (all other recipe types: `rituals`, `progression`; advancements, damage types, Codex, lang) are maintained by hand. M&A-specific recipe types live in the respective subfolders of `data/dmnr/recipes`.
- **Configuration**: balancing values should be `ForgeConfigSpec` entries in `CommonConfig` instead of hardcoded values whenever they should be adjustable for modpacks.
- **Code style**: English identifiers and comments, Javadoc on classes with `@author Joh0210`, `@NotNull`/`@Nullable` from `org.jetbrains.annotations`. Follow the style of the surrounding class.
- **Mixins** only when there is no other way via events/APIs – they can easily cause incompatibilities (see "Build configuration" for the required setup).
