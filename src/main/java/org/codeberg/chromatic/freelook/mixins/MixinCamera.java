package org.codeberg.chromatic.freelook.mixins;

import com.github.chromaticforge.freelook.client.CameraStateTracker;
import com.github.chromaticforge.freelook.client.FreelookController;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @ModifyArgs(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setRotation(FF)V",
                    ordinal = 1
            )
    )
    private void modifyRotationArgs(Args args) {
        LocalPlayer focused = Minecraft.getInstance().player;

        if (FreelookController.perspectiveToggled && focused instanceof LocalPlayer) {
            CameraStateTracker tracker = CameraStateTracker.INSTANCE;

            args.set(0, tracker.getCameraYaw(focused));
            args.set(1, tracker.getCameraPitch(focused));
        }
    }

    @ModifyReturnValue(method = "getMaxZoom", at = @At("RETURN"))
    private float adjustClipReturn(float original) {
        return FreelookController.INSTANCE.applySmoothScale(original);
    }
}
