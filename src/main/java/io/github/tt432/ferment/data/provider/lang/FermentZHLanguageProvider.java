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
public class FermentZHLanguageProvider extends LanguageProvider {
    public FermentZHLanguageProvider(PackOutput output) {
        super(output, Ferment.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addBlock(FermentBlocks.FERMENTER, "发酵罐");
        addBlock(FermentBlocks.APPLE_TREE_LEAVES, "苹果树树叶");
        addBlock(FermentBlocks.APPLE_SAPLING, "苹果树苗");

        addItem(FermentItems.BAD_APPLE, "烂苹果");
        addItem(FermentItems.SALT, "盐");
        addItem(FermentItems.CIDER_LEES, "苹果渣");

        addItemStack(() -> BottleItem.bottle(FermentFluids.SALT_WATER), "盐水瓶");
        addItemStack(() -> BottleItem.bottle(FermentFluids.CIDER), "苹果酒瓶");
        addItemStack(() -> BottleItem.bottle(FermentFluids.ALE), "小麦酒瓶");

        add(FermentItemGroup.MAIN.getKey().location().toLanguageKey(), "发酵");

        addFluid(FermentFluids.SALT_WATER, "盐水");
        addFluid(FermentFluids.CIDER, "苹果酒");

        add("ferment.jei.recipe.bottle", "瓶");
    }

    void addFluid(Holder<Fluid> holder, String value) {
        add(holder.getKey().location().toLanguageKey("fluid"), value);
    }
}
