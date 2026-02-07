package com.github.chromaticforge.freelook

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object FreelookMod : ClientModInitializer {

    var key = KeyBinding("freelook.key.activation", GLFW.GLFW_KEY_V, "key.categories.misc")

    override fun onInitializeClient() {
        KeyBindingRegistryImpl.registerKeyBinding(key)
    }

}