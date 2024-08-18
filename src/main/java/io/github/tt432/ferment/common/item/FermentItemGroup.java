package io.github.tt432.ferment.common.item;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.fluid.FermentFluids;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentItemGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Ferment.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable(Ferment.MOD_ID + ".main"))
                    .icon(FermentItems.BAD_APPLE::toStack)
                    .displayItems((params, output) -> {
                        FermentItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get()));
                        output.accept(BottleItem.bottle(FermentFluids.SALT_WATER));
                        output.accept(BottleItem.bottle(FermentFluids.CIDER));
                        output.accept(BottleItem.bottle(FermentFluids.ALE));
                    })
                    .build()
    );
}
