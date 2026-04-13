package org.codeberg.chromatic.freelook.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DisableModPayload() implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("freelook", "disable");
    public static final Type<DisableModPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DisableModPayload> CODEC = StreamCodec.unit(new DisableModPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
