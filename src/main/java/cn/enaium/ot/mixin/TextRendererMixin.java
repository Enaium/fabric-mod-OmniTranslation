package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {

    @Shadow
    @Final
    private TextHandler handler;

    @Shadow
    public abstract String mirror(String text);

    @Shadow
    protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light);

    @Shadow
    @Final
    private static Vector3f FORWARD_SHIFT;

    /**
     * @author Enaium
     */
    @Overwrite
    private int draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow, boolean mirror) {
        if (text == null) {
            return 0;
        } else {
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            int i = MinecraftClient.getInstance().textRenderer.draw(Utils.getKey(text), x, y, color, shadow, matrix, immediate, false, 0, 15728880, mirror);
            immediate.draw();
            return i;
        }
    }

    /**
     * @author Enaium
     */
    @Overwrite
    private int drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean mirror) {
        String string = Utils.getKey(text);
        if (mirror) {
            string = this.mirror(string);
        }

        color = tweakTransparency(color);
        Matrix4f matrix4f = matrix.copy();
        if (shadow) {
            this.drawLayer(string, x, y, color, true, matrix, vertexConsumers, seeThrough, backgroundColor, light);
            matrix4f.addToLastColumn(FORWARD_SHIFT);
        }

        x = this.drawLayer(string, x, y, color, false, matrix4f, vertexConsumers, seeThrough, backgroundColor, light);
        return (int) x + (shadow ? 1 : 0);
    }

    /**
     * @author Enaium
     */
    @Overwrite
    public int getWidth(String text) {
        return MathHelper.ceil(this.handler.getWidth(Utils.getKey(text)));
    }

    private static int tweakTransparency(int argb) {
        return (argb & -67108864) == 0 ? argb | -16777216 : argb;
    }

}
