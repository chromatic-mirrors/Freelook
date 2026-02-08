package com.github.chromaticforge.freelook.client

import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft

object PerspectiveManager {
    fun setPerspective(perspective: Int) {
        Minecraft.getInstance().options.cameraType = CameraType.entries[perspective]
    }

    fun getCurrentPerspective(): Int {
        val perspective = Minecraft.getInstance().options.cameraType
        return perspective.ordinal
    }

    /**
     * @return The maximum perspective index
     */
    fun getMaximumPerspectiveIndex(): Int {
        return CameraType.entries.size - 1
    }
}
