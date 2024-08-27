package io.github.tt432.ferment.common.ui;

import net.minecraft.world.entity.player.Player;

/**
 * @author TT432
 */
public record Slot(
        int x,
        int y,
        int w,
        int h,
        ClickAction clickAction
) {
    public interface ClickAction {
        void onClick(Player player);
    }
}
