package de.joh.dmnr.client.item;

import de.joh.dmnr.common.item.RiftEmitterItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

/**
 * GeckoLib renderer for RiftEmitterItem.
 * <br> This code is generic.
 * @author Joh021
 */
public class RiftEmitterItemRenderer extends GeoItemRenderer<RiftEmitterItem> {
    public RiftEmitterItemRenderer() {
        super(new RiftEmitterItemModel());
    }
}