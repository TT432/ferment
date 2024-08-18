package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ferment.MOD_ID);

    public static final DeferredItem<Item> BAD_APPLE = ITEMS.registerItem("bad_apple",
            Item::new, new Item.Properties().food(Foods.APPLE));

    public static final DeferredItem<BlockItem> APPLE_SAPLING = ITEMS.registerSimpleBlockItem(FermentBlocks.APPLE_SAPLING);
    public static final DeferredItem<BlockItem> FERMENTER = ITEMS.registerSimpleBlockItem(FermentBlocks.FERMENTER);
    public static final DeferredItem<Item> SALT = ITEMS.registerItem("salt", Item::new);
    public static final DeferredItem<BottleItem> BOTTLE =
            ITEMS.registerItem("bottle", BottleItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> CIDER_LEES = ITEMS.registerItem("cider_lees", Item::new);
    public static final DeferredItem<Item> SPENT_GRAIN = ITEMS.registerItem("spent_grain", Item::new);
}
