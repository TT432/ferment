package io.github.tt432.ferment.common.recipe;

import io.github.tt432.ferment.common.block.entity.FermenterBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author TT432
 */
public record FermenterRecipeInput(
        FermenterBlockEntity entity
) implements RecipeInput {
    public NonNullList<ItemStack> inv() {
        return entity.getData().getInventory();
    }

    public Fluid fluid() {
        return entity.getData().getFluid();
    }

    public Runnable emptyFluidAction() {
        return () -> entity.getData().setFluid(Fluids.EMPTY);
    }

    public Consumer<Fluid> outputSetter() {
        return outputFluid -> entity.getData().setOutputFluid(outputFluid);
    }

    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        return inv().get(pIndex);
    }

    @Override
    public int size() {
        return inv().size();
    }
}
