package org.codeberg.chromatic.freelook.handler;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import org.codeberg.chromatic.freelook.Freelook;

public class CameraCycleHandler {
    public static boolean hasCycledFreelook = false;

    public static boolean shouldOverrideCameraCycle() {
        switch (Freelook.config().onCycleChange) {
            case STOP_FREELOOK -> FreelookHandler.INSTANCE.freelookToggled = false;
            case BLOCK_PERSPECTIVE_CHANGE -> {
                if (FreelookHandler.INSTANCE.freelookToggled && !hasCycledFreelook) {
                    return false;
                }
            }
            case CHANGE_AND_FREELOOK -> {}
        }

        if (Freelook.config().addToCameraCycle) {
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
