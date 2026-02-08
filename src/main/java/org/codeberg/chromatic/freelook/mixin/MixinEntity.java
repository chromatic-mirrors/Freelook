package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.option.FreelookOptions;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void changeCameraLookDirection(double yaw, double pitch, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.perspectiveToggled && (Object) this instanceof LocalPlayer entity) {
            boolean snaplook = FreelookOptions.snaplook;

            float pitchDelta = (float) (pitch * 0.15);
            float yawDelta = (float) (yaw * 0.15);

            if (FreelookOptions.PITCH.enabled) {
                float cameraPitch = FreelookHandler.updateCameraValue(
                        CameraStateTracker.getCameraPitch(entity),
                        pitchDelta,
                        FreelookOptions.PITCH.invert,
                        FreelookOptions.PITCH.lock
                );
                CameraStateTracker.setCameraPitch(entity, snaplook ? entity.getYRot() : cameraPitch);
            }

            if (FreelookOptions.YAW.enabled) {
                float cameraYaw = FreelookHandler.updateCameraValue(
                        CameraStateTracker.getCameraYaw(entity),
                        yawDelta,
                        FreelookOptions.YAW.invert,
                        FreelookOptions.YAW.lock
                );
                CameraStateTracker.setCameraYaw(entity, snaplook ? entity.getXRot() : cameraYaw);
            }

            if (!snaplook) {
                ci.cancel();
            }
        }
    }

}
