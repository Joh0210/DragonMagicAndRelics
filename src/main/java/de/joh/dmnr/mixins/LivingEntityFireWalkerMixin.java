package de.joh.dmnr.mixins;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class LivingEntityFireWalkerMixin {
    public LivingEntityFireWalkerMixin() {
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"canStandOnFluid"},
            cancellable = true
    )
    public void dmnr$canStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
//        LivingEntity self = (LivingEntity) (Object) this;
//        if (state.getTags().anyMatch((t) -> t == FluidTags.LAVA) && self instanceof Player && (
//                            (!self.getLevel().isClientSide() && ArmorUpgradeHelper.getUpgradeLevel((Player) self, ArmorUpgradeInit.BURNING_FRENZY) >= 1)
//                            ||
//                            (self.getLevel().isClientSide() && ClientPlayerDragonMagic.hasBurningFrenzy())
//                    )) {
//            cir.setReturnValue(!self.isCrouching());
//        }
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"isAffectedByFluids"},
            cancellable = true
    )
    public void dmnr$isAffectedByFluids(CallbackInfoReturnable<Boolean> cir) {
//        LivingEntity self = (LivingEntity) (Object) this;
//        if (self.isInLava() && self instanceof Player && (
//                            (!self.getLevel().isClientSide() && ArmorUpgradeHelper.getUpgradeLevel((Player) self, ArmorUpgradeInit.BURNING_FRENZY) >= 1)
//                            ||
//                            (self.getLevel().isClientSide() && ClientPlayerDragonMagic.hasBurningFrenzy())
//                    )) {
//            cir.setReturnValue(!self.isInLava());
//        }

    }

    @Inject(
            at = {@At("HEAD")},
            method = {"jumpInLiquid"},
            cancellable = true
    )
    public void dmnr$jumpInLiquid(TagKey<Fluid> fluid, CallbackInfo ci) {
        //todo: fix -> Cannot Jump in Lava

//        if (fluid == FluidTags.LAVA) {
//            LivingEntity self = (LivingEntity) (Object) this;
//            if (self instanceof Player && (
//                            (!self.getLevel().isClientSide() && ArmorUpgradeHelper.getUpgradeLevel((Player) self, ArmorUpgradeInit.BURNING_FRENZY) >= 1)
//                            ||
//                            (self.getLevel().isClientSide() && ClientPlayerDragonMagic.hasBurningFrenzy())
//                    )) {
//                ((Player)self).jumpFromGround();
//                ci.cancel();
//            }
//        }
    }
}
