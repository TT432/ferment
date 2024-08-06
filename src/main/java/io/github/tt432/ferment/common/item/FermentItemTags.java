package io.github.tt432.ferment.common.item;

import io.github.tt432.eyelib.util.ResourceLocations;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentItemTags {
    // todo public static final TagKey<Item> LEAVEN = tag("leaven");

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocations.of("c", name));
    }
}
