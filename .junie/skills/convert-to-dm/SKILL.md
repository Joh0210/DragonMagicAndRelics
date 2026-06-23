
---
name: convert-to-dm
description: Convert an existing Item class to Dragon Magic Item
---

# Convert to Dragon Magic

<CONTEXT>
You are an expert Java developer for Minecraft mods (Forge/NeoForge). 
Your task is to inject a specific "Dragon Magic" capability into existing Item classes.
</CONTEXT>

<RULES>
When modifying the provided class, you MUST strictly follow these rules in order:

1. INHERITANCE & INTERFACES
   Analyze the existing class and modify its inheritance:
* Case A: If it currently extends `TieredItem`, change it to extend `de.joh.dmnr.api.item.BaseDragonMagicItem` instead.
* Case B: If it currently extends `ChargeableItem`, change it to extend `de.joh.dmnr.api.item.ChargeableDragonMagicItem` instead.
* Case C (Fallback): If it extends anything else, KEEP the existing inheritance, but directly implement the interface `de.joh.dmnr.api.item.IDragonMagicItem`.

2. CASE C METHOD INJECTIONS
   If and ONLY if Case C applies, you MUST implement/modify the following methods:
* `appendHoverText`: Add `this.tooltipAddition(tooltip);` exactly before the `super.appendHoverText(...)` call. If missing, generate the entire method:
  `@Override @OnlyIn(Dist.CLIENT) public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) { this.tooltipAddition(tooltip); super.appendHoverText(stack, worldIn, tooltip, flagIn); }`
* `isFoil`: Override to return exactly: `return this.isEnabled() && this.hasDragonMagic();`

3. DRAGON MAGIC ID
   Regardless of the inheritance case, you MUST implement this method:
   `@Override public String dragonMagicID() { return "item.dmnr.<item_id>.dragonmagic"; }`
   *Rule:* Derive `<item_id>` by converting the class name from CamelCase to snake_case (e.g., `FireSword` becomes `fire_sword`).

4. DRAGON MAGIC EFFECTS
   Implement the requested magic effect using the most logical approach:
* Approach 1 (Continuous/Tick): Check `if (this.hasDragonMagic(user))` inside `onTick` or a relevant handler.
* Approach 2 (Equip/Discard states): Override `onDMEquip(ItemStack itemStack, Player entity)` and `onDMDiscard(ItemStack itemStack, Player entity)` to handle permanent effects.

5. LOCALIZATION
   Provide the required JSON localization string for `src/main/resources/assets/dmnr/lang/en_us.json`.
   </RULES>

<OUTPUT_FORMAT>
1. Return the complete, updated Java code.
2. Return a separate JSON snippet containing the newly generated translation key and a fitting English description of the effect.
   </OUTPUT_FORMAT>