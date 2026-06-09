package org.codeberg.chromatic.freelook.option;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.Property;
import org.polyfrost.oneconfig.api.config.v1.annotations.*;

public class FreelookConfig extends Config {
    @Dropdown(
            title = "Perspective Type",
            options = { "First Person", "Third Person (Back)", "Third Person (Front)" }
    )
    public static int perspectiveType = 1;

    @Dropdown(
            title = "Press Mode",
            options = { "Hold", "Quick Press", "Toggle" }
    )
    public static int pressMode = 1;

    @Slider(
            title = "Hold Threshold",
            min = 50f, max = 1000f, step = 50f
    )
    public static float holdThreshold = 300f;

//    @Switch(
//            title = "Add to Camera Cycle"
//    )
//    public static boolean addToCameraCycle = false;

    @Dropdown(
            title = "On Cycle Change",
            options = { "Cycle and Freelook", "Stop Freelook", "Block Camera Cycle" }
    )
    public static int onCycleChange = 1;

    @Switch(
            title = "Smooth Camera"
    )
    public static boolean smoothCamera = true;

    @Accordion(
            title = "Pitch"
    )
    public static class Pitch {
        @Switch(
                title = "Enabled"
        )
        public static boolean enabled = true;

        @Switch(
                title = "Invert"
        )
        public static boolean invert = false;

        @Switch(
                title = "Lock"
        )
        public static boolean lock = true;
    }

    @Accordion(
            title = "Yaw"
    )
    public static class Yaw {
        @Switch(
                title = "Enabled"
        )
        public static boolean enabled = true;

        @Switch(
                title = "Invert"
        )
        public static boolean invert = false;
    }

    static {
        new FreelookConfig(); // init
    }

    public FreelookConfig() {
        super("freelook.json", "Freelook", Category.QOL);

        addDependency("Pitch.invert", "Pitch.enabled");
        addDependency("Pitch.lock", "Pitch.enabled");

        addDependency("Yaw.invert", "Yaw.enabled");

        addDependency("holdThreshold", "pressMode", () -> pressMode != 1 ? Property.Display.DISABLED : Property.Display.SHOWN);
    }
}
