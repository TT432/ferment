package io.github.tt432.ferment.data.provider;

import io.github.tt432.eyelib.util.ResourceLocations;
import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.FermentItemTags;
import io.github.tt432.ferment.common.item.FermentItems;
import io.github.tt432.ferment.common.recipe.FermenterRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.concurrent.CompletableFuture;

/**
 * @author TT432
 */
public class FermenterRecipeProvider extends RecipeProvider {
    public FermenterRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        add(output, ResourceLocations.of(Ferment.MOD_ID, "cider"), new FermenterRecipe(
                Ingredient.of(FermentItems.BAD_APPLE),
                Ingredient.of(Items.APPLE),
                FluidIngredient.of(Fluids.WATER),
                48000,
                FermentItems.CIDER_LEES.toStack(),
                FermentFluids.CIDER.get()
        ));

        add(output, ResourceLocations.of(Ferment.MOD_ID, "ale"), new FermenterRecipe(
                Ingredient.of(FermentItemTags.LEES),
                Ingredient.of(Items.WHEAT),
                FluidIngredient.of(Fluids.WATER),
                48000,
                FermentItems.SPENT_GRAIN.toStack(),
                FermentFluids.ALE.get()
        ));
    }

    <T extends Recipe<?>> void add(RecipeOutput output, ResourceLocation id, T recipe) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }
}
