package org.codeberg.chromatic.freelook.util;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class CameraController {
    public static CameraType activePerspective = null;

    public static void set(CameraType type) {
        if (Minecraft.getInstance().options.getCameraType() != type) {
            activePerspective = type;
            Minecraft.getInstance().options.setCameraType(type);
        }
    }

    public static void restore(CameraType type) {
        activePerspective = type;
        Minecraft.getInstance().options.setCameraType(type);
    }
}

