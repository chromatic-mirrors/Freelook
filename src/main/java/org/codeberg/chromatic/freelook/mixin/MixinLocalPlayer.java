package org.codeberg.chromatic.freelook.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer implements CameraStateTracker {

    @Unique
    private float pitch, yaw;

    @Override
    public float freelook$getPitch() {
        return pitch;
    }

    @Override
    public float freelook$getYaw() {
        return yaw;
    }

    @Override
    public void freelook$setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void freelook$setYaw(float yaw) {
        this.yaw = yaw;
    }

}
