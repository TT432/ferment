package io.github.tt432.ferment.data.provider.lang;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.BottleItem;
import io.github.tt432.ferment.common.item.FermentItemGroup;
import io.github.tt432.ferment.common.item.FermentItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * @author TT432
 */
public class FermentENLanguageProvider extends LanguageProvider {
    public FermentENLanguageProvider(PackOutput output) {
        super(output, Ferment.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(FermentBlocks.FERMENTER, "Fermenter");
        addBlock(FermentBlocks.APPLE_TREE_LEAVES, "Apple Tree Leaves");
        addBlock(FermentBlocks.APPLE_SAPLING, "Apple Sapling");

        addItem(FermentItems.BAD_APPLE, "Bad Apple");
        addItem(FermentItems.SALT, "Salt");

        addItemStack(() -> BottleItem.bottle(() -> Fluids.EMPTY), "Empty Bottle");
        addItemStack(() -> BottleItem.bottle(FermentFluids.SALT_WATER), "Salt Water Bottle");
        addItemStack(() -> BottleItem.bottle(FermentFluids.CIDER), "Cider Bottle");

        add(FermentItemGroup.MAIN.getKey().location().toLanguageKey(), "Ferment");
    }
}
