package de.joh.dragonmagicandrelics.capabilities.dragonmagic;

import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnTick;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnEquipped;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.MarkSave;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the permanent data that is stored on a player for the magic components of this mod.
 * Each function here is called on a player-specific basis, since every player owns an instance of this class.
 * Currently includes: Player specific save for the Rune of Marking
 * @see de.joh.dragonmagicandrelics.spells.components.ComponentMark
 * @see de.joh.dragonmagicandrelics.spells.components.ComponentAlternativeRecall
 * @see de.joh.dragonmagicandrelics.spells.shapes.ShapeAtMark
 * @author Joh0210
 */
public class PlayerDragonMagic {
    protected HashMap<String, Pair<ArmorUpgrade, Integer>> onEventUpgrade = new HashMap<>();
    protected HashMap<String, Pair<IArmorUpgradeOnTick, Integer>> onTickUpgrade = new HashMap<>();
    protected HashMap<String, Pair<IArmorUpgradeOnEquipped, Integer>> onEquipUpgrade = new HashMap<>();
    /**
     * List of dimensions with corresponding mark, for ComponentMark, ComponentAlternativeRecall, ...
     */
    private HashMap<ResourceKey<Level>, MarkSave> markMap = new HashMap<>();

    /**
     * Save this position as a mark, in this dimension, for the player who executes it.
     */
    public void mark(BlockPos position, Direction direction, Level world){
        markMap.put(world.dimension(), new MarkSave(position, direction));
    }

    /**
     * @return Is there a mark from the player in this dimension?
     */
    public boolean hasValidMark(Level world){
        return (markMap.get(world.dimension()) != null);
    }

    /**
     * @return Mark of this dimension from the player
     */
    @Nullable
    public MarkSave getMark(Level world){
        return markMap.get(world.dimension());
    }

    public void copyFrom(PlayerDragonMagic source, Player player) {
        this.markMap = source.getMarkMap();
        for(Map.Entry<String, Pair<ArmorUpgrade, Integer>> entry : onEventUpgrade.entrySet()) {
            addUpgrade(entry.getKey(), entry.getValue().getA(), entry.getValue().getB(), player);
        }
        for(Map.Entry<String,  Pair<IArmorUpgradeOnTick, Integer>> entry : onTickUpgrade.entrySet()) {
            addUpgrade(entry.getKey(), entry.getValue().getA(), entry.getValue().getB(), player);
        }
        for(Map.Entry<String, Pair<IArmorUpgradeOnEquipped, Integer>> entry : onEquipUpgrade.entrySet()) {
            addUpgrade(entry.getKey(), entry.getValue().getA(), entry.getValue().getB(), player);
        }
    }

    public HashMap<ResourceKey<Level>, MarkSave> getMarkMap() {
        return markMap;
    }

    public void saveNBT(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();
        int upgradeSize = 0;
        for(Map.Entry<String, Pair<ArmorUpgrade, Integer>> entry : onEventUpgrade.entrySet()) {
            nbt.putString("upgrade_source_" + upgradeSize, entry.getKey());
            nbt.putString("upgrade_value_" + upgradeSize, entry.getValue().getA().getRegistryName().toString());
            nbt.putInt("upgrade_int_" + upgradeSize, entry.getValue().getB());
            upgradeSize++;
        }
        for(Map.Entry<String, Pair<IArmorUpgradeOnTick, Integer>> entry : onTickUpgrade.entrySet()) {
            nbt.putString("upgrade_source_" + upgradeSize, entry.getKey());
            nbt.putString("upgrade_value_" + upgradeSize, entry.getValue().getA().getRegistryName().toString());
            nbt.putInt("upgrade_int_" + upgradeSize, entry.getValue().getB());
            upgradeSize++;
        }
        for(Map.Entry<String, Pair<IArmorUpgradeOnEquipped, Integer>> entry : onEquipUpgrade.entrySet()) {
            nbt.putString("upgrade_source_" + upgradeSize, entry.getKey());
            nbt.putString("upgrade_value_" + upgradeSize, entry.getValue().getA().getRegistryName().toString());
            nbt.putInt("upgrade_int_" + upgradeSize, entry.getValue().getB());
            upgradeSize++;
        }
        nbt.putInt("upgrades_size", upgradeSize);

        nbt.putInt("mark_map_size", markMap.size());
        int i = 0;
        for(Map.Entry<ResourceKey<Level>, MarkSave> entry : markMap.entrySet()) {
            nbt.putString("mark_map_dimension_" + i, entry.getKey().location().toString());
            nbt.put("mark_map_mark_save_" + i, entry.getValue().saveNBT());
            i++;
        }

        compound.put("dragon_magic_data", nbt);
    }

    public void loadNBT(CompoundTag compound) {
        //todo here
        if (compound.contains("dragon_magic_data")) {
            CompoundTag nbt = compound.getCompound("dragon_magic_data");

            for(int i = 0; i< nbt.getInt("upgrades_size"); i++){
                addUpgrade(
                        nbt.getString("upgrade_source_" + i),
                        Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(nbt.getString("upgrade_value_" + i))),
                        nbt.getInt("upgrade_int_" + i)
                );
            }

            if(nbt.contains("mark_map_size")){
                for(int i = 0; i< nbt.getInt("mark_map_size"); i++){
                    markMap.put(
                            ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("mark_map_dimension_" + i))),
                            new MarkSave(nbt.getCompound("mark_map_mark_save_" + i))
                    );
                }
            }
        }
    }

    public void addUpgrade(String source, @Nullable ArmorUpgrade armorUpgrade, int level, Player player){
        if(armorUpgrade != null){
            if(armorUpgrade instanceof IArmorUpgradeOnEquipped){
                if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                    ((IArmorUpgradeOnEquipped) armorUpgrade).onEquip(player, level);
                }
            }
            addUpgrade(source, armorUpgrade, level);
        }
    }

    public void removeUpgrade(String source, Player player){
        Pair<IArmorUpgradeOnTick, Integer> armorUpgrade0 = onTickUpgrade.remove(source);
        if(armorUpgrade0 != null){
            armorUpgrade0.getA().onRemove(player);
        }
        Pair<IArmorUpgradeOnEquipped, Integer> armorUpgrade1 = onEquipUpgrade.remove(source);
        if(armorUpgrade1 != null){
            armorUpgrade1.getA().onRemove(player);
        }
        Pair<ArmorUpgrade, Integer> armorUpgrade2 = onEventUpgrade.remove(source);
        if(armorUpgrade2 != null){
            armorUpgrade2.getA().onRemove(player);
        }
    }

    private void addUpgrade(String source, @Nullable ArmorUpgrade armorUpgrade, int level){
        if(armorUpgrade instanceof IArmorUpgradeOnTick){
            onTickUpgrade.put(source, new Pair<>((IArmorUpgradeOnTick)armorUpgrade, level));
        }
        else if(armorUpgrade instanceof IArmorUpgradeOnEquipped){
            onEquipUpgrade.put(source, new Pair<>((IArmorUpgradeOnEquipped) armorUpgrade, level));
        }
        else if (armorUpgrade != null){
            onEventUpgrade.put(source, new Pair<>(armorUpgrade, level));
        }
    }
}

