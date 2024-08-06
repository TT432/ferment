package io.github.tt432.ferment.client;

import io.github.tt432.ferment.common.block.FermentBlocks;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * @author TT432
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentBlockColors {
    @SubscribeEvent
    public static void onEvent(RegisterColorHandlersEvent.Block event) {
        event.register(
                (blockState, getter, blockPos, i) -> getter != null && blockPos != null
                        ? BiomeColors.getAverageFoliageColor(getter, blockPos)
                        : FoliageColor.getDefaultColor(),
                FermentBlocks.APPLE_TREE_LEAVES.get()
        );
    }
}
