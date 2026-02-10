package org.codeberg.chromatic.freelook;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keymapping.KeyMappingRegistryImpl;
import net.minecraft.client.KeyMapping;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
import org.lwjgl.glfw.GLFW;

public class Freelook implements ClientModInitializer, ModMenuApi {
    @Override
    public void onInitializeClient() {
        KeyMapping activationKey = new KeyMapping("freelook.key.activation", GLFW.GLFW_KEY_V, KeyMapping.Category.MISC);

        KeyMappingRegistryImpl.registerKeyMapping(activationKey);
        ClientTickEvents.END_CLIENT_TICK.register(_ -> FreelookHandler.INSTANCE.tick(activationKey));

        FreelookConfig.HANDLER.load();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FreelookConfig::configScreen;
    }

    public static FreelookConfig config() {
        return FreelookConfig.HANDLER.instance();
    }
}
