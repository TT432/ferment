package io.github.tt432.ferment.jei;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.block.FermentBlocks;
import io.github.tt432.ferment.common.datapack.BottleData;
import io.github.tt432.ferment.common.datapack.FermentDataPacks;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.BottleItem;
import io.github.tt432.ferment.common.item.FermentItems;
import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import io.github.tt432.ferment.common.recipe.FermentRecipeTypes;
import io.github.tt432.ferment.common.recipe.FermenterRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author TT432
 */
@JeiPlugin
public class FermentJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "plugin");
    }

    @SuppressWarnings("unchecked")
    private static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> from(
            Holder<net.minecraft.world.item.crafting.RecipeType<?>> recipeType
    ) {
        return new RecipeType<>(recipeType.getKey().location(), (Class<RecipeHolder<R>>) (Object) RecipeHolder.class);
    }

    private final RecipeType<RecipeHolder<FermenterRecipe>> fermenterRecipeType = from(FermentRecipeTypes.FERMENTER);
    private final RecipeType<BottleData> bottleDataType = new RecipeType<>(FermentDataPacks.BOTTLE.location(), BottleData.class);

    public record RecordRecipeCategory<R>(
            RecipeType<R> getRecipeType,
            Component getTitle,
            IDrawable getBackground,
            IDrawable getIcon,
            SetRecipe<R> setRecipe
    ) implements IRecipeCategory<R> {
        @FunctionalInterface
        public interface SetRecipe<R> {
            void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, R recipe, IFocusGroup focuses) {
            setRecipe.setRecipe(builder, recipe, focuses);
        }
    }

    public static final ResourceLocation RECIPE_GUI =
            ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "textures/gui/jei/fermenter.png");

    public static final ResourceLocation BOTTLE =
            ResourceLocation.fromNamespaceAndPath(Ferment.MOD_ID, "textures/gui/jei/bottle.png");

    private static IDrawableStatic fullTexture(IGuiHelper guiHelper, ResourceLocation texture, int w, int h) {
        return guiHelper.drawableBuilder(texture, 0, 0, w, h).setTextureSize(w, h).build();
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(FermentItems.BOTTLE.get(), (stack, __) -> BuiltInRegistries.FLUID.getKey(stack.getOrDefault(FermentItemDataComponents.FLUID, Fluids.EMPTY)).toString());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new RecordRecipeCategory<>(
                fermenterRecipeType,
                Component.translatable(FermentBlocks.FERMENTER.get().getDescriptionId()),
                fullTexture(guiHelper, RECIPE_GUI, 124, 90),
                guiHelper.createDrawableItemStack(new ItemStack(FermentBlocks.FERMENTER)),
                (builder, recipe, focuses) -> {
                    FermenterRecipe fermenterRecipe = recipe.value();
                    builder.addSlot(RecipeIngredientRole.INPUT, 35, 7)
                            .addIngredients(fermenterRecipe.base());
                    builder.addSlot(RecipeIngredientRole.INPUT, 35, 28)
                            .addIngredients(fermenterRecipe.nutrient());
                    builder.addSlot(RecipeIngredientRole.INPUT, 12, 7)
                            .setFluidRenderer(1, false, 18, 77)
                            .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(fermenterRecipe.inputFluid().getStacks()));

                    builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 68)
                            .addItemStack(fermenterRecipe.output());
                    builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 7)
                            .setFluidRenderer(1, false, 18, 77)
                            .addFluidStack(fermenterRecipe.outputFluid(), 1);
                }
        ));
        registration.addRecipeCategories(new RecordRecipeCategory<>(
                bottleDataType,
                Component.translatable("ferment.jei.recipe.bottle"),
                fullTexture(guiHelper, BOTTLE, 80, 60),
                guiHelper.createDrawableItemStack(BottleItem.bottle(FermentFluids.SALT_WATER)),
                (builder, recipe, focuses) -> {
                    builder.addSlot(RecipeIngredientRole.INPUT, 8, 4)
                            .addIngredients(recipe.addition());

                    FluidStack[] fluidStacks = recipe.bottle().getStacks();
                    ItemStack[] stacks = new ItemStack[fluidStacks.length];
                    for (int i = 0; i < fluidStacks.length; i++) {
                        stacks[i] = BottleItem.bottle(fluidStacks[i].getFluid());
                    }
                    builder.addSlot(RecipeIngredientRole.INPUT, 16, 40)
                            .addIngredients(Ingredient.of(stacks));

                    builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 22)
                            .addItemStack(BottleItem.bottle(recipe.output()));
                }
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(fermenterRecipeType, Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(FermentRecipeTypes.FERMENTER.get()));
        Minecraft.getInstance().level.registryAccess().registry(FermentDataPacks.BOTTLE)
                .ifPresent(reg -> registration.addRecipes(bottleDataType, reg.holders().map(Holder::value).toList()));
    }
}
