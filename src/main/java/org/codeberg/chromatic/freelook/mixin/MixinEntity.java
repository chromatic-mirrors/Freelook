package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
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
            at = @At("HEAD"),
            cancellable = true
    )
    public void rotate(double xo, double yo, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.freelookToggled && this instanceof CameraStateTracker tracker) {
            if (FreelookConfig.Pitch.enabled) tracker.freelook$setPitch(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getPitch(), yo, FreelookConfig.Pitch.invert, FreelookConfig.Pitch.lock
                    )
            );

            if (FreelookConfig.Yaw.enabled) tracker.freelook$setYaw(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getYaw(), xo, FreelookConfig.Yaw.invert, false
                    )
            );

            ci.cancel();
        }
    }

}
