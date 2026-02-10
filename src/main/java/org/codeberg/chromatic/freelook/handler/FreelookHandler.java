package org.codeberg.chromatic.freelook.handler;

import org.codeberg.chromatic.freelook.Freelook;
import org.codeberg.chromatic.freelook.util.CameraStateTracker;
import org.codeberg.chromatic.freelook.util.PerspectiveManager;
import org.codeberg.chromatic.freelook.util.SmoothTransitionTimer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.CameraType;
import net.minecraft.util.Mth;

public class FreelookHandler {
    public static final FreelookHandler INSTANCE = new FreelookHandler();

    public boolean perspectiveToggled = false;
    private CameraType lastPerspective = CameraType.FIRST_PERSON;
    private long pressStartTime = 0;
    private final SmoothTransitionTimer timer = new SmoothTransitionTimer(650L);

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
        if (perspectiveToggled) {
            stop();
        } else {
            start();
        }
    }

    public void start() {
        if (perspectiveToggled) return;

        lastPerspective = PerspectiveManager.getCurrentPerspective();

        PerspectiveManager.setPerspective(Freelook.config().perspectiveType.asCameraType());

        if (Freelook.config().smoothCamera) timer.start();

        LocalPlayer player = Minecraft.getInstance().player;

        if (player instanceof CameraStateTracker tracker) {
            tracker.freelook$setPitch(player.getXRot());
            tracker.freelook$setYaw(player.getYRot());
        }

        perspectiveToggled = true;
    }

    public void stop() {
        if (!perspectiveToggled) return;

        perspectiveToggled = false;
        PerspectiveManager.setPerspective(lastPerspective);
        timer.stop();
    }

    public float applySmoothScale(float z) {
        if (!perspectiveToggled || timer.isComplete() || !Freelook.config().smoothCamera) return z;

        float transitionProgress = timer.getCurrentProgress();
        float scale = 0.125f + transitionProgress * (1.0f - 0.125f);
        return z * scale;
    }

    public static float calculateCameraRotation(float currentValue, double delta, boolean invert, boolean lock) {
        delta = (delta * 0.15F * (invert ? -1 : 1));
        float rotation = currentValue + (float) delta;
        return lock ? Mth.clamp(rotation, -90.0F, 90.0F) : rotation;
    }
}
