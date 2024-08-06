package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentBottles {
    public static ItemStack fillItemStack(Fluid fluid) {
        if (fluid == Fluids.EMPTY) {
            return Items.GLASS_BOTTLE.getDefaultInstance();
        } else if (fluid == Fluids.WATER) {
            return PotionContents.createItemStack(Items.POTION, Potions.WATER);
        } else {
            return new ItemStack(FermentItems.BOTTLE, 1, DataComponentPatch.builder()
                    .set(FermentItemDataComponents.FLUID.get(), fluid)
                    .build());
        }
    }

    public static Fluid getFluid(ItemStack stack) {
        if (stack.getItem() == Items.GLASS_BOTTLE) return Fluids.EMPTY;
        if (stack.getItem() == Items.POTION
                && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER))
            return Fluids.WATER;
        return stack.getOrDefault(FermentItemDataComponents.FLUID, Fluids.EMPTY);
    }
}
