package org.codeberg.chromatic.freelook.util;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class PerspectiveManager {
    public static void setPerspective(CameraType type) {
        Minecraft.getInstance().options.setCameraType(type);
    }

    public static CameraType getCurrentPerspective() {
        return Minecraft.getInstance().options.getCameraType();
    }

    public static CameraType getLastPerspectiveType() {
        CameraType[] values = CameraType.values();
        return values[values.length - 1];
    }
}
