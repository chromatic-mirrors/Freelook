package org.codeberg.chromatic.freelook.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Camera;
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
    private void modifyRotationArgs(Args args, @Local(argsOnly = true) Entity entity) {
        if (entity instanceof LocalPlayer player) {
            CameraStateHandler handler = (CameraStateHandler) player;

            if (FreelookHandler.INSTANCE.perspectiveToggled) {
                args.set(0, handler.freelook$getYaw());
                args.set(1, handler.freelook$getPitch());
            } else {
                handler.freelook$setYaw(args.get(0));
                handler.freelook$setPitch(args.get(1));
            }
        }
    }

    @ModifyReturnValue(method = "getMaxZoom", at = @At("RETURN"))
    private float adjustClipReturn(float original) {
        return FreelookHandler.INSTANCE.applySmoothScale(original);
    }

}
