package org.codeberg.chromatic.freelook.option;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.Property;
import org.polyfrost.oneconfig.api.config.v1.annotations.Checkbox;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;

public class FreelookConfig extends Config {
    @Dropdown(
            title = "Perspective Type",
            options = { "First Person", "Third Person (Back)", "Third Person (Front)" }
    )
    public int perspectiveType = 1;

    @Dropdown(
            title = "Press Mode",
            options = { "Hold", "Quick Press", "Toggle" }
    )
    public int pressMode = 1;

    @Slider(
            title = "Hold Threshold",
            min = 50f, max = 1000f, step = 50f
    )
    public float holdThreshold = 300f;

    @Checkbox(
            title = "Add to Camera Cycle"
    )
    public boolean addToCameraCycle = false;

    @Dropdown(
            title = "On Cycle Change",
            options = { "Cycle and Freelook", "Stop Freelook", "Block Camera Cycle" }
    )
    public int onCycleChange = 1;

    @Checkbox(
            title = "Smooth Camera"
    )
    public boolean smoothCamera = false;

    @Checkbox(
            title = "Enabled",
            subcategory = "Pitch"
    )
    public boolean pitchEnabled = true;

    @Checkbox(
            title = "Invert",
            subcategory = "Pitch"
    )
    public boolean pitchInvert = false;

    @Checkbox(
            title = "Lock",
            subcategory = "Pitch"
    )
    public boolean pitchLock = false;

    @Checkbox(
            title = "Enabled",
            subcategory = "Yaw"
    )
    public boolean yawEnabled = true;

    @Checkbox(
            title = "Invert",
            subcategory = "Yaw"
    )
    public boolean yawInvert = false;

    public FreelookConfig() {
        super("freelook.json", "Freelook", Category.QOL);

        addDependency("pitchInvert", "pitchEnabled");
        addDependency("pitchLock", "pitchEnabled");

        addDependency("yawInvert", "yawEnabled");

        addDependency("holdThreshold", "Quick Press", () -> pressMode != 1 ? Property.Display.DISABLED : Property.Display.SHOWN);
    }
}
