package de.joh.dragonmagicandrelics.item.items.client;

import de.joh.dragonmagicandrelics.item.items.RiftEmitterItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RiftEmitterItemRenderer extends GeoItemRenderer<RiftEmitterItem> {
    public RiftEmitterItemRenderer() {
        super(new RiftEmitterItemModel());
    }
}