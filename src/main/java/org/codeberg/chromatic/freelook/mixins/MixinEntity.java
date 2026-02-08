package org.codeberg.chromatic.freelook.mixins;

import com.github.chromaticforge.freelook.client.CameraStateTracker;
import com.github.chromaticforge.freelook.client.FreelookController;
import com.github.chromaticforge.freelook.client.FreelookConfig;
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
        if (FreelookController.perspectiveToggled && (Object) this instanceof LocalPlayer entity) {
            boolean snaplook = FreelookConfig.INSTANCE.getSnaplook();

            float pitchDelta = (float) (pitch * 0.15);
            float yawDelta = (float) (yaw * 0.15);

            if (FreelookConfig.Pitch.INSTANCE.getEnabled()) {
                float cameraPitch = FreelookController.INSTANCE.updateCameraValue(
                        CameraStateTracker.INSTANCE.getCameraPitch(entity),
                        pitchDelta,
                        FreelookConfig.Pitch.INSTANCE.getInvert(),
                        FreelookConfig.Pitch.INSTANCE.getLock()
                );
                CameraStateTracker.INSTANCE.setCameraPitch(entity, snaplook ? entity.getYRot() : cameraPitch);
            }

            if (FreelookConfig.Yaw.INSTANCE.getEnabled()) {
                float cameraYaw = FreelookController.INSTANCE.updateCameraValue(
                        CameraStateTracker.INSTANCE.getCameraYaw(entity),
                        yawDelta,
                        FreelookConfig.Yaw.INSTANCE.getInvert(),
                        FreelookConfig.Yaw.INSTANCE.getLock()
                );
                CameraStateTracker.INSTANCE.setCameraYaw(entity, snaplook ? entity.getXRot() : cameraYaw);
            }

            if (!snaplook) {
                ci.cancel();
            }
        }
    }

}
