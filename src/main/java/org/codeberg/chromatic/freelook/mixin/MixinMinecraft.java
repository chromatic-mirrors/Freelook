package org.codeberg.chromatic.freelook.mixin;

import org.codeberg.chromatic.freelook.handler.CameraCycleHandler;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @WrapWithCondition(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Options;setCameraType(Lnet/minecraft/client/CameraType;)V"
            )
    )
    private boolean overrideCameraCycle(Options instance, CameraType cameraType) {
        return CameraCycleHandler.shouldOverrideCameraCycle();
    }

    @Inject(
            method = "tick", at = @At("TAIL")
    )
    private void meow(CallbackInfo ci) {
        FreelookHandler.INSTANCE.tick();
    }
}
