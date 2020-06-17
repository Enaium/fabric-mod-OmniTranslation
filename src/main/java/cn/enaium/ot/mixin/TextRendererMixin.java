package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {

    @Shadow
    @Final
    private FontStorage fontStorage;

    @Shadow
    private boolean rightToLeft;

    @Shadow
    public abstract String mirror(String text);

    @Shadow
    protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light);

    /**
     * @author Enaium
     */
    @Overwrite
    private int drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int backgroundColor, int light) {
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
        if(!Utils.has(text)) {
            string = temp;
        }
        if (this.rightToLeft) {
            string = this.mirror(string);
        }

        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        if (shadow) {
            this.drawLayer(string, x, y, color, true, matrix, vertexConsumerProvider, seeThrough, backgroundColor, light);
        }

        Matrix4f matrix4f = matrix.copy();
        matrix4f.addToLastColumn(new Vector3f(0.0F, 0.0F, 0.001F));
        x = this.drawLayer(string, x, y, color, false, matrix4f, vertexConsumerProvider, seeThrough, backgroundColor, light);
        return (int) x + (shadow ? 1 : 0);
    }

    /**
     * @author Enaium
     */
    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            String string = Utils.getKey(text);
            float f = 0.0F;
            boolean bl = false;

            for (int i = 0; i < string.length(); ++i) {
                char c = string.charAt(i);
                if (c == 167 && i < string.length() - 1) {
                    ++i;
                    Formatting formatting = Formatting.byCode(string.charAt(i));
                    if (formatting == Formatting.BOLD) {
                        bl = true;
                    } else if (formatting != null && formatting.affectsGlyphWidth()) {
                        bl = false;
                    }
                } else {
                    f += this.fontStorage.getGlyph(c).getAdvance(bl);
                }
            }
            return MathHelper.ceil(f);
        }
    }

}
