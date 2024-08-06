package io.github.tt432.ferment.common.datapack;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FermentDataPacks {
    public static ResourceKey<Registry<BottleData>> BOTTLE =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "bottle"));

    @SubscribeEvent
    public static void onEvent(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BOTTLE, BottleData.CODEC, BottleData.CODEC);
    }
}
