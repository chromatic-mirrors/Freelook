package org.codeberg.chromatic.freelook;

import net.fabricmc.api.ClientModInitializer;
//? if >1.8.9 {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
//?} else {
/*import net.minecraft.client.Minecraft;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.ChannelRegistry;
import net.ornithemc.osl.networking.api.StringChannelIdentifierParser;
import net.ornithemc.osl.networking.api.client.ClientConnectionEvents;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
*///?}
import org.codeberg.chromatic.freelook.handler.FreelookHandler;
import org.codeberg.chromatic.freelook.network.DisableModPayload;
import org.codeberg.chromatic.freelook.network.HandshakePayload;
import org.codeberg.chromatic.freelook.option.FreelookConfig;
//? if 1.8.9 {
/*import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.TickEvent;

import java.nio.charset.StandardCharsets;
*///?}

public class Freelook implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //? if >1.8.9 {
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
        //?} else {
        /*EventManager.register(TickEvent.End.class, event -> FreelookHandler.INSTANCE.tick(Minecraft.getInstance()));

        ClientPlayNetworking.registerListener(DisableModPayload.ID, DisableModPayload::new, (context, payload) -> {
            context.ensureOnMainThread();

            FreelookHandler.INSTANCE.enabledServer = false;

            if (context.minecraft().player == null) return;
            context.minecraft().player.addMessage(
                    new LiteralText("This server has disabled Freelook.").setStyle(new Style().setColor(Formatting.RED))
            );
        });

        ClientConnectionEvents.LOGIN.register(context -> {
            FreelookHandler.INSTANCE.enabledServer = true;

            ClientPlayNetworking.sendNoCheck(REGISTER, "freelook:disable".getBytes(StandardCharsets.UTF_8));
            ClientPlayNetworking.sendNoCheck(HandshakePayload.ID, new HandshakePayload());
        });
        *///?}

        FreelookConfig.INSTANCE.preload();
        FreelookConfig.INSTANCE.migrateVanillaKeybind();
        FreelookConfig.INSTANCE.migrateDefaults();
    }

    //? if 1.8.9 {
    /*private static final NamespacedIdentifier REGISTER = ChannelRegistry.register(
            StringChannelIdentifierParser.fromString("REGISTER"), false, true
    );
    *///?}
}
