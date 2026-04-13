package org.codeberg.chromatic.freelook.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record HandshakePayload() implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("freelook", "handshake");
    public static final Type<HandshakePayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, HandshakePayload> CODEC = StreamCodec.unit(new HandshakePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
