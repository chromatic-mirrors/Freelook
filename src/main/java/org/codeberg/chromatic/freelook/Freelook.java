package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.keymapping.KeyMappingRegistryImpl;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Freelook implements ClientModInitializer {
    public static KeyMapping key = new KeyMapping("freelook.key.activation", GLFW.GLFW_KEY_V, KeyMapping.Category.MISC);

    @Override
    public void onInitializeClient() {
        KeyMappingRegistryImpl.registerKeyMapping(key);
    }
}
