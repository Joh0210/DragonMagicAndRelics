package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import de.joh.dmnr.client.item.RiftEmitterItemRenderer;
import de.joh.dmnr.common.block.RiftEmitterBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/**
 * Matching item for the Rift Emitter (Block)
 * @see RiftEmitterBlock
 * @author Joh0210
 */
public class RiftEmitterItem extends BlockItem implements ITieredItem<RiftEmitterItem> {
    private int _tier = -1;

    public RiftEmitterItem(Block block, Properties settings) {
        super(block, settings);
    }

    /**
     * The item shows the block model together with the animated parts of the RiftEmitterRenderer.
     */
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private RiftEmitterItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new RiftEmitterItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }
}
