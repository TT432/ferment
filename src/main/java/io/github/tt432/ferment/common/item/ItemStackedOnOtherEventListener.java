package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.common.datapack.FermentDataPacks;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EventBusSubscriber
public class ItemStackedOnOtherEventListener {
    @SubscribeEvent
    public static void onEvent(ItemStackedOnOtherEvent event) {
        event.getPlayer().level().registryAccess().registry(FermentDataPacks.BOTTLE).flatMap(reg -> reg.stream()
                .filter(data -> data.addition().test(event.getStackedOnItem())
                        && data.bottle().test(new FluidStack(FermentBottles.getFluid(event.getCarriedItem()), FluidType.BUCKET_VOLUME)))
                .findFirst()).ifPresent(data -> {
            event.getStackedOnItem().shrink(1);
            event.getSlot().set(FermentBottles.fillItemStack(data.output()));
            event.setCanceled(true);
        });
    }
}
