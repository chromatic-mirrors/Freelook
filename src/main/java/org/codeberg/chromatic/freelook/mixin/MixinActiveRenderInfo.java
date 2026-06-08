package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ActiveRenderInfo.class)
abstract class MixinActiveRenderInfo {
    @Redirect(
            method = "updateRenderInfo",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;rotationPitch:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private static float modifyPitch(EntityPlayer player) {
        if (player instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) player;

            return tracker.freelook$getPitch();
        }

        return player.rotationPitch;
    }

    @Redirect(
            method = "updateRenderInfo",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;rotationYaw:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private static float modifyYaw(EntityPlayer player) {
        if (player instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) player;

            return tracker.freelook$getYaw();
        }

        return player.rotationYaw;
    }
}
