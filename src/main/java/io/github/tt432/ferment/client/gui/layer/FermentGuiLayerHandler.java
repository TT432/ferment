package io.github.tt432.ferment.client.gui.layer;

import io.github.tt432.eyelib.util.ResourceLocations;
import io.github.tt432.ferment.Ferment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * @author TT432
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class FermentGuiLayerHandler {
    @SubscribeEvent
    public static void onEvent(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ResourceLocations.of(Ferment.MOD_ID, "fermenter"), new FermenterContentDisplayLayer());
    }
}
