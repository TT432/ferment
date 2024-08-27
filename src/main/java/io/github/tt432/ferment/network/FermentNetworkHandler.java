package io.github.tt432.ferment.network;

import io.github.tt432.ferment.common.ui.SlotSet;
import io.github.tt432.ferment.data.FermentDataAttachments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * @author TT432
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FermentNetworkHandler {
    @SubscribeEvent
    public static void onEvent(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(SlotClickPacket.TYPE, SlotClickPacket.STREAM_CODEC, (pkg, ctx) -> {
            Level level = ctx.player().level();

            if (level.isLoaded(pkg.pos())) {
                BlockEntity blockEntity = level.getBlockEntity(pkg.pos());

                if (blockEntity != null && blockEntity.hasData(FermentDataAttachments.SLOT_SET)) {
                    SlotSet.SlotWithIndex slot = blockEntity.getData(FermentDataAttachments.SLOT_SET).get(pkg.slot());

                    if (slot != null) {
                        slot.slot().clickAction().onClick(ctx.player());
                    }
                }
            }
        });
    }
}
