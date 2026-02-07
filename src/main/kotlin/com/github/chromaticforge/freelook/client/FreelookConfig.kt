package com.github.chromaticforge.freelook.client

object FreelookConfig {
    // "Which perspective should make Freelook/Snaplook start in a different perspective"
    // 0 = Never, 1 = First Person Only, 2 = Third Person Only, 3 = Always
    var changePerspective: Int = 1

    // "Which camera perspective to start if changed"
    // 0 = First Person, 1 = Third Person, 2 = Third Person Reversed
    var perspectiveMode: Int = 1

    object Activation {
        // "Whether you can hold, quick press, or toggle to activate"
        // 0 = Hold, 1 = Quick Press, 2 = Toggle
        var pressMode: Int = 1
        // "How long you can hold before Freelook/Snaplook is disabled upon release"
        var holdThreshold: Long = 300
    }

    open class MovementConfig {
        var enabled: Boolean = true
        var invert: Boolean = false
    }

    object Pitch : MovementConfig() {
        var lock: Boolean = true
    }

    object Yaw : MovementConfig() {
        var lock: Boolean = false
    }


    @JvmField
    // "Add Freelook/Snaplook as part of the perspective cycle"
    var addToCameraCycle: Boolean = false

    @JvmField
    // 0 = Change & Freelook, 1 = Stop Freelook, 3 = Block Perspective Change
    var onCycleChange: Int = 1

    // "Animate third person when Freelook/Snaplook is enabled"
    var smoothCamera: Boolean = false

    // "Use a vanilla perspective instead."
    var snaplook: Boolean = false
}