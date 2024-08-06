package io.github.tt432.ferment.common.item.componet;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentItemDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Ferment.MOD_ID);

    public static final Supplier<DataComponentType<Fluid>> FLUID =
            DATA_COMPONENTS.registerComponentType("fluid", builder -> builder
                    .persistent(BuiltInRegistries.FLUID.byNameCodec())
                    .networkSynchronized(ByteBufCodecs.registry(Registries.FLUID)));
}
