package io.github.tt432.ferment.common.fluid;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Ferment.MOD_ID);

    public static final Supplier<FluidType> SLAT_WATER_TYPE = registerType("slat_water", 0xFFC2C2C2);
    public static final Supplier<FluidType> CIDER_TYPE = registerType("cider", 0xFFc78f8f);

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Ferment.MOD_ID);

    public static final Supplier<Fluid> SALT_WATER = FLUIDS.register("slat_water", () -> new FluidWithType(SLAT_WATER_TYPE));
    public static final Supplier<Fluid> CIDER = FLUIDS.register("cider", () -> new FluidWithType(CIDER_TYPE));

    private static DeferredHolder<FluidType, FluidType> registerType(String id, int color) {
        return FLUID_TYPES.register(id, () -> new FluidType(FluidType.Properties.create()
                .descriptionId("fluid.ferment." + id)
                .fallDistanceModifier(0.0F)
                .canExtinguish(true)
                .canConvertToSource(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
                .addDripstoneDripping(0.17578125F, ParticleTypes.DRIPPING_DRIPSTONE_WATER,
                        Blocks.WATER_CAULDRON, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
            public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
                if (reader instanceof Level level) {
                    return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
                } else {
                    return super.canConvertToSource(state, reader, pos);
                }
            }

            public @Nullable PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
                return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
            }

            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");
                    private static final ResourceLocation WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
                    private static final ResourceLocation WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
                    private static final ResourceLocation WATER_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");

                    public ResourceLocation getStillTexture() {
                        return WATER_STILL;
                    }

                    public ResourceLocation getFlowingTexture() {
                        return WATER_FLOW;
                    }

                    public ResourceLocation getOverlayTexture() {
                        return WATER_OVERLAY;
                    }

                    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                        return UNDERWATER_LOCATION;
                    }

                    public int getTintColor() {
                        return color;
                    }
                });
            }
        });
    }

}
