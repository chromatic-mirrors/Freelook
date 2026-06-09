package org.codeberg.chromatic.freelook.handler;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import org.codeberg.chromatic.freelook.option.FreelookConfig;

public class CameraCycleHandler {
    public static boolean hasCycledFreelook = false;

    public static boolean shouldOverrideCameraCycle() {
        switch (FreelookConfig.onCycleChange) {
            case 1 -> FreelookHandler.INSTANCE.freelookToggled = false;
            case 2 -> {
                if (FreelookHandler.INSTANCE.freelookToggled && !hasCycledFreelook) {
                    return false;
                }
            }
            case 0 -> {}
        }

        if (FreelookConfig.addToCameraCycle) {
            if (hasCycledFreelook) {
                hasCycledFreelook = false;
                FreelookHandler.INSTANCE.stop();
            } else if (Minecraft.getInstance().options.getCameraType() == getLastPerspectiveType()) {
                FreelookHandler.INSTANCE.start();
                hasCycledFreelook = true;
                return false;
            }
        }

        return true;
    }

    public static CameraType getLastPerspectiveType() {
        CameraType[] values = CameraType.values();
        return values[values.length - 1];
    }
}
