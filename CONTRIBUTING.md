# Contribution Guide

Welcome to the Contribution Guide for Dragon Magic And Relics! 
<br>Thank you for your interest in improving the project — whether you're fixing bugs, adding features, or helping with documentation. Every contribution helps! 

## Requirements
- [Java Development Kit](https://www.oracle.com/ae/java/technologies/downloads/) with Java 17 or higher.
- Recommended is an IDE to make coding more enjoyable (I use [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/?section=windows)).
- The remaining configs and data are included in the Git.

## Setup
1. Fork the repository on GitHub and clone it to your local machine.
2. Open the project in your preferred IDE.
3. Let Gradle finish importing and setting up dependencies (this may take 10+ minutes).
4. Use the `runClient` task via Gradle to start the modded Minecraft client. <br>At this point, no changes should be necessary — the main branch should always contain a working version of the mod.
5. Make your changes!
6. Commit your work to your forked repository.
7. Open a **Pull Request** to the main Dragon Magic And Relics repository.
8. Your changes will be reviewed and, if accepted, included in a future update. :D

## Project Structure
### Code
Located in: `src/main/java/de/joh/dmnr`
- **`DragonMagicAndRelics.java`**: The main entry point of the mod.
- **Packages**:
  - `client`: Client-only features like GUIs.
  - `common`: Shared code (items, handlers, etc.).
  - `networking`: Handles communication between client and server using packets.
  - `api`: Exposed API allowing other mods to hook into DM&R features.
  - `capabilities`: Extensions to player data.
  - `mixins`: Injected modifications to base Minecraft code.<br>**Use with caution** – can easily introduce bugs or incompatibilities.

Subpackages in `common` group game areas. Content is initialized in the corresponding `init` classes.  
Events are handled in the `event` package, and reusable helper functions are located in `util`.

### Data
Located in: `src/main/resources`
- **`assets`**: Client-specific content (textures, translations, guidebook)
- **`data`**: Server-synced definitions (recipes, loot tables, tags)
- **`META-INF`**: Mod metadata required by NeoForge