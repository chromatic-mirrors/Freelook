package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keymapping.KeyMappingRegistryImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
import org.lwjgl.glfw.GLFW;

public class Freelook implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyMapping.Category freelookCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("freelook", "freelook"));
        KeyMapping activationKey = new KeyMapping("freelook.key.activate", GLFW.GLFW_KEY_V, freelookCategory);

        KeyMappingRegistryImpl.registerKeyMapping(activationKey);
        ClientTickEvents.END_CLIENT_TICK.register(_ -> FreelookHandler.INSTANCE.tick(activationKey));

        FreelookConfig.HANDLER.load();
    }

    public static FreelookConfig config() {
        return FreelookConfig.HANDLER.instance();
    }
}
