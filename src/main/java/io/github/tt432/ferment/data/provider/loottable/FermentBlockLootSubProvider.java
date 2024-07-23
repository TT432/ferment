package io.github.tt432.ferment.data.provider.loottable;

import io.github.tt432.ferment.block.FermentBlocks;
import io.github.tt432.ferment.item.FermentItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author TT432
 */
public class FermentBlockLootSubProvider extends BlockLootSubProvider {
    public FermentBlockLootSubProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        add(FermentBlocks.APPLE_TREE_LEAVES.get(), b ->
                LootTable.lootTable().withPool(this.applyExplosionCondition(FermentItems.BAD_APPLE,
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(FermentItems.BAD_APPLE).setWeight(1))
                                .add(LootItem.lootTableItem(Items.APPLE).setWeight(10))
                                .add(LootItem.lootTableItem(FermentItems.APPLE_SAPLING).setWeight(9))))
        );

        dropSelf(FermentBlocks.APPLE_SAPLING.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return FermentBlocks.BLOCKS.getEntries()
                .stream()
                .flatMap(d -> d.asOptional().stream())
                .map(Block.class::cast)
                ::iterator;
    }
}
