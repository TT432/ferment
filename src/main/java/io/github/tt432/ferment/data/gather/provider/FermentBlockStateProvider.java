package io.github.tt432.ferment.data.provider;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

/**
 * @author TT432
 */
public class FermentBlockStateProvider extends BlockStateProvider {
    public FermentBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Ferment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(FermentBlocks.APPLE_SAPLING.get(), cross(FermentBlocks.APPLE_SAPLING));

        simpleBlock(FermentBlocks.APPLE_TREE_LEAVES.get(),
                models().leaves(loc(FermentBlocks.APPLE_TREE_LEAVES), blockTex(FermentBlocks.APPLE_TREE_LEAVES)));
    }

    private <B extends Block> String loc(Supplier<B> block) {
        return BuiltInRegistries.BLOCK.getKey(block.get()).toString();
    }

    private ResourceLocation blockTex(Holder<Block> block) {
        return ResourceLocation.fromNamespaceAndPath(block.getKey().location().getNamespace(),
                "block/" + block.getKey().location().getPath());
    }

    private ModelFile cross(Holder<Block> block) {
        return models().cross(BuiltInRegistries.BLOCK.getKey(block.value()).toString(), blockTex(block))
                .renderType(ResourceLocation.withDefaultNamespace("cutout"));
    }
}
