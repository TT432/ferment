package io.github.tt432.ferment.client.block;

import io.github.tt432.ferment.common.block.entity.FermentBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * @author TT432
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class FermentBlockEntityRenderHandler {
    @SubscribeEvent
    public static void onEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FermentBlockEntities.FERMENTER.get(), FermenterRenderer::new);
    }
}
