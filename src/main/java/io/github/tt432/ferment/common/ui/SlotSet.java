package io.github.tt432.ferment.common.ui;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.With;

import java.util.List;

/**
 * @author TT432
 */
@With
public record SlotSet(
        Int2ObjectOpenHashMap<SlotWithIndex> map
) {
    public static final SlotSet EMPTY = new SlotSet(new Int2ObjectOpenHashMap<>());
    
    public SlotSet(List<Slot> list) {
        this(from(list));
    }
    
    public SlotWithIndex get(int index) {
        return map.get(index);
    }

    private static Int2ObjectOpenHashMap<SlotWithIndex> from(List<Slot> list) {
        Int2ObjectOpenHashMap<SlotWithIndex> result = new Int2ObjectOpenHashMap<>();

        for (int i = 0; i < list.size(); i++) {
            result.put(i, new SlotWithIndex(i, list.get(i)));
        }

        return result;
    }

    public record SlotWithIndex(
            int index,
            Slot slot
    ) {
    }
}
