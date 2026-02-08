package org.codeberg.chromatic.freelook.handler;

import org.codeberg.chromatic.freelook.option.FreelookOptions;
import org.codeberg.chromatic.freelook.util.PerspectiveManager;

public class CameraCycleHandler {
    public static boolean hasCycledFreelook = false;

    public static boolean shouldOverrideCameraCycle() {
        switch (FreelookOptions.onCycleChange) {
            case STOP_FREELOOK -> FreelookHandler.INSTANCE.perspectiveToggled = false;
            case BLOCK_PERSPECTIVE_CHANGE -> {
                if (FreelookHandler.INSTANCE.perspectiveToggled && !hasCycledFreelook) {
                    return false;
                }
            }
            case CHANGE_AND_FREELOOK -> {}
        }

        if (FreelookOptions.addToCameraCycle) {
            if (hasCycledFreelook) {
                hasCycledFreelook = false;
                FreelookHandler.INSTANCE.stop();
            } else if (PerspectiveManager.getCurrentPerspective() == PerspectiveManager.getLastPerspectiveType()) {
                FreelookHandler.INSTANCE.start();
                hasCycledFreelook = true;
                return false;
            }
        }

        return true;
    }
}
