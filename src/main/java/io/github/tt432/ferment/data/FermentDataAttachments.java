package io.github.tt432.ferment.data;

import io.github.tt432.ferment.Ferment;
import io.github.tt432.ferment.common.ui.SlotSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * @author TT432
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FermentDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> REG =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Ferment.MOD_ID);
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SlotSet>> SLOT_SET=
            REG.register("slot_set", ()-> AttachmentType.<SlotSet>builder(()->SlotSet.EMPTY).build());
}
