package io.github.tt432.ferment.data.provider;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.item.FermentItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

/**
 * @author TT432
 */
public class FermentItemModelProvider extends ItemModelProvider {
    public FermentItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Ferment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(FermentItems.BAD_APPLE.asItem());
        basicItem(FermentItems.SALT.asItem());
        basicItem(FermentItems.CIDER_LEES.asItem());
        basicItem(FermentItems.SPENT_GRAIN.asItem());

        getBuilder(FermentItems.BOTTLE.asItem().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.withDefaultNamespace("item/potion_overlay"))
                .texture("layer1", ResourceLocation.withDefaultNamespace("item/potion"));

        simpleBlockItem(FermentItems.APPLE_SAPLING.get());
        blockItem(FermentItems.FERMENTER);
    }

    public ItemModelBuilder blockItem(Holder<Item> item) {
        return getBuilder(item.getKey().location().toString())
                .parent(new ModelFile.UncheckedModelFile(item.getKey().location().withPrefix("block/")));
    }

    public ItemModelBuilder simpleBlockItem(Item item) {
        return simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder simpleBlockItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "block/" + item.getPath()));
    }
}
