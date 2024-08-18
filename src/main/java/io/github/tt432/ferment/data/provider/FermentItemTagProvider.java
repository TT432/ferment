package io.github.tt432.ferment.data.provider;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.item.FermentItemTags;
import io.github.tt432.ferment.common.item.FermentItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * @author TT432
 */
public class FermentItemTagProvider extends ItemTagsProvider {
    public FermentItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagLookup<Block>> blockTagProvider,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTagProvider, Ferment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(FermentItemTags.LEES)
                .add(FermentItems.CIDER_LEES.asItem())
                .add(FermentItems.SPENT_GRAIN.asItem());
    }
}
