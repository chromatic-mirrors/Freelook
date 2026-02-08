package org.codeberg.chromatic.freelook.option;

public class FreelookOptions {
    public enum ChangePerspective {
        NEVER,
        FIRST_PERSON_ONLY,
        THIRD_PERSON_ONLY,
        ALWAYS
    }

    public enum PerspectiveMode {
        FIRST_PERSON,
        THIRD_PERSON,
        THIRD_PERSON_REVERSED
    }

    public enum PressMode {
        HOLD,
        QUICK_PRESS,
        TOGGLE
    }

    public enum CycleChangeAction {
        CHANGE_AND_FREELOOK,
        STOP_FREELOOK,
        BLOCK_PERSPECTIVE_CHANGE
    }

    // "Which perspective should make Freelook/Snaplook start in a different perspective"
    public static ChangePerspective changePerspective = ChangePerspective.FIRST_PERSON_ONLY;

    // "Which camera perspective to start if changed"
    public static PerspectiveMode perspectiveMode = PerspectiveMode.THIRD_PERSON;

    public static class Activation {
        // "Whether you can hold, quick press, or toggle to activate"
        public static PressMode pressMode = PressMode.QUICK_PRESS;
        // "How long you can hold before Freelook/Snaplook is disabled upon release"
        public static long holdThreshold = 300;
    }

    public static class MovementConfig {
        public boolean enabled, invert, lock;

        public MovementConfig(boolean enabled, boolean invert, boolean lock) {
            this.enabled = enabled;
            this.invert = invert;
            this.lock = lock;
        }
    }

    public static final MovementConfig PITCH = new MovementConfig(true, false, true);
    public static final MovementConfig YAW = new MovementConfig(true, false, false);

    // "Add Freelook/Snaplook as part of the perspective cycle"
    public static boolean addToCameraCycle = false;

    // 0 = Change & Freelook, 1 = Stop Freelook, 3 = Block Perspective Change
    public static CycleChangeAction onCycleChange = CycleChangeAction.STOP_FREELOOK;

    // "Animate third person when Freelook/Snaplook is enabled"
    public static boolean smoothCamera = false;

    // "Use a vanilla perspective instead."
    public static boolean snaplook = false;
}
