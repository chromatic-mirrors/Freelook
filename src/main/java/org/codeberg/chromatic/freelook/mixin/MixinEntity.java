package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.entity.Entity;
import org.codeberg.chromatic.freelook.Freelook;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
abstract class MixinEntity {
    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    public void rotate(float yaw, float pitch, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.freelookToggled && this instanceof CameraStateTracker) {
            CameraStateTracker tracker = (CameraStateTracker) this;

            if (Freelook.config().pitchEnabled) tracker.freelook$setPitch(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getPitch(), pitch, Freelook.config().invertPitch, Freelook.config().lockPitch
                    )
            );

            if (Freelook.config().yawEnabled) tracker.freelook$setYaw(
                    FreelookHandler.calculateCameraRotation(
                            // yaw is inverted by default in < 1.12.2 so we reflect that here (s/o tellinq)
                            tracker.freelook$getYaw(), yaw, !Freelook.config().invertYaw, false
                    )
            );

            ci.cancel();
        }
    }
}
