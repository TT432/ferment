package io.github.tt432.ferment.item;

import io.github.tt432.ferment.Ferment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentItemGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Ferment.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    //Set the title of the tab. Don't forget to add a translation!
                    .title(Component.translatable("itemGroup." + Ferment.MOD_ID + ".example"))
                    //Set the icon of the tab.
                    .icon(FermentItems.BAD_APPLE::toStack)
                    //Add your items to the tab.
                    .displayItems((params, output) -> FermentItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get())))
                    .build()
    );
}
