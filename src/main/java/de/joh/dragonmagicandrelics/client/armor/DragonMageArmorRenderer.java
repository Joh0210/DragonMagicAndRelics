package de.joh.dragonmagicandrelics.client.armor;

import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class DragonMageArmorRenderer extends GeoArmorRenderer<DragonMageArmor> {

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
