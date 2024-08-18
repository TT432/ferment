package io.github.tt432.ferment.data;

import io.github.tt432.eyelib.util.ResourceLocations;
import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import io.github.tt432.ferment.common.datapack.BottleData;
import io.github.tt432.ferment.common.datapack.FermentDataPacks;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.FermentItems;
import io.github.tt432.ferment.common.world.FermentWorldgenKeys;
import io.github.tt432.ferment.data.provider.*;
import io.github.tt432.ferment.data.provider.lang.FermentENLanguageProvider;
import io.github.tt432.ferment.data.provider.lang.FermentZHLanguageProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

/**
 * @author TT432
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatherDataHandler {
    @SubscribeEvent
    public static void onEvent(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                output,
                lookupProvider,
                new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, GatherDataHandler::registerConfiguredFeature)
                        .add(Registries.PLACED_FEATURE, GatherDataHandler::registerPlacedFeature)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GatherDataHandler::registerBiomeModifiers)
                        .add(FermentDataPacks.BOTTLE, GatherDataHandler::registerBottleData),
                Set.of(Ferment.MOD_ID)
        ));

        generator.addProvider(event.includeServer(), new FermentLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new FermenterRecipeProvider(output, lookupProvider));
        FermentBlockTagProvider blockTagProvider = new FermentBlockTagProvider(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTagProvider);
        generator.addProvider(event.includeServer(),
                new FermentItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), helper));

        generator.addProvider(event.includeClient(), new FermentItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new FermentBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new FermentENLanguageProvider(output));
        generator.addProvider(event.includeClient(), new FermentZHLanguageProvider(output));
    }

    private static void registerBottleData(BootstrapContext<BottleData> bootstrap) {
        bootstrap.register(ResourceKey.create(
                FermentDataPacks.BOTTLE,
                ResourceLocations.of(Ferment.MOD_ID, "salt_water")
        ), new BottleData(Ingredient.of(FermentItems.SALT),
                FluidIngredient.of(Fluids.WATER), FermentFluids.SALT_WATER.get()));
    }

    private static void registerBiomeModifiers(BootstrapContext<BiomeModifier> bootstrap) {
        var biomeRegistry = bootstrap.lookup(Registries.BIOME);
        var placedFeatureRegistry = bootstrap.lookup(Registries.PLACED_FEATURE);

        placedFeatureRegistry.get(FermentWorldgenKeys.APPLE_TREE_PLACED).ifPresent(feature ->
                biomeRegistry.get(Tags.Biomes.IS_FOREST).ifPresent(n ->
                        bootstrap.register(ResourceKey.create(
                                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "add_apple_tree")
                        ), new BiomeModifiers.AddFeaturesBiomeModifier(
                                n, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION
                        ))));
    }

    private static void registerPlacedFeature(BootstrapContext<PlacedFeature> bootstrap) {
        HolderGetter<ConfiguredFeature<?, ?>> otherRegistry = bootstrap.lookup(Registries.CONFIGURED_FEATURE);

        otherRegistry.get(FermentWorldgenKeys.APPLE_TREE_CONFIGURED).ifPresent(holder ->
                bootstrap.register(FermentWorldgenKeys.APPLE_TREE_PLACED, new PlacedFeature(holder,
                        VegetationPlacements.treePlacement(PlacementUtils
                                        .countExtra(10, 0.1F, 1),
                                Blocks.OAK_SAPLING))));
    }

    private static void registerConfiguredFeature(BootstrapContext<ConfiguredFeature<?, ?>> bootstrap) {
        bootstrap.register(FermentWorldgenKeys.APPLE_TREE_CONFIGURED,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(Blocks.OAK_LOG),
                                new StraightTrunkPlacer(4, 2, 0),
                                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                        .add(FermentBlocks.APPLE_TREE_LEAVES.get().defaultBlockState(), 1)
                                        .add(Blocks.OAK_LEAVES.defaultBlockState(), 19)
                                        .build()),
                                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).ignoreVines().build()
                ));
    }
}
