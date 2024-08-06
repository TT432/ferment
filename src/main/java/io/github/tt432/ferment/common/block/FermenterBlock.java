package io.github.tt432.ferment.common.block;

import io.github.tt432.ferment.common.block.entity.FermenterBlockEntity;
import io.github.tt432.ferment.common.item.FermentBottles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * @author TT432
 */
public class FermenterBlock extends Block implements EntityBlock {
    public FermenterBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FermenterBlockEntity(pPos, pState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);

        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity instanceof FermenterBlockEntity fbe) {
            ItemStack stack = pPlayer.getItemInHand(pHand);
            Item item = stack.getItem();
            Fluid fluid = FermentBottles.getFluid(stack);

            if (pPlayer.isCrouching()) {
                var inventory = fbe.getData().getInventory();

                for (int i = 2; i >= 0; i--) {
                    if (!inventory.get(i).isEmpty()) {
                        ItemStack copy = inventory.get(i).copy();
                        inventory.get(i).shrink(1);
                        copy.setCount(1);
                        pPlayer.addItem(copy);
                        onChanged(pState, pLevel, pPos, fbe);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            } else {
                if (fbe.getData().getFluid() != Fluids.EMPTY && item == Items.GLASS_BOTTLE) {
                    stack.shrink(1);
                    pPlayer.addItem(FermentBottles.fillItemStack(fbe.getData().getFluid()));
                    fbe.getData().setFluid(Fluids.EMPTY);
                    onChanged(pState, pLevel, pPos, fbe);
                    return ItemInteractionResult.SUCCESS;
                } else if (fbe.getData().getOutputFluid() != Fluids.EMPTY && item == Items.GLASS_BOTTLE) {
                    stack.shrink(1);
                    pPlayer.addItem(FermentBottles.fillItemStack(fbe.getData().getOutputFluid()));
                    fbe.getData().setOutputFluid(Fluids.EMPTY);
                    onChanged(pState, pLevel, pPos, fbe);
                    return ItemInteractionResult.SUCCESS;
                } else if (fluid != Fluids.EMPTY && fbe.getData().getFluid() == Fluids.EMPTY) {
                    pPlayer.setItemInHand(pHand, FermentBottles.fillItemStack(Fluids.EMPTY));
                    fbe.getData().setFluid(fluid);
                    onChanged(pState, pLevel, pPos, fbe);
                    return ItemInteractionResult.SUCCESS;
                } else if (!stack.isEmpty()) {
                    var inventory = fbe.getData().getInventory();
                    ItemStack copy = stack.copyWithCount(1);

                    if (inventory.get(0).isEmpty()) {
                        stack.shrink(1);
                        inventory.set(0, copy);
                        onChanged(pState, pLevel, pPos, fbe);
                        return ItemInteractionResult.SUCCESS;
                    } else if (inventory.get(1).isEmpty()) {
                        stack.shrink(1);
                        inventory.set(1, copy);
                        onChanged(pState, pLevel, pPos, fbe);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
        }

        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    private static void onChanged(BlockState pState, Level pLevel, BlockPos pPos, FermenterBlockEntity fbe) {
        fbe.setChanged();
        pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_CLIENTS);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (l, p, s, be) -> ((FermenterBlockEntity) be).tick();
    }
}
