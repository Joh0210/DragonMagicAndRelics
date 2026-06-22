package de.joh.dmnr.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class DragonMagicChangeEvent extends PlayerEvent {
    public final boolean enabling;
    public DragonMagicChangeEvent(Player player, boolean enabling) {
        super(player);
        this.enabling = enabling;
    }
}
