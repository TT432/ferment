package io.github.tt432.ferment.common.block;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Ferment.MOD_ID);

    public static final DeferredBlock<LeavesBlock> APPLE_TREE_LEAVES =
            BLOCKS.registerBlock("apple_tree_leaves", LeavesBlock::new, leaves(SoundType.GRASS));

    public static final DeferredBlock<SaplingBlock> APPLE_SAPLING =
            BLOCKS.registerBlock("apple_sapling", p -> new SaplingBlock(FermentTreeGrowers.APPLE, p),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY));

    public static final DeferredBlock<FermenterBlock> FERMENTER =
            BLOCKS.registerBlock("fermenter", FermenterBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.WOOD)
                            .noOcclusion()
                            .sound(SoundType.WOOD)
                            .instrument(NoteBlockInstrument.BASS)
                            .strength(0.6F)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY));

    private static BlockBehaviour.Properties leaves(SoundType pSoundType) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(pSoundType)
                .noOcclusion()
                .isValidSpawn(FermentBlocks::ocelotOrParrot)
                .isSuffocating(FermentBlocks::never)
                .isViewBlocking(FermentBlocks::never)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(FermentBlocks::never);
    }

    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
}
