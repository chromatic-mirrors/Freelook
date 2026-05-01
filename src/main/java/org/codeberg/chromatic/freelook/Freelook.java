package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.network.DisableModPayload;
import org.codeberg.chromatic.freelook.network.HandshakePayload;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
import org.lwjgl.glfw.GLFW;

public class Freelook implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMapping.Category freelookCategory = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("freelook", "freelook"));
        KeyMapping activationKey = new KeyMapping("freelook.key.activate", GLFW.GLFW_KEY_V, freelookCategory);

        KeyBindingHelper.registerKeyBinding(activationKey);
        ClientTickEvents.END_CLIENT_TICK.register(it -> FreelookHandler.INSTANCE.tick(activationKey));

        PayloadTypeRegistry.playS2C().register(DisableModPayload.TYPE, DisableModPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(HandshakePayload.TYPE, HandshakePayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DisableModPayload.TYPE, (payload, context) -> {
            FreelookHandler.INSTANCE.enabledServer = false;
            Component message = Component.literal("This server has disabled Freelook.").withStyle(ChatFormatting.RED);

            context.player().displayClientMessage(message, true);
        });

        ClientPlayConnectionEvents.JOIN.register((listener, sender, minecraft) -> {
            sender.sendPacket(new HandshakePayload());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> {
            FreelookHandler.INSTANCE.enabledServer = true;
        });

        FreelookConfig.HANDLER.load();
    }

    public static FreelookConfig config() {
        return FreelookConfig.HANDLER.instance();
    }

}
