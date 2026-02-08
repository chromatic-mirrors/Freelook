package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
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

        if (FreelookHandler.INSTANCE.perspectiveToggled && focused instanceof LocalPlayer) {
            args.set(0, CameraStateTracker.getCameraYaw(focused));
            args.set(1, CameraStateTracker.getCameraPitch(focused));
        }
    }

    @ModifyReturnValue(method = "getMaxZoom", at = @At("RETURN"))
    private float adjustClipReturn(float original) {
        return FreelookHandler.INSTANCE.applySmoothScale(original);
    }
}
