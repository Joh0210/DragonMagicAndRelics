package de.joh.dmnr.client.item.armor;

import de.joh.dmnr.api.item.DragonMageArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

/**
 * GeckoLib renderer for DragonMageArmor.
 * <br> This code is generic.
 * @author Joh021
 */
public class DragonMageArmorRenderer extends GeoArmorRenderer<DragonMageArmorItem> {

    public DragonMageArmorRenderer() {
        super(new DragonMageArmorModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.leftLegBone = "armorLeftLeg";
        this.rightLegBone = "armorRightLeg";
        this.leftBootBone = "armorLeftBoot";
        this.rightBootBone = "armorRightBoot";
    }
}
