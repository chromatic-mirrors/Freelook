package org.codeberg.chromatic.freelook.handler;

import org.codeberg.chromatic.freelook.Freelook;
import org.codeberg.chromatic.freelook.option.FreelookOptions;
import org.codeberg.chromatic.freelook.util.CameraStateHandler;
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

    public void tick() {
        KeyMapping key = Freelook.key;

        if (FreelookOptions.Activation.pressMode == FreelookOptions.PressMode.QUICK_PRESS) {
            handlePressAndHold(key.isDown());
        } else if (FreelookOptions.Activation.pressMode == FreelookOptions.PressMode.TOGGLE) {
            if (key.consumeClick()) {
                toggle();
            }
        } else {
            if (key.isDown() && !perspectiveToggled) {
                start();
            } else if (!key.isDown() && perspectiveToggled) {
                stop();
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

            if (pressDuration > FreelookOptions.Activation.holdThreshold) {
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
        CameraType currentPerspective = PerspectiveManager.getCurrentPerspective();
        if (currentPerspective != lastPerspective) {
            lastPerspective = currentPerspective;
        }

        CameraType targetPerspective = switch (FreelookOptions.perspectiveMode) {
            case FIRST_PERSON -> CameraType.FIRST_PERSON;
            case THIRD_PERSON -> CameraType.THIRD_PERSON_BACK;
            case THIRD_PERSON_REVERSED -> CameraType.THIRD_PERSON_FRONT;
        };

        switch (FreelookOptions.changePerspective) {
            case NEVER -> {}
            case FIRST_PERSON_ONLY -> {
                if (lastPerspective == CameraType.FIRST_PERSON) {
                    PerspectiveManager.setPerspective(targetPerspective);
                }
            }
            case THIRD_PERSON_ONLY -> {
                if (lastPerspective != CameraType.FIRST_PERSON) {
                    PerspectiveManager.setPerspective(targetPerspective);
                }
            }
            case ALWAYS -> PerspectiveManager.setPerspective(targetPerspective);
        }

        if (FreelookOptions.smoothCamera) {
            timer.start();
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            CameraStateHandler handler = (CameraStateHandler) player;
            handler.freelook$setPitch(player.getXRot());
            handler.freelook$setYaw(player.getYRot());
        }

        perspectiveToggled = true;
    }

    public void stop() {
        perspectiveToggled = false;
        PerspectiveManager.setPerspective(lastPerspective);
        timer.stop();
    }

    public float applySmoothScale(float z) {
        if (!perspectiveToggled || timer.isComplete() || !FreelookOptions.smoothCamera) {
            return z;
        }

        float transitionProgress = timer.getCurrentProgress();
        float scale = 0.125f + transitionProgress * (1.0f - 0.125f);
        return z * scale;
    }

    public static float updateCameraValue(
        float currentValue,
        float delta,
        boolean invert,
        boolean lock
    ) {
        float adjustedDelta = invert ? -delta : delta;
        if (lock) {
            return Mth.clamp(currentValue + adjustedDelta, -90.0F, 90.0F);
        } else {
            return currentValue + adjustedDelta;
        }
    }
}
