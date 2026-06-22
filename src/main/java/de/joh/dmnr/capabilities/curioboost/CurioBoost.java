package de.joh.dmnr.capabilities.curioboost;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class CurioBoost {
    private boolean enabled = false;
    private final Set<String> blacklist = new HashSet<>();

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void blacklistID(String element) {
        this.blacklist.add(element);
    }

    public void whitelistID(String element) {
        this.blacklist.remove(element);
    }

    public boolean isBlacklisted(String element) {
        return this.blacklist.contains(element);
    }

    public void copyFrom(CurioBoost source, Player player) {
        this.blacklist.clear();
        this.blacklist.addAll(source.blacklist);
        this.enabled = source.enabled;
    }

    public void saveNBT(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("enabled", this.enabled);

        ListTag listTag = new ListTag();
        for (String s : blacklist) {
            listTag.add(StringTag.valueOf(s));
        }
        nbt.put("blacklist", listTag);

        compound.put("curio_boost_data", nbt);
    }

    public void loadNBT(CompoundTag compound) {
        if (compound.contains("curio_boost_data")) {
            CompoundTag nbt = compound.getCompound("curio_boost_data");
            this.enabled = nbt.getBoolean("enabled");

            this.blacklist.clear();
            if (nbt.contains("blacklist", Tag.TAG_LIST)) {
                ListTag listTag = nbt.getList("blacklist", Tag.TAG_STRING);
                for (Tag tag : listTag) {
                    this.blacklist.add(tag.getAsString());
                }
            }
        }
    }
}
