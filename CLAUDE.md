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
| Optional | MagiChem (own recipes under `recipes/magichem`), JEI (development runs only) |

### Build configuration

- `gradle.properties` is the **single source of truth** for all versions and mod metadata. `*_version` is the artifact used in development, `*_version_range` the range written into `mods.toml` (`mna_version`, `magichem_version`, `jei_version` are CurseMaven file IDs).
- `src/main/templates/META-INF/mods.toml` and `src/main/templates/pack.mcmeta` are templates. The `generateModMetadata` task in `build.gradle` expands their `${...}` placeholders; every placeholder must be listed in its `replaceProperties` map. Never put `META-INF/mods.toml` or `pack.mcmeta` into `src/main/resources`.
- Mod dependencies are declared with the remapping configurations `modImplementation`, `modCompileOnly`, `modRuntimeOnly` and `modLocalRuntime` (development runs only). They are **not transitive**; every required mod has to be listed explicitly. Every repository has a `content { includeGroup ... }` filter.
- A new required mod needs: version + range in `gradle.properties`, a dependency in `build.gradle`, an entry in the `replaceProperties` map and a `[[dependencies.${mod_id}]]` block in the `mods.toml` template.
- There is no Mixin setup. If a Mixin ever becomes necessary, it needs the Mixin annotation processor, a `mixin { }` block and the `MixinConfigs` manifest attribute (see the ModDevGradle `LEGACY.md`).

### Tomes And Libs (`lib/tnl-<tnl_version>.jar`)

- The author's own library (mod ID `tnl`, package `de.joh.tnl`), a regular (SRG-obfuscated) mod jar that is included via the `flatDir` repository and `modImplementation "de.joh:tnl:${tnl_version}"`, so ModDevGradle remaps it.
- Contains the extracted Dragon Magic API: `de.joh.tnl.api.item.IDragonMagicItem`, `BaseDragonMagicItem`, `ChargeableDragonMagicItem`, `ScrollableItem`, `de.joh.tnl.api.event.DragonMagicChangeEvent`, as well as the Curio Boost capability (`de.joh.tnl.capabilities.CurioBoostProvider`).
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
└── networking/                    ModMessages (SimpleChannel) and packet/ (S2C/C2S packets)
src/main/templates/                META-INF/mods.toml, pack.mcmeta (expanded from gradle.properties)
src/main/resources/
├── assets/dmnr/                   lang/ (en_us, zh_cn, ko_kr, ru_ru), guide/ (Codex entries), textures/, models/,
│                                  blockstates/, geo/ + animations/ (GeckoLib)
└── data/
    ├── dmnr/                      recipes/ (manaweaving, eldrin_altar, rituals, components, shapes, magichem, progression),
    │                              advancements/, loot_tables/, damage_type/, tags/
    ├── curios/tags/items/         Assignment of items to curio slots
    ├── mna/tags/items/            Extensions of M&A tags (e.g. relics)
    └── minecraft/tags/            Vanilla tags (mineable, damage_type)
src/generated/resources/           Datagen output (currently empty, included as a resource directory)
```

## Conventions

- **Registration**: new content is registered in the matching `*Init` class via `DeferredRegister`/`RegistryObject` (`ItemInit.ITEMS.register("snake_case_id", ...)`). Spells and rituals are registered in `SpellInit`/`RitualInit` via `RegisterEvent` on the mod bus; always create `ResourceLocation`s via `RLoc.create(...)`.
- **Event handlers**: classes annotated with `@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = ...)` and static `@SubscribeEvent` methods. Keep client code strictly in `client/` or guard it with `@OnlyIn(Dist.CLIENT)`/`Dist.CLIENT`.
- **Dragon Magic items**: extend `BaseDragonMagicItem` / `ChargeableDragonMagicItem` or implement `IDragonMagicItem` (package `de.joh.tnl.api.item`). `dragonMagicID()` returns the translation key `item.dmnr.<item_id>.dragonmagic`; the effect is implemented via `hasDragonMagic(entity)` or `onDMEquip`/`onDMDiscard`. Details: `.junie/skills/convert-to-dm/SKILL.md` (package names there are outdated, see above).
- **Translations**: every new key belongs at least in `assets/dmnr/lang/en_us.json`; tooltips use `item.dmnr.<id>.description`. New content should get a Codex entry in `assets/dmnr/guide/base/en_us.json` (or the folder of the optional mod).
- **Recipes/data**: JSON files are maintained by hand (no active datagen). M&A-specific recipe types live in the respective subfolders of `data/dmnr/recipes`. New curios need an entry in the matching `data/curios/tags/items/*.json`.
- **Configuration**: balancing values should be `ForgeConfigSpec` entries in `CommonConfig` instead of hardcoded values whenever they should be adjustable for modpacks.
- **Code style**: English identifiers and comments, Javadoc on classes with `@author Joh0210`, `@NotNull`/`@Nullable` from `org.jetbrains.annotations`. Follow the style of the surrounding class.
- **Mixins** only when there is no other way via events/APIs – they can easily cause incompatibilities (see "Build configuration" for the required setup).
