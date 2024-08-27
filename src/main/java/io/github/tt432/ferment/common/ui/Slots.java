package io.github.tt432.ferment.common.ui;

import it.unimi.dsi.fastutil.Pair;
import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author TT432
 */
@UtilityClass
public class Slots {
    @FunctionalInterface
    public interface Fluid2Item {
        /**
         * 禁止修改 container，结果应假设 1 container + 1 bucket fluid = 1 result
         */
        @Nullable
        ItemStack apply(FluidStack stack, ItemStack container);
    }

    @FunctionalInterface
    public interface Item2Fluid {
        /**
         * 禁止修改 container，结果应假设 1 container = 1 bucket fluid + 1 result
         */
        @Nullable
        Pair<FluidStack, ItemStack> apply(ItemStack container);
    }

    public boolean fluidInput(Player player, List<FluidStack> fluidStackList, int index, Item2Fluid item2Fluid, Fluid2Item fluid2Item) {
        if (!fluidStackList.get(index).isEmpty()) {
            return fluidOutput(player, fluidStackList, index, fluid2Item);
        } else {
            ItemStack handItem = player.getMainHandItem();
            var apply = item2Fluid.apply(handItem);

            if (apply != null) {
                handItem.consume(1, player);

                if (!player.addItem(apply.right())) {
                    player.drop(apply.right(), false);
                }

                fluidStackList.set(index, apply.left());

                return true;
            }
        }

        return false;
    }

    public boolean fluidOutput(Player player, List<FluidStack> fluidStackList, int index, Fluid2Item fluid2Item) {
        if (!fluidStackList.get(index).isEmpty()) {
            ItemStack handItem = player.getMainHandItem();
            FluidStack stack = fluidStackList.get(index);
            ItemStack apply = fluid2Item.apply(stack, handItem);

            if (apply != null) {
                handItem.consume(1, player);
                stack.shrink(FluidType.BUCKET_VOLUME);

                if (!player.addItem(apply)) {
                    player.drop(apply, false);
                }

                return true;
            }
        }

        return false;
    }

    public boolean itemInput(Player player, List<ItemStack> inventory, int index) {
        ItemStack handItem = player.getMainHandItem();
        ItemStack invItem = inventory.get(index);

        if (handItem.isEmpty()) {
            return itemOutput(player, inventory, index);
        } else {
            if (invItem.isEmpty()) {
                inventory.set(index, handItem.split(1));
                return true;
            }
        }

        return false;
    }

    public boolean itemOutput(Player player, List<ItemStack> inventory, int index) {
        ItemStack handItem = player.getMainHandItem();
        ItemStack invItem = inventory.get(index);

        if (handItem.isEmpty()) {
            if (!invItem.isEmpty()) {
                inventory.set(index, ItemStack.EMPTY);

                if (!player.addItem(invItem)) {
                    player.drop(invItem, false);
                }

                return true;
            }
        }

        return false;
    }
}
