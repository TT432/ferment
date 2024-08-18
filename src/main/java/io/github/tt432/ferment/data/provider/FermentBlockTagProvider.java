package io.github.tt432.ferment.data.provider;

import io.github.tt432.ferment.Ferment;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author TT432
 */
public class FermentBlockTagProvider extends BlockTagsProvider {
    public FermentBlockTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, Ferment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

    }
}
