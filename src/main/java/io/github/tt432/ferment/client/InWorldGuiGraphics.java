package io.github.tt432.ferment.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

/**
 * @author TT432
 */
public class InWorldGuiGraphics extends GuiGraphics {
    public InWorldGuiGraphics(Minecraft pMinecraft, MultiBufferSource.BufferSource pBufferSource) {
        super(pMinecraft, pBufferSource);
    }

    private VertexConsumer solid(ResourceLocation texture) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entitySolid(texture));
    }

    private VertexConsumer cutout(ResourceLocation texture) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityCutout(texture));
    }

    private VertexConsumer translucent(ResourceLocation texture) {
        return Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(texture));
    }

    @Override
    public void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, float pRed, float pGreen, float pBlue, float pAlpha) {
        Matrix4f matrix4f = this.pose().last().pose();

        Vector3f v1 = matrix4f.transformPosition(new Vector3f((float) pX1, (float) pY1, (float) pBlitOffset));
        Vector3f v2 = matrix4f.transformPosition(new Vector3f((float) pX1, (float) pY2, (float) pBlitOffset));
        Vector3f v3 = matrix4f.transformPosition(new Vector3f((float) pX2, (float) pY2, (float) pBlitOffset));
        Vector3f v4 = matrix4f.transformPosition(new Vector3f((float) pX2, (float) pY1, (float) pBlitOffset));

        int color = FastColor.ARGB32.color((int) (pAlpha * 255), (int) (pRed * 255), (int) (pGreen * 255), (int) (pBlue * 255));

        VertexConsumer buffer = translucent(pAtlasLocation);

        buffer.addVertex(v1.x, v1.y, v1.z, color, pMinU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v2.x, v2.y, v2.z, color, pMinU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v3.x, v3.y, v3.z, color, pMaxU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v4.x, v4.y, v4.z, color, pMaxU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
    }

    @Override
    public void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        Matrix4f matrix4f = pose().last().pose();
        Vector3f v1 = matrix4f.transformPosition(new Vector3f((float) pX1, (float) pY1, (float) pBlitOffset));
        Vector3f v2 = matrix4f.transformPosition(new Vector3f((float) pX1, (float) pY2, (float) pBlitOffset));
        Vector3f v3 = matrix4f.transformPosition(new Vector3f((float) pX2, (float) pY2, (float) pBlitOffset));
        Vector3f v4 = matrix4f.transformPosition(new Vector3f((float) pX2, (float) pY1, (float) pBlitOffset));

        VertexConsumer buffer = cutout(pAtlasLocation);

        buffer.addVertex(v1.x, v1.y, v1.z, 0xFF_FF_FF_FF, pMinU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v2.x, v2.y, v2.z, 0xFF_FF_FF_FF, pMinU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v3.x, v3.y, v3.z, 0xFF_FF_FF_FF, pMaxU, pMaxV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
        buffer.addVertex(v4.x, v4.y, v4.z, 0xFF_FF_FF_FF, pMaxU, pMinV, OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 0, 0, 1);
    }

    @Override
    public void renderItem(@Nullable LivingEntity pEntity, @Nullable Level pLevel, ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset) {
        if (!pStack.isEmpty()) {
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(pStack, pLevel, pEntity, pSeed);
            this.pose().pushPose();
            this.pose().translate((float) (pX + 8), (float) (pY + 8), (float) (bakedmodel.isGui3d() ? pGuiOffset : 0) + 2);

            try {
                this.pose().scale(16.0F, -16.0F, 16.0F);

                Minecraft.getInstance().getItemRenderer().render(pStack, ItemDisplayContext.GUI, false,
                        this.pose(), Minecraft.getInstance().renderBuffers().bufferSource(),
                        15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(pStack.getItem()));
                crashreportcategory.setDetail("Item Components", () -> String.valueOf(pStack.getComponents()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(pStack.hasFoil()));
                throw new ReportedException(crashreport);
            }

            this.pose().popPose();
        }
    }
}
