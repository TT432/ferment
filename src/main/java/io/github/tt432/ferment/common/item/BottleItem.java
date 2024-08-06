package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static io.github.tt432.ferment.common.item.FermentItems.BOTTLE;

/**
 * @author TT432
 */
public class BottleItem extends Item {
    public BottleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack pStack) {
        ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(pStack.getOrDefault(FermentItemDataComponents.FLUID, Fluids.EMPTY));
        return this.getDescriptionId() + ".fluid." + fluidId.getNamespace() + "." + fluidId.getPath();
    }

    public static ItemStack bottle(Supplier<Fluid> fluid) {
        return bottle(fluid.get());
    }

    public static ItemStack bottle(Fluid fluid) {
        var stack = BOTTLE.toStack();
        stack.set(FermentItemDataComponents.FLUID, fluid);
        return stack;
    }
}
