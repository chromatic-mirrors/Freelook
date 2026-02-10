package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.Freelook;
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
            at = @At("HEAD"),
            cancellable = true
    )
    public void rotate(double yaw, double pitch, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.perspectiveToggled && this instanceof CameraStateTracker tracker) {
            if (Freelook.config().pitchEnabled) tracker.freelook$setPitch(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getPitch(), pitch, Freelook.config().invertPitch, Freelook.config().lockPitch
                    )
            );

            if (Freelook.config().yawEnabled) tracker.freelook$setYaw(
                    FreelookHandler.calculateCameraRotation(
                            tracker.freelook$getYaw(), yaw, Freelook.config().invertYaw, false
                    )
            );

            ci.cancel();
        }
    }

}
