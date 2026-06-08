package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
abstract class MixinEntityRenderer {
    @ModifyArg(
            method = "orientCamera",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 2),
            index = 2
    )
    private float modifyZArg(float z) {
        return FreelookHandler.INSTANCE.applySmoothScale(z);

    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F", opcode = Opcodes.GETFIELD))
    private float rotationYawModifier(Entity entity) {
        if (entity instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) entity;

            return tracker.freelook$getYaw();
        }

        return entity.rotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F", opcode = Opcodes.GETFIELD))
    private float prevRotationYawModifier(Entity entity) {
        if (entity instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) entity;

            return tracker.freelook$getYaw();
        }

        return entity.prevRotationYaw;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F", opcode = Opcodes.GETFIELD))
    private float rotationPitchModifier(Entity entity) {
        if (entity instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) entity;

            return tracker.freelook$getPitch();
        }

        return entity.rotationPitch;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F", opcode = Opcodes.GETFIELD))
    private float prevRotationPitchModifier(Entity entity) {
        if (entity instanceof CameraStateTracker && FreelookHandler.INSTANCE.freelookToggled) {
            CameraStateTracker tracker = (CameraStateTracker) entity;

            return tracker.freelook$getPitch();
        }

        return entity.prevRotationPitch;
    }
}
