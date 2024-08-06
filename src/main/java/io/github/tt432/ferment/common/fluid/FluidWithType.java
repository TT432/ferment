package io.github.tt432.ferment.common.fluid;

import lombok.AllArgsConstructor;
import net.minecraft.world.level.material.EmptyFluid;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Supplier;

/**
 * @author TT432
 */
@AllArgsConstructor
public class FluidWithType extends EmptyFluid {
    Supplier<FluidType> type;

    @Override
    public FluidType getFluidType() {
        return type.get();
    }
}
