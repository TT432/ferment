package io.github.tt432.ferment.network;

import io.github.tt432.ferment.Ferment;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * @author TT432
 */
public record SlotClickPacket(
        BlockPos pos,
        int slot
) implements CustomPacketPayload {
    public static final Type<SlotClickPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "slot_click"));

    public static final StreamCodec<ByteBuf, SlotClickPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SlotClickPacket::pos,
            ByteBufCodecs.VAR_INT,
            SlotClickPacket::slot,
            SlotClickPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
