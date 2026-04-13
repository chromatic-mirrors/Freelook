package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.impl.client.keymapping.KeyMappingRegistryImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.network.DisableModPayload;
import org.codeberg.chromatic.freelook.network.HandshakePayload;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
import org.lwjgl.glfw.GLFW;

public class Freelook implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMapping.Category freelookCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("freelook", "freelook"));
        KeyMapping activationKey = new KeyMapping("freelook.key.activate", GLFW.GLFW_KEY_V, freelookCategory);

        KeyMappingRegistryImpl.registerKeyMapping(activationKey);
        ClientTickEvents.END_CLIENT_TICK.register(_ -> FreelookHandler.INSTANCE.tick(activationKey));

        PayloadTypeRegistry.clientboundPlay().register(DisableModPayload.TYPE, DisableModPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(HandshakePayload.TYPE, HandshakePayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DisableModPayload.TYPE, (payload, context) -> {
            FreelookHandler.INSTANCE.enabledServer = false;
            Component message = Component.literal("This server has disabled Freelook.").withStyle(ChatFormatting.RED);

            context.player().sendSystemMessage(message);
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
