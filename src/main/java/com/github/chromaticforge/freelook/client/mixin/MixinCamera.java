package com.github.chromaticforge.freelook.client.mixin;

import com.github.chromaticforge.freelook.client.CameraStateTracker;
import com.github.chromaticforge.freelook.client.FreelookController;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @ModifyArgs(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    ordinal = 1
            )
    )
    private void modifyRotationArgs(Args args) {
        ClientPlayerEntity focused = MinecraftClient.getInstance().player;

        if (FreelookController.perspectiveToggled && focused instanceof ClientPlayerEntity) {
            CameraStateTracker tracker = CameraStateTracker.INSTANCE;

            args.set(0, tracker.getCameraYaw(focused));
            args.set(1, tracker.getCameraPitch(focused));
        }
    }

    @ModifyReturnValue(method = "clipToSpace", at = @At("RETURN"))
    private float adjustClipReturn(float original) {
        return FreelookController.INSTANCE.applySmoothScale(original);
    }
}
