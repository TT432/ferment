package io.github.tt432.ferment.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.tt432.ferment.client.InWorldGuiGraphics;
import io.github.tt432.ferment.common.block.entity.FermenterBlockEntity;
import io.github.tt432.ferment.common.ui.Slot;
import io.github.tt432.ferment.common.ui.SlotSet;
import io.github.tt432.ferment.network.SlotClickPacket;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.RayAabIntersection;
import org.joml.Vector3f;

/**
 * @author TT432
 */
@RequiredArgsConstructor
public class FermenterRenderer implements BlockEntityRenderer<FermenterBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public static final ResourceLocation ARROW = ResourceLocation.withDefaultNamespace("ferment/arrow");
    public static final ResourceLocation SLOT = ResourceLocation.withDefaultNamespace("ferment/slot");
    public static final ResourceLocation FERMENTER_BUBBLE = ResourceLocation.withDefaultNamespace("ferment/fermenter_bubble");

    @Override
    public boolean shouldRender(FermenterBlockEntity pBlockEntity, Vec3 pCameraPos) {
        BlockPos pos = pBlockEntity.getBlockPos();

        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos)
                && Minecraft.getInstance().cameraEntity != null
                && Minecraft.getInstance().cameraEntity.position().distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 25;
    }

    @Override
    public void render(FermenterBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var pGuiGraphics = new InWorldGuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        poseStack.pushPose();

        Matrix4f m4 = poseStack.last().pose();
        m4.translate(0.5F, 1.5F, 0.5F);
        //m4.rotateZ(180 * EyeMath.DEGREES_TO_RADIANS);
        m4.rotate(camera.rotation());
        m4.scale(1, -1, 1);
        int multiplayer = 16 * 16 / 3;
        m4.scale(1F / multiplayer);

        pGuiGraphics.pose().poseStack.addLast(poseStack.last());

        pGuiGraphics.blitSprite(ARROW, -20, -35, 60, 16);

        if (blockEntity.processing) {
            pGuiGraphics.blitSprite(FERMENTER_BUBBLE, -6, -51, 32, 32);
        }

        BlockPos pos = blockEntity.getBlockPos();
        SlotSet slots = blockEntity.slots;
        FermenterBlockEntity.Data data = blockEntity.getData();
        NonNullList<ItemStack> inventory = data.getInventory();
        NonNullList<FluidStack> fluidStacks = data.getFluidStacks();

        renderItemSlot(pGuiGraphics, slots.get(0).slot(), inventory.getFirst());
        renderItemSlot(pGuiGraphics, slots.get(1).slot(), inventory.get(1));
        renderFluidWithSlot(pGuiGraphics, slots.get(3).slot(), fluidStacks.getFirst().getFluid());

        renderItemSlot(pGuiGraphics, slots.get(2).slot(), inventory.get(2));
        renderFluidWithSlot(pGuiGraphics, slots.get(4).slot(), fluidStacks.get(1).getFluid());

        testSlots(pGuiGraphics, slots, pos);

        poseStack.popPose();
    }

    private boolean hover(PoseStack poseStack, int x, int y, int w, int h) {
        Matrix4f m4 = poseStack.last().pose();

        Vector3f v1 = m4.transformPosition(new Vector3f(x, y, 0));
        Vector3f v2 = m4.transformPosition(new Vector3f(x, y + h, 0));
        Vector3f v3 = m4.transformPosition(new Vector3f(x + w, y + h, 0));
        Vector3f v4 = m4.transformPosition(new Vector3f(x + w, y, 0));

        Entity cameraEntity = Minecraft.getInstance().cameraEntity;

        if (cameraEntity != null) {
            var view = cameraEntity.getViewVector(Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));

            RayAabIntersection ray = new RayAabIntersection(
                    (float) 0,
                    (float) 0,
                    (float) 0,
                    (float) view.x,
                    (float) view.y,
                    (float) view.z
            );

            var maxX = Math.max(Math.max(v1.x, v2.x), Math.max(v3.x, v4.x));
            var minX = Math.min(Math.min(v1.x, v2.x), Math.min(v3.x, v4.x));

            var maxY = Math.max(Math.max(v1.y, v2.y), Math.max(v3.y, v4.y));
            var minY = Math.min(Math.min(v1.y, v2.y), Math.min(v3.y, v4.y));

            var maxZ = Math.max(Math.max(v1.z, v2.z), Math.max(v3.z, v4.z));
            var minZ = Math.min(Math.min(v1.z, v2.z), Math.min(v3.z, v4.z));

            return ray.test(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return false;
    }

    private void renderFluidWithSlot(GuiGraphics guiGraphics, Slot slot, Fluid fluid) {
        renderSlot(guiGraphics, slot);

        int x = slot.x();
        int y = slot.y();
        int w = slot.w();
        int h = slot.h();

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation stillTexture = extensions.getStillTexture();

        if (stillTexture != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int color = extensions.getTintColor();

            blitTiledSprite(guiGraphics, sprite,
                    x + 1, y + 1, 1,
                    w - 2, h - 2,
                    0, 0,
                    sprite.contents().width(), sprite.contents().height(),
                    sprite.contents().width(), sprite.contents().height(),
                    color);
        }
    }

    private void blitTiledSprite(GuiGraphics guiGraphics, TextureAtlasSprite pSprite, int pX, int pY, int pBlitOffset,
                                 int pWidth, int pHeight, int pUPosition, int pVPosition,
                                 int pSpriteWidth, int pSpriteHeight, int pNineSliceWidth, int pNineSliceHeight, int color) {
        if (pWidth > 0 && pHeight > 0) {
            for (int i = 0; i < pWidth; i += pSpriteWidth) {
                int j = Math.min(pSpriteWidth, pWidth - i);

                for (int k = 0; k < pHeight; k += pSpriteHeight) {
                    int l = Math.min(pSpriteHeight, pHeight - k);
                    blitSprite(guiGraphics, pSprite, pNineSliceWidth, pNineSliceHeight,
                            pUPosition, pVPosition, pX + i, pY + k, pBlitOffset, j, l, color);
                }
            }
        }
    }

    private VertexConsumer cutout(ResourceLocation texture) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityCutout(texture));
    }

    private void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite pSprite, int pTextureWidth, int pTextureHeight,
                            int pUPosition, int pVPosition, int pX, int pY, int pBlitOffset, int pUWidth, int pVHeight, int color) {
        if (pUWidth != 0 && pVHeight != 0) {
            ResourceLocation pAtlasLocation = pSprite.atlasLocation();
            float pMinU = pSprite.getU((float) pUPosition / (float) pTextureWidth);
            float pMaxU = pSprite.getU((float) (pUPosition + pUWidth) / (float) pTextureWidth);
            float pMinV = pSprite.getV((float) pVPosition / (float) pTextureHeight);
            float pMaxV = pSprite.getV((float) (pVPosition + pVHeight) / (float) pTextureHeight);

            Matrix4f matrix4f = guiGraphics.pose().last().pose();

            Vector3f v1 = matrix4f.transformPosition(new Vector3f((float) pX, (float) pY, (float) pBlitOffset));
            Vector3f v2 = matrix4f.transformPosition(new Vector3f((float) pX, (float) pY + pVHeight, (float) pBlitOffset));
            Vector3f v3 = matrix4f.transformPosition(new Vector3f((float) pX + pUWidth, (float) pY + pVHeight, (float) pBlitOffset));
            Vector3f v4 = matrix4f.transformPosition(new Vector3f((float) pX + pUWidth, (float) pY, (float) pBlitOffset));

            VertexConsumer buffer = cutout(pAtlasLocation);

            buffer.addVertex(v1.x, v1.y, v1.z, color, pMinU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
            buffer.addVertex(v2.x, v2.y, v2.z, color, pMinU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
            buffer.addVertex(v3.x, v3.y, v3.z, color, pMaxU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
            buffer.addVertex(v4.x, v4.y, v4.z, color, pMaxU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        }
    }

    protected void testSlots(GuiGraphics pGuiGraphics, SlotSet slotSet, BlockPos pos) {
        if (Minecraft.getInstance().mouseHandler.isRightPressed()) {
            for (SlotSet.SlotWithIndex value : slotSet.map().values()) {
                Slot slot = value.slot();

                if (hover(pGuiGraphics.pose(), slot.x(), slot.y(), slot.w(), slot.h())) {
                    PacketDistributor.sendToServer(new SlotClickPacket(pos, value.index()));
                }
            }
        }
    }

    protected void renderItemSlot(GuiGraphics pGuiGraphics, Slot slot, ItemStack itemstack) {
        renderSlot(pGuiGraphics, slot);
        renderSlotContents(pGuiGraphics, slot.x() + 1, slot.y() + 1, itemstack);
    }

    private static void renderSlot(GuiGraphics pGuiGraphics, Slot slot) {
        pGuiGraphics.blitSprite(SLOT, slot.x(), slot.y(), slot.w(), slot.h());
    }

    protected void renderSlotContents(GuiGraphics guiGraphics, int x, int y, ItemStack itemstack) {
        guiGraphics.renderFakeItem(itemstack, x, y, x + y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemstack, x, y, null);
    }
}
