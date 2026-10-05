package org.codeberg.chromatic.freelook.mixin.legacy;

//? if 1.8.9 {
/*import net.minecraft.client.render.GameRenderer;
import net.minecraft.world.entity.Entity;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Redirect(
            method = "transformCamera",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;yaw:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private float modifyYaw(Entity entity) {
        return FreelookHandler.INSTANCE.cameraYaw(entity, entity.yaw);
    }

    @Redirect(
            method = "transformCamera",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;lastYaw:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private float modifyLastYaw(Entity entity) {
        return FreelookHandler.INSTANCE.cameraYaw(entity, entity.lastYaw);
    }

    @Redirect(
            method = "transformCamera",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;pitch:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private float modifyPitch(Entity entity) {
        return FreelookHandler.INSTANCE.cameraPitch(entity, entity.pitch);
    }

    @Redirect(
            method = "transformCamera",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;lastPitch:F",
                    opcode = Opcodes.GETFIELD
            )
    )
    private float modifyLastPitch(Entity entity) {
        return FreelookHandler.INSTANCE.cameraPitch(entity, entity.lastPitch);
    }

    @ModifyArg(
            method = "transformCamera",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/platform/GlStateManager;translatef(FFF)V",
                    ordinal = 2
            ),
            index = 2
    )
    private float adjustCameraDistance(float z) {
        return FreelookHandler.INSTANCE.applySmoothScale(z);
    }

}
*///?}
