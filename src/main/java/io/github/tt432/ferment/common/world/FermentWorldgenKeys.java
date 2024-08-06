package io.github.tt432.ferment.common.world;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentWorldgenKeys {
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_TREE_CONFIGURED = configuredFeature("apple_tree");
    public static final ResourceKey<PlacedFeature> APPLE_TREE_PLACED = placedFeature("apple_tree");

    private static ResourceKey<PlacedFeature> placedFeature(String path) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, path)
        );
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(String path) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, path)
        );
    }
}
