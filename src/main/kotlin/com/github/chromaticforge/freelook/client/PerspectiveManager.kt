package com.github.chromaticforge.freelook.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Perspective

object PerspectiveManager {
    fun setPerspective(perspective: Int) {
        MinecraftClient.getInstance().options.perspective = Perspective.entries[perspective]
    }

    fun getCurrentPerspective(): Int {
        val perspective = MinecraftClient.getInstance().options.perspective
        return perspective.ordinal
    }

    /**
     * @return The maximum perspective index
     */
    fun getMaximumPerspectiveIndex(): Int {
        return Perspective.entries.size - 1
    }
}
