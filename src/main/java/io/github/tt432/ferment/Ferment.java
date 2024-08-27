package io.github.tt432.ferment;

import io.github.tt432.ferment.common.block.FermentBlocks;
import io.github.tt432.ferment.common.block.entity.FermentBlockEntities;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import io.github.tt432.ferment.common.item.FermentItemGroup;
import io.github.tt432.ferment.common.item.FermentItems;
import io.github.tt432.ferment.common.item.componet.FermentItemDataComponents;
import io.github.tt432.ferment.common.recipe.FermentRecipeSerializers;
import io.github.tt432.ferment.common.recipe.FermentRecipeTypes;
import io.github.tt432.ferment.data.FermentDataAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * @author TT432
 */
@Mod(Ferment.MOD_ID)
public class Ferment {
    public static final String MOD_ID = "ferment";

    public Ferment(IEventBus bus) {
        FermentBlocks.BLOCKS.register(bus);
        FermentItems.ITEMS.register(bus);
        FermentItemGroup.CREATIVE_MODE_TABS.register(bus);
        FermentBlockEntities.BLOCK_ENTITIES.register(bus);
        FermentRecipeTypes.RECIPE_TYPES.register(bus);
        FermentRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        FermentFluids.FLUID_TYPES.register(bus);
        FermentFluids.FLUIDS.register(bus);
        FermentItemDataComponents.DATA_COMPONENTS.register(bus);
        FermentDataAttachments.REG.register(bus);
    }
}
