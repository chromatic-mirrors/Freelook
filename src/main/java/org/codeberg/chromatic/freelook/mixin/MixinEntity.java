package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.option.FreelookOptions;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity implements CameraStateHandler {

    @Unique
    private float pitch, yaw;

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void rotate(double xo, double yo, CallbackInfo ci) {
        if (FreelookHandler.INSTANCE.perspectiveToggled) {
            if ((Object) this instanceof LocalPlayer player) {
                boolean snaplook = FreelookOptions.snaplook;

                float yawDelta = (float) (xo * 0.15);
                float pitchDelta = (float) (yo * 0.15);

                if (FreelookOptions.PITCH.enabled) {
                    float cameraPitch = FreelookHandler.updateCameraValue(
                            freelook$getPitch(),
                            pitchDelta,
                            FreelookOptions.PITCH.invert,
                            FreelookOptions.PITCH.lock
                    );
                    freelook$setPitch(snaplook ? player.getXRot() : cameraPitch);
                }

                if (FreelookOptions.YAW.enabled) {
                    float cameraYaw = FreelookHandler.updateCameraValue(
                            freelook$getYaw(),
                            yawDelta,
                            FreelookOptions.YAW.invert,
                            FreelookOptions.YAW.lock
                    );
                    freelook$setYaw(snaplook ? player.getYRot() : cameraYaw);
                }

                if (!snaplook) {
                    ci.cancel();
                }
            }
        }
    }

    @Override
    public float freelook$getPitch() {
        return pitch;
    }

    @Override
    public float freelook$getYaw() {
        return yaw;
    }

    @Override
    public void freelook$setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void freelook$setYaw(float yaw) {
        this.yaw = yaw;
    }
}
