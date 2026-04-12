package org.codeberg.chromatic.freelook.handler;

import org.codeberg.chromatic.freelook.Freelook;
import org.codeberg.chromatic.freelook.util.CameraController;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.codeberg.chromatic.freelook.util.EasingProgressTimer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.CameraType;
import net.minecraft.util.Mth;

public class FreelookHandler {
    public static final FreelookHandler INSTANCE = new FreelookHandler();

    public boolean freelookToggled = false;
    CameraType lastPerspective = CameraType.FIRST_PERSON;
    private long pressStartTime = 0;
    final EasingProgressTimer timer = new EasingProgressTimer(650L);

    public void tick(KeyMapping key) {
        switch (Freelook.config().pressMode) {
            case QUICK_PRESS -> handlePressAndHold(key.isDown());
            case TOGGLE -> {
                if (key.consumeClick()) toggle();
            }
            case HOLD -> {
                if (key.isDown()) {
                    start();
                } else {
                    stop();
                }
            }
        }
    }

    public void handlePressAndHold(boolean pressed) {
        if (pressed && pressStartTime == 0L) {
            pressStartTime = System.currentTimeMillis();
            toggle();
        } else if (!pressed && pressStartTime != 0L) {
            long pressDuration = System.currentTimeMillis() - pressStartTime;
            pressStartTime = 0L;

            if (pressDuration > Freelook.config().holdThreshold) {
                stop();
            }
        }
    }

    public void toggle() {
        if (freelookToggled) {
            stop();
        } else {
            start();
        }
    }

    public void start() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (freelookToggled) return;

        lastPerspective = Minecraft.getInstance().options.getCameraType();

        CameraController.set(Freelook.config().perspectiveType.asCameraType());

        timer.start();

        if (player instanceof CameraStateTracker tracker) {
            tracker.freelook$setPitch(player.getXRot());
            tracker.freelook$setYaw(player.getYRot());
        }

        freelookToggled = true;
    }

    public void stop() {
        if (!freelookToggled) return;

        freelookToggled = false;
        CameraCycleHandler.hasCycledFreelook = false;
        CameraController.restore(lastPerspective);
        timer.stop();
    }

    public float applySmoothScale(float z) {
        if (!freelookToggled || timer.isComplete() || !Freelook.config().smoothCamera) return z;

        float transitionProgress = timer.getProgress();
        float scale = 0.125f + transitionProgress * (1.0f - 0.125f);
        return z * scale;
    }

    public static float calculateCameraRotation(float currentValue, double delta, boolean invert, boolean lock) {
        delta = (delta * 0.15F * (invert ? -1 : 1));
        float rotation = currentValue + (float) delta;
        return lock ? Mth.clamp(rotation, -90.0F, 90.0F) : rotation;
    }
}
