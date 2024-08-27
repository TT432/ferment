package io.github.tt432.ferment.data.provider;

import io.github.tt432.ferment.data.provider.loottable.FermentBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author TT432
 */
public class FermentLootTableProvider extends LootTableProvider {
    public FermentLootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(FermentBlockLootSubProvider::new, LootContextParamSets.BLOCK)
        ), pRegistries);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        // Do not validate against all registered loot tables
    }
}
