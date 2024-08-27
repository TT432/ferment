package io.github.tt432.ferment.data.provider.lang;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.BottleItem;
import io.github.tt432.ferment.common.item.FermentItemGroup;
import io.github.tt432.ferment.common.item.FermentItems;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.material.Fluid;
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
        addItem(FermentItems.CIDER_LEES, "Apple Pomace");

        addItemStack(() -> BottleItem.bottle(FermentFluids.SALT_WATER), "Salt Water Bottle");
        addItemStack(() -> BottleItem.bottle(FermentFluids.CIDER), "Cider Bottle");
        addItemStack(() -> BottleItem.bottle(FermentFluids.ALE), "Ale Bottle");

        add(FermentItemGroup.MAIN.getKey().location().toLanguageKey(), "Ferment");

        addFluid(FermentFluids.SALT_WATER, "Salt Water");
        addFluid(FermentFluids.CIDER, "Cider");

        add("ferment.jei.recipe.bottle", "Bottle");
    }

    void addFluid(Holder<Fluid> holder, String value) {
        add(holder.getKey().location().toLanguageKey("fluid"), value);
    }
}
