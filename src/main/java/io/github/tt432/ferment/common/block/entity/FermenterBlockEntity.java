package io.github.tt432.ferment.common.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.tt432.ferment.common.item.FermentBottles;
import io.github.tt432.ferment.common.recipe.FermentRecipeTypes;
import io.github.tt432.ferment.common.recipe.FermenterRecipe;
import io.github.tt432.ferment.common.recipe.FermenterRecipeInput;
import io.github.tt432.ferment.common.ui.Slot;
import io.github.tt432.ferment.common.ui.SlotSet;
import io.github.tt432.ferment.common.ui.Slots;
import io.github.tt432.ferment.data.FermentDataAttachments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author TT432
 */
public class FermenterBlockEntity extends BlockEntity {
    @Getter
    @Setter
    private Data data = new Data(
            NonNullList.withSize(3, ItemStack.EMPTY),
            NonNullList.withSize(2, FluidStack.EMPTY),
            0
    );
    private final FermenterRecipeInput input = new FermenterRecipeInput(this);

    private final RecipeManager.CachedCheck<FermenterRecipeInput, FermenterRecipe> check =
            RecipeManager.createCheck(FermentRecipeTypes.FERMENTER.get());

    private void update() {
        setChanged();
        BlockState state = getBlockState();
        level.sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_CLIENTS);
    }

    public final SlotSet slots = new SlotSet(List.of(
            new Slot(-53, -51, 18, 18, player -> {
                if (Slots.itemInput(player, data.inventory, 0)) update();
            }),
            new Slot(-53, -30, 18, 18, player -> {
                if (Slots.itemInput(player, data.inventory, 1)) update();
            }),
            new Slot(33, 11, 18, 18, player -> {
                if (Slots.itemOutput(player, data.inventory, 2)) update();
            }),
            new Slot(-76, -51, 20, 80, player -> {
                if (Slots.fluidInput(player, data.fluidStacks, 0, FermentBottles.GET_BOTTLE_FLUID, FermentBottles.FILL_BOTTLE))
                    update();
            }),
            new Slot(54, -51, 20, 80, player -> {
                if (Slots.fluidOutput(player, data.fluidStacks, 1, FermentBottles.FILL_BOTTLE)) update();
            })
    ));

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Data {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ItemStack.OPTIONAL_CODEC.listOf().xmap(
                        lst -> NonNullList.of(ItemStack.EMPTY, lst.toArray(ItemStack[]::new)),
                        Function.identity()
                ).fieldOf("inventory").forGetter(Data::getInventory),
                FluidStack.OPTIONAL_CODEC.listOf().xmap(
                        lst -> NonNullList.of(FluidStack.EMPTY, lst.toArray(FluidStack[]::new)),
                        Function.identity()
                ).fieldOf("fluid_stacks").forGetter(Data::getFluidStacks),
                Codec.INT.optionalFieldOf("processing_tick", 0).forGetter(Data::getProcessingTick)
        ).apply(ins, Data::new));

        private NonNullList<ItemStack> inventory;
        private NonNullList<FluidStack> fluidStacks;
        private int processingTick;
    }

    /**
     * client only
     */
    public boolean processing;

    public FermenterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(FermentBlockEntities.FERMENTER.get(), pPos, pBlockState);

        setData(FermentDataAttachments.SLOT_SET, slots);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);

        loadData(tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag result = super.getUpdateTag(pRegistries);
        saveData(result);
        result.putBoolean("processing", getData().getProcessingTick() > 0);
        return result;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(tag, pRegistries);
        loadData(tag);
        processing = tag.contains("processing", Tag.TAG_BYTE) && tag.getBoolean("processing");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        saveData(pTag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void saveData(CompoundTag result) {
        result.put("data", Data.CODEC.encodeStart(NbtOps.INSTANCE, getData()).getOrThrow());
    }

    private void loadData(CompoundTag tag) {
        if (tag.contains("data", Tag.TAG_COMPOUND)) {
            setData(Data.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("data")).getOrThrow());
        }
    }

    public void tick() {
        if (level.isClientSide()) return;
        Optional<RecipeHolder<FermenterRecipe>> processingRecipe = check.getRecipeFor(input, level);

        if (processingRecipe.isPresent()) {
            FermenterRecipe recipe = processingRecipe.get().value();

            if (recipe.matches(input, level)) {
                getData().setProcessingTick(getData().getProcessingTick() + 1);
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
                setChanged();

                if (getData().getProcessingTick() >= recipe.time()) {
                    getData().getInventory().set(2, recipe.assemble(input, level.registryAccess()));
                    resetRecipe();
                }
            } else {
                resetRecipe();
            }
        } else {
            if (getData().getProcessingTick() > 0) {
                resetRecipe();
            }
        }
    }

    private void resetRecipe() {
        getData().setProcessingTick(0);
        update();
    }
}
