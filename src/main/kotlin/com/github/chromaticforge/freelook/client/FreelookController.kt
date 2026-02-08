package com.github.chromaticforge.freelook.client

import com.github.chromaticforge.freelook.FreelookMod
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth

object FreelookController {
    @JvmField
    var perspectiveToggled: Boolean = false
    private var lastPerspective: Int = 0
    private var pressStartTime: Long = 0
    private var lastUpdateTime: Long = 0
    private val timer = EaseInExpoTimer(650L)

    fun tick() {
        val key = FreelookMod.key

        if (FreelookConfig.Activation.pressMode == 1) {
            handlePressAndHold(key.isDown)
        } else if (FreelookConfig.Activation.pressMode == 2) {
            if (key.consumeClick()) {
                toggle()
            }
        } else {
            if (key.isDown && !perspectiveToggled) {
                start()
            } else if (!key.isDown && perspectiveToggled) {
                stop()
            }
        }
    }

    fun handlePressAndHold(pressed: Boolean) {
        if (pressed && pressStartTime == 0L) {
            pressStartTime = System.currentTimeMillis()
            toggle()
        } else {
            val pressDuration = System.currentTimeMillis() - pressStartTime
            pressStartTime = 0L

            if (pressDuration > FreelookConfig.Activation.holdThreshold) {
                stop()
            }
        }
    }

    fun toggle() {
        if (perspectiveToggled) {
            stop()
        } else {
            start()
        }
    }

    fun start() {
        val currentPerspective = PerspectiveManager.getCurrentPerspective()
        if (currentPerspective != lastPerspective) {
            lastPerspective = currentPerspective
        }

        val perspective = FreelookConfig.perspectiveMode

        when (FreelookConfig.changePerspective) {
            0 -> {}
            1 -> if (lastPerspective == 0) {
                PerspectiveManager.setPerspective(perspective)
            }

            2 -> if (lastPerspective != 0) {
                PerspectiveManager.setPerspective(perspective)
            }

            3 -> PerspectiveManager.setPerspective(perspective)
        }

        if (FreelookConfig.smoothCamera) {
            timer.start()
            lastUpdateTime = System.currentTimeMillis()
        }

        val player = Minecraft.getInstance().player!!
        CameraStateTracker.setCameraYaw(player, player.xRot)
        CameraStateTracker.setCameraPitch(player, player.yRot)

        perspectiveToggled = true
    }

    fun stop() {
        perspectiveToggled = false
        PerspectiveManager.setPerspective(lastPerspective)
        timer.stop()
    }

    fun applySmoothScale(z: Float): Float {
        if (!perspectiveToggled || timer.complete || !FreelookConfig.smoothCamera) {
            return z
        }

        val currentTime = System.currentTimeMillis()
        currentTime - lastUpdateTime
        lastUpdateTime = currentTime

        val transitionProgress = timer.currentProgress
        val scale = 0.125f + transitionProgress * (1.0f - 0.125f)
        return z * scale
    }

    fun updateCameraValue(
        currentValue: Float,
        delta: Float,
        invert: Boolean,
        lock: Boolean
    ): Float {
        val adjustedDelta = if (invert) -delta else delta
        return if (lock) {
            Mth.clamp(currentValue + adjustedDelta, -90.0F, 90.0F)
        } else {
            currentValue + adjustedDelta
        }
    }
}
