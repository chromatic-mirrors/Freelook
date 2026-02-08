package com.github.chromaticforge.freelook

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.impl.client.keymapping.KeyMappingRegistryImpl
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

object FreelookMod : ClientModInitializer {

    var key = KeyMapping("freelook.key.activation", GLFW.GLFW_KEY_V, KeyMapping.Category.MISC)

    override fun onInitializeClient() {
        KeyMappingRegistryImpl.registerKeyMapping(key)
    }

}