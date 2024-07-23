package io.github.tt432.ferment.block;

import io.github.tt432.ferment.world.FermentWorldgenKeys;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/**
 * @author TT432
 */
@NoArgsConstructor
public class FermentTreeGrowers {
    public static final TreeGrower APPLE = new TreeGrower(
            "apple",
            Optional.empty(),
            Optional.of(FermentWorldgenKeys.APPLE_TREE_CONFIGURED),
            Optional.empty()
    );
}
