package io.github.tt432.ferment;

import io.github.tt432.ferment.block.FermentBlocks;
import io.github.tt432.ferment.item.FermentItemGroup;
import io.github.tt432.ferment.item.FermentItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * @author TT432
 */
@Mod(Ferment.MOD_ID)
public class Ferment {
    public static final String MOD_ID = "ferment";

    public Ferment(IEventBus bus) {
        FermentBlocks.BLOCKS.register(bus);
        FermentItems.ITEMS.register(bus);
        FermentItemGroup.CREATIVE_MODE_TABS.register(bus);
    }
}
