package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import io.github.tt432.ferment.common.ui.Slots;
import it.unimi.dsi.fastutil.Pair;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentBottles {
    public static final Slots.Fluid2Item FILL_BOTTLE = new Slots.Fluid2Item() {
        @Override
        public @Nullable ItemStack apply(FluidStack stack, ItemStack container) {
            return container.is(Items.GLASS_BOTTLE) ? fillItemStack(stack.getFluid()) : null;
        }
    };

    public static final Slots.Item2Fluid GET_BOTTLE_FLUID = new Slots.Item2Fluid() {
        @Override
        public @Nullable Pair<FluidStack, ItemStack> apply(ItemStack container) {
            return container.getItem() == Items.POTION
                    && container.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)
                    || container.has(FermentItemDataComponents.FLUID)
                    ? Pair.of(new FluidStack(getFluid(container), FluidType.BUCKET_VOLUME), Items.GLASS_BOTTLE.getDefaultInstance())
                    : null;
        }
    };

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
