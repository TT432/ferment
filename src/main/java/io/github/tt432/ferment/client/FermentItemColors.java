package io.github.tt432.ferment.client;

import io.github.tt432.ferment.common.item.FermentItems;
import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TT432
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class FermentItemColors {
    @SubscribeEvent
    @SuppressWarnings("removal")
    public static void onEvent(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, layer) -> {
                    AtomicInteger color = new AtomicInteger();
                    stack.getOrDefault(FermentItemDataComponents.FLUID, Fluids.EMPTY)
                            .getFluidType()
                            .initializeClient(ex -> color.set(ex.getTintColor()));
                    return layer > 0 ? -1 : FastColor.ARGB32.opaque(color.get());
                },
                FermentItems.BOTTLE
        );
    }
}
