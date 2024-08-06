package io.github.tt432.ferment.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

/**
 * @author TT432
 */
public record FermenterRecipe(
        Ingredient base,
        Ingredient nutrient,
        FluidIngredient inputFluid,
        int time,
        ItemStack output,
        Fluid outputFluid
) implements Recipe<FermenterRecipeInput> {
    public static final MapCodec<FermenterRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("base").forGetter(FermenterRecipe::base),
            Ingredient.CODEC.fieldOf("nutrient").forGetter(FermenterRecipe::nutrient),
            FluidIngredient.CODEC.fieldOf("input_fluid").forGetter(FermenterRecipe::inputFluid),
            Codec.INT.fieldOf("time").forGetter(FermenterRecipe::time),
            ItemStack.CODEC.optionalFieldOf("output", ItemStack.EMPTY).forGetter(FermenterRecipe::output),
            BuiltInRegistries.FLUID.byNameCodec().optionalFieldOf("output_fluid", Fluids.EMPTY).forGetter(FermenterRecipe::outputFluid)
    ).apply(instance, FermenterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FermenterRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            FermenterRecipe::base,
            Ingredient.CONTENTS_STREAM_CODEC,
            FermenterRecipe::nutrient,
            FluidIngredient.STREAM_CODEC,
            FermenterRecipe::inputFluid,
            ByteBufCodecs.VAR_INT,
            FermenterRecipe::time,
            ItemStack.OPTIONAL_STREAM_CODEC,
            FermenterRecipe::output,
            ByteBufCodecs.registry(Registries.FLUID),
            FermenterRecipe::outputFluid,
            FermenterRecipe::new
    );

    @Override
    public boolean matches(FermenterRecipeInput pInput, Level pLevel) {
        return base.test(pInput.getItem(0))
                && nutrient.test(pInput.getItem(1))
                && inputFluid.test(new FluidStack(pInput.fluid(), 1));
    }

    @Override
    public ItemStack assemble(FermenterRecipeInput pInput, HolderLookup.Provider pRegistries) {
        pInput.getItem(0).shrink(1);
        pInput.getItem(1).shrink(1);
        pInput.emptyFluidAction().run();
        pInput.outputSetter().accept(outputFluid);
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FermentRecipeSerializers.FERMENTER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FermentRecipeTypes.FERMENTER.get();
    }
}
