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

    public static int getCurrentPerspectiveIndex() {
        return getCurrentPerspective().ordinal();
    }

    public static CameraType getLastPerspectiveType() {
        CameraType[] values = CameraType.values();
        return values[values.length - 1];
    }

    /**
     * @return The maximum perspective index
     */
    public static int getMaximumPerspectiveIndex() {
        return CameraType.values().length - 1;
    }
}
