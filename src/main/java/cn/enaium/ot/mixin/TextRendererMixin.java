package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.StringRenderable;
import net.minecraft.util.Formatting;
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
    public abstract String mirror(String text);

    @Shadow
    protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light);

    @Shadow
    @Final
    private static Vector3f FORWARD_SHIFT;

    @Shadow
    @Final
    private TextHandler handler;

    /**
     * @author Enaium
     */
    @Overwrite
    private int drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean mirror) {
        String temp = text;
        for (char s : text.toCharArray()) {
            for (int i = 0; i < text.length(); ++i) {
                char c = text.charAt(i);
                if (c == 167 && i < text.length() - 1) {
                    ++i;
                    text = text.replaceAll(Formatting.byCode(text.charAt(i)).toString(), "").replaceAll("^[　 ]*", "").replaceAll("[　 ]*$", "");
                }
            }
        }

        String string = Utils.getKey(text);

        if (!Utils.has(text)) {
            string = temp;
        }

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

    private static int tweakTransparency(int argb) {
        return (argb & -67108864) == 0 ? argb | -16777216 : argb;
    }

    /**
     * @author Enaium
     */
    @Overwrite
    public int getWidth(String text) {
        return MathHelper.ceil(this.handler.getWidth(Utils.getKey(text)));
    }

}
