package org.codeberg.chromatic.freelook.util;

public interface CameraStateHandler {
    float freelook$getPitch();
    float freelook$getYaw();

    void freelook$setPitch(float xRot);
    void freelook$setYaw(float yRot);
}
