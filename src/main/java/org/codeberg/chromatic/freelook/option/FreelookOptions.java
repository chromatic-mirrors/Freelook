package org.codeberg.chromatic.freelook.option;

import net.minecraft.client.CameraType;

public class FreelookOptions {
    // "Which camera perspective to start if changed"
    public static PerspectiveTypes perspectiveType = PerspectiveTypes.THIRD_PERSON_BACK;

    // "Whether you can hold, quick press, or toggle to activate"
    public static PressMode pressMode = PressMode.QUICK_PRESS;

    // "How long you can hold before Freelook is disabled upon release"
    public static long holdThreshold = 300;

    public static final MovementConfig PITCH = new MovementConfig(true, false, true);
    public static final MovementConfig YAW = new MovementConfig(true, false, false);

    // "Add Freelook/Snaplook as part of the perspective cycle"
    public static boolean addToCameraCycle = false;

    // 0 = Change & Freelook, 1 = Stop Freelook, 3 = Block Perspective Change
    public static CycleChangeAction onCycleChange = CycleChangeAction.STOP_FREELOOK;

    // "Animate third person when Freelook/Snaplook is enabled"
    public static boolean smoothCamera = false;

    public enum PerspectiveTypes {
        FIRST_PERSON,
        THIRD_PERSON_BACK,
        THIRD_PERSON_FRONT;

        public CameraType asCameraType() {
            return switch (this) {
                case FIRST_PERSON -> CameraType.FIRST_PERSON;
                case THIRD_PERSON_BACK -> CameraType.THIRD_PERSON_BACK;
                case THIRD_PERSON_FRONT -> CameraType.THIRD_PERSON_FRONT;
            };
        }
    }

    public enum PressMode {
        HOLD,
        QUICK_PRESS,
        TOGGLE
    }

    public static class MovementConfig {
        public boolean enabled, invert, lock;

        public MovementConfig(boolean enabled, boolean invert, boolean lock) {
            this.enabled = enabled;
            this.invert = invert;
            this.lock = lock;
        }
    }

    public enum CycleChangeAction {
        CHANGE_AND_FREELOOK,
        STOP_FREELOOK,
        BLOCK_PERSPECTIVE_CHANGE
    }
}
