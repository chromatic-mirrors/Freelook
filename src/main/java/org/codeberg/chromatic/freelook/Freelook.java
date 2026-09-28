package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.network.DisableModPayload;
import org.codeberg.chromatic.freelook.network.HandshakePayload;
import org.codeberg.chromatic.freelook.option.FreelookConfig;

public class Freelook implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(FreelookHandler.INSTANCE::tick);

        PayloadTypeRegistry.clientboundPlay().register(DisableModPayload.TYPE, DisableModPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(HandshakePayload.TYPE, HandshakePayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DisableModPayload.TYPE, (payload, context) -> {
            FreelookHandler.INSTANCE.enabledServer = false;

            Component message = Component.literal("This server has disabled Freelook.").withStyle(ChatFormatting.RED);

            context.player().sendSystemMessage(
                    message
                    /*? if < 26.1*/
                    //, true
            );
        });

        ClientPlayConnectionEvents.JOIN.register((listener, sender, minecraft) -> {
            sender.sendPacket(new HandshakePayload());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> {
            FreelookHandler.INSTANCE.enabledServer = true;
        });

        FreelookConfig.INSTANCE.preload();
        FreelookConfig.INSTANCE.migrateVanillaKeybind();
    }
}
