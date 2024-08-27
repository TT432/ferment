package io.github.tt432.ferment.common.recipe;

import io.github.tt432.ferment.common.block.entity.FermenterBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
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
        return entity.getData().getFluidStacks().get(0).getFluid();
    }

    public Runnable emptyFluidAction() {
        return () -> entity.getData().getFluidStacks().set(0, new FluidStack(Fluids.EMPTY, FluidType.BUCKET_VOLUME));
    }

    public Consumer<Fluid> outputSetter() {
        return outputFluid -> entity.getData().getFluidStacks().set(1, new FluidStack(outputFluid, FluidType.BUCKET_VOLUME));
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
