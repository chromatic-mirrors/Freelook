package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.client.settings.GameSettings;
import org.codeberg.chromatic.freelook.handler.CameraCycleHandler;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
abstract class MixinMinecraft {
    @Redirect(
            method = "runTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    private void overrideCameraCycle(GameSettings instance, int cameraType) {
        if (CameraCycleHandler.shouldOverrideCameraCycle()) {
            instance.thirdPersonView = cameraType;
        }
    }
}
