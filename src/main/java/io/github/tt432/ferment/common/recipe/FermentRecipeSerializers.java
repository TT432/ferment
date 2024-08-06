package io.github.tt432.ferment.common.recipe;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Ferment.MOD_ID);

    public static final Supplier<RecipeSerializer<FermenterRecipe>> FERMENTER = RECIPE_SERIALIZERS.register("fermenter",
            () -> new SimpleRecipeSerializer<>(FermenterRecipe.CODEC, FermenterRecipe.STREAM_CODEC));
}
