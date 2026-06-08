package org.codeberg.chromatic.freelook.util;

import net.minecraft.client.Minecraft;

public class CameraController {
    public static int activePerspective = 0;

    public static void set(int type) {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != type) {
            activePerspective = type;
            Minecraft.getMinecraft().gameSettings.thirdPersonView = type;
        }
    }

    public static void restore(int type) {
        activePerspective = type;
        Minecraft.getMinecraft().gameSettings.thirdPersonView = type;
    }
}

