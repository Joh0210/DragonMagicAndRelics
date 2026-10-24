# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Git rules (mandatory)

- **Claude must not commit or push changes.** No `git commit`, no `git push`, no `git merge`/`rebase` onto remote branches, no creating tags or pull requests – not even when asked as part of another task.
- Claude only edits files in the working tree. Committing and pushing is done by the maintainer (Joh0210) personally.
- Read-only git commands (`git status`, `git diff`, `git log`, `git show`, `git blame`) are allowed.

## Language rule (mandatory)

- **All text created within this repository must be written exclusively in English.** This includes code, identifiers, comments, Javadoc, log messages, config comments, documentation, Markdown files, JSON content and Codex/guide entries.
- **Only exception: translations.** Localized files for other languages (e.g. `assets/dmnr/lang/zh_cn.json`, `ko_kr.json`, `ru_ru.json`, `assets/dmnr/guide/zh_cn.json`, `ko_kr.json`) are of course written in their respective language.
- This rule applies regardless of the language used in the conversation with Claude.

## Project context

**Dragon Magic and Relics** (mod ID `dmnr`, package `de.joh.dmnr`) is a Minecraft mod and an endgame add-on for **Mana and Artifice (M&A)**. It extends the M&A late game with powerful gear, spell components and shapes, rituals, effects and blocks. Its main target audience is modpacks.


- Core concept **"Dragon Magic"**: items can have an additional Dragon Magic ability that only becomes active under certain conditions (`hasDragonMagic(...)`). Its foundation lives in the **Tomes And Libs** library (see below).
- In-game documentation is provided via the M&A Codex/guidebook (`assets/dmnr/guide/*.json`); complete content only exists in `en_us`.

### Tech stack
| Component | Version |
|---|---|
| Minecraft | 1.20.1 |
| Loader | NeoForge (Forge namespace `net.minecraftforge`) 47.1.65, plugin `net.neoforged.gradle` |
| Java | 17 (toolchain) |
| Mappings | Parchment `2023.09.03-1.20.1` |
| Required dependencies | Mana and Artifice (`mna`), Curios, Caelus, GeckoLib 4.2.2, Tomes And Libs (`tnl`) |
| Optional | MagiChem (own recipes under `recipes/magichem`), JEI (runtime only) |

Dependency versions are defined in `gradle.properties` (`mna_version`, `magichem_version`, `jei_version` are CurseMaven file IDs).

### Tomes And Libs (`lib/tnl-1.0-dev.jar`)

- The author's own library (mod ID `tnl`, package `de.joh.tnl`), included via `flatDir` + `implementation files('lib/tnl-1.0-dev.jar')`.
- Contains the extracted Dragon Magic API: `de.joh.tnl.api.item.IDragonMagicItem`, `BaseDragonMagicItem`, `ChargeableDragonMagicItem`, `ScrollableItem`, `de.joh.tnl.api.event.DragonMagicChangeEvent`, as well as the Curio Boost capability (`de.joh.tnl.capabilities.CurioBoostProvider`).
- The jar is **not version-controlled** because of `.gitignore` (`*.jar`) – it must be present locally, otherwise the build fails.
- Note: older references (e.g. `.junie/skills/convert-to-dm/SKILL.md`) still mention `de.joh.dmnr.api.item.*`. These classes no longer exist in DM&R – the correct package is `de.joh.tnl.api.item.*`.

### Branches

- `master`: main branch, should always contain a working version.
- `1.21.X-prep`: preparation/rework (including the new Dragon Magic system and removal of the old upgrade seals); the code base is currently still 1.20.1.
- `1.18`, `1.19`: states for older Minecraft versions.

## Build & run

On Windows use `gradlew.bat`, or `./gradlew` in Git Bash. The initial Gradle setup can take 10+ minutes.

```bash
./gradlew build             # Compiles and produces the reobfuscated jar in build/libs
./gradlew runClient         # Starts a dev client (working directory run/client)
./gradlew runServer         # Dedicated dev server (--nogui)
./gradlew runData           # Datagen into src/generated/resources
./gradlew runGameTestServer # Runs registered GameTests
```

## Project structure

```
build.gradle, gradle.properties   Build configuration, versions, mod metadata
lib/tnl-1.0-dev.jar                Tomes And Libs (local, not version-controlled)
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
├── mixins/                        Mixins (LivingEntityFireWalkerMixin) + MixinPlugin – use with caution
└── networking/                    ModMessages (SimpleChannel) and packet/ (S2C/C2S packets)
src/main/resources/
├── META-INF/mods.toml             Mod metadata and dependencies
├── dmnr.mixins.json               Mixin configuration
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
- **Translations**: every new key belongs at least in `assets/dmnr/lang/en_us.json`; tooltips use `item.dmnr.<id>.description`. New content should get a Codex entry in `assets/dmnr/guide/en_us.json`.
- **Recipes/data**: JSON files are maintained by hand (no active datagen). M&A-specific recipe types live in the respective subfolders of `data/dmnr/recipes`. New curios need an entry in the matching `data/curios/tags/items/*.json`.
- **Configuration**: balancing values should be `ForgeConfigSpec` entries in `CommonConfig` instead of hardcoded values whenever they should be adjustable for modpacks.
- **Code style**: English identifiers and comments, Javadoc on classes with `@author Joh0210`, `@NotNull`/`@Nullable` from `org.jetbrains.annotations`. Follow the style of the surrounding class.
- **Mixins** only when there is no other way via events/APIs – they can easily cause incompatibilities.
