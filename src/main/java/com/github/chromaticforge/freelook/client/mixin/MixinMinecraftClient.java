package com.github.chromaticforge.freelook.client.mixin;

import com.github.chromaticforge.freelook.client.CameraCycleHandler;
import com.github.chromaticforge.freelook.client.FreelookController;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @WrapWithCondition(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V"
            )
    )
    private boolean overrideCameraCycle(GameOptions instance, Perspective cameraType) {
        return CameraCycleHandler.shouldOverrideCameraCycle();
    }

    @Inject(
            method = "tick", at = @At("TAIL")
    )
    private void meow(CallbackInfo ci) {
        FreelookController.INSTANCE.tick();
    }
}
