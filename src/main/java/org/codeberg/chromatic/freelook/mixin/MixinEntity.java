package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.option.FreelookOptions;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(
            method = "turn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setXRot(F)V"),
            cancellable = true
    )
    public void rotate(double yaw, double pitch, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.perspectiveToggled && this instanceof CameraStateTracker tracker) {
            if (FreelookOptions.PITCH.enabled) tracker.freelook$setPitch(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getPitch(), pitch, FreelookOptions.PITCH
                    )
            );

            if (FreelookOptions.YAW.enabled) tracker.freelook$setYaw(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getYaw(), yaw, FreelookOptions.YAW
                    )
            );

            ci.cancel();
        }
    }

}
