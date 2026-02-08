package org.codeberg.chromatic.freelook.util;

public class SmoothTransitionTimer {
    private final long durationMs;
    private long startTime = 0L;
    private boolean active = false;

    public SmoothTransitionTimer(long durationMs) {
        this.durationMs = durationMs;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isComplete() {
        return getTimeRemaining() <= 0L && active;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.active = true;
    }

    public void stop() {
        this.active = false;
    }

    public float getCurrentProgress() {
        if (!active || startTime == 0L) {
            return 0.0f;
        }
        if (isComplete()) {
            return 1.0f;
        }
        float f = Math.clamp((float) (durationMs - getTimeRemaining()) / durationMs, 0.0f, 1.0f);
        return Math.clamp((float) (1.0 - Math.pow(2.0, -10.0 * f)), 0.0f, 1.0f);
    }

    private long getTimeRemaining() {
        return startTime + durationMs - System.currentTimeMillis();
    }
}
