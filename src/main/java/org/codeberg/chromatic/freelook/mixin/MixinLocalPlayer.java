package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayerSP.class)
abstract class MixinLocalPlayer implements CameraStateTracker {
    @Unique
    private float freelook$pitch, freelook$yaw;

    @Override
    public float freelook$getPitch() {
        return freelook$pitch;
    }

    @Override
    public float freelook$getYaw() {
        return freelook$yaw;
    }

    @Override
    public void freelook$setPitch(float pitch) {
        this.freelook$pitch = pitch;
    }

    @Override
    public void freelook$setYaw(float yaw) {
        this.freelook$yaw = yaw;
    }
}
