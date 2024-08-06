package io.github.tt432.ferment.client.gui.layer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.tt432.ferment.common.block.entity.FermenterBlockEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;

/**
 * @author TT432
 */
public class FermenterContentDisplayLayer implements LayeredDraw.Layer {

    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");

    public static final ResourceLocation ARROW = ResourceLocation.withDefaultNamespace("ferment/arrow");
    public static final ResourceLocation SLOT = ResourceLocation.withDefaultNamespace("ferment/slot");
    public static final ResourceLocation FERMENTER_BUBBLE = ResourceLocation.withDefaultNamespace("ferment/fermenter_bubble");

    @Override
    public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (!(mc.hitResult instanceof BlockHitResult bhr)) return;

        BlockPos blockPos = bhr.getBlockPos();
        ClientLevel level = mc.level;

        if (level == null) return;

        if (level.getBlockEntity(blockPos) instanceof FermenterBlockEntity fbe) {
            int h = pGuiGraphics.guiHeight();
            int centerY = h / 2;
            int w = pGuiGraphics.guiWidth();
            int centerX = w / 2;

            pGuiGraphics.blitSprite(ARROW, centerX - 20, centerY - 35, 60, 16);

            if (fbe.processing) {
                pGuiGraphics.blitSprite(FERMENTER_BUBBLE, centerX - 6, centerY - 51, 32, 32);
            }

            renderSlot(pGuiGraphics, centerX - 33 - 20, centerY - 51, fbe.getData().getInventory().getFirst());
            renderSlot(pGuiGraphics, centerX - 33 - 20, centerY - 30, fbe.getData().getInventory().get(1));
            renderFluidWithSlot(pGuiGraphics, fbe.getData().getFluid(), centerX - 33 - 43, centerY - 51, 20, 80);

            renderSlot(pGuiGraphics, centerX + 33, centerY + 11, fbe.getData().getInventory().getLast());
            renderFluidWithSlot(pGuiGraphics, fbe.getData().getOutputFluid(), centerX + 33 + 21, centerY - 51, 20, 80);
        }
    }

    private void renderFluidWithSlot(GuiGraphics guiGraphics, Fluid fluid, int x, int y, int w, int h) {
        guiGraphics.blitSprite(SLOT, x, y, w, h);

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation stillTexture = extensions.getStillTexture();

        if (stillTexture != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
            int color = extensions.getTintColor();

            blitTiledSprite(guiGraphics, sprite,
                    x + 1, y + 1, 100,
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

    private void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite pSprite, int pTextureWidth, int pTextureHeight,
                            int pUPosition, int pVPosition, int pX, int pY, int pBlitOffset, int pUWidth, int pVHeight, int color) {
        if (pUWidth != 0 && pVHeight != 0) {
            ResourceLocation pAtlasLocation = pSprite.atlasLocation();
            float pMinU = pSprite.getU((float) pUPosition / (float) pTextureWidth);
            float pMaxU = pSprite.getU((float) (pUPosition + pUWidth) / (float) pTextureWidth);
            float pMinV = pSprite.getV((float) pVPosition / (float) pTextureHeight);
            float pMaxV = pSprite.getV((float) (pVPosition + pVHeight) / (float) pTextureHeight);
            RenderSystem.setShaderTexture(0, pAtlasLocation);
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            Matrix4f matrix4f = guiGraphics.pose().last().pose();
            BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.addVertex(matrix4f, (float) pX, (float) pY, (float) pBlitOffset).setUv(pMinU, pMinV).setColor(color);
            bufferbuilder.addVertex(matrix4f, (float) pX, (float) (pY + pVHeight), (float) pBlitOffset).setUv(pMinU, pMaxV).setColor(color);
            bufferbuilder.addVertex(matrix4f, (float) (pX + pUWidth), (float) (pY + pVHeight), (float) pBlitOffset).setUv(pMaxU, pMaxV).setColor(color);
            bufferbuilder.addVertex(matrix4f, (float) (pX + pUWidth), (float) pY, (float) pBlitOffset).setUv(pMaxU, pMinV).setColor(color);
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        }
    }

    void innerBlit(
            GuiGraphics guiGraphics,
            ResourceLocation pAtlasLocation,
            int pX1,
            int pX2,
            int pY1,
            int pY2,
            int pBlitOffset,
            float pMinU,
            float pMaxU,
            float pMinV,
            float pMaxV
    ) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float) pX1, (float) pY1, (float) pBlitOffset).setUv(pMinU, pMinV);
        bufferbuilder.addVertex(matrix4f, (float) pX1, (float) pY2, (float) pBlitOffset).setUv(pMinU, pMaxV);
        bufferbuilder.addVertex(matrix4f, (float) pX2, (float) pY2, (float) pBlitOffset).setUv(pMaxU, pMaxV);
        bufferbuilder.addVertex(matrix4f, (float) pX2, (float) pY1, (float) pBlitOffset).setUv(pMaxU, pMinV);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    protected void renderSlot(GuiGraphics pGuiGraphics, int x, int y, ItemStack itemstack) {
        pGuiGraphics.blitSprite(SLOT_SPRITE, x, y, 0, 18, 18);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        renderSlotContents(pGuiGraphics, x + 1, y + 1, itemstack);

        pGuiGraphics.pose().popPose();
    }

    protected void renderSlotContents(GuiGraphics guiGraphics, int x, int y, ItemStack itemstack) {
        guiGraphics.renderFakeItem(itemstack, x, y, x + y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemstack, x, y, null);
    }
}
