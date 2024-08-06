package io.github.tt432.ferment.common.datapack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

/**
 * @author TT432
 */
public record BottleData(
        Ingredient addition,
        FluidIngredient bottle,
        Fluid output
) {
    public static final Codec<BottleData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition),
            FluidIngredient.CODEC.fieldOf("bottle").forGetter(o -> o.bottle),
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("output").forGetter(o -> o.output)
    ).apply(ins, BottleData::new));
}
