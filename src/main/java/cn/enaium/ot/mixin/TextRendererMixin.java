package cn.enaium.ot.mixin;

import cn.enaium.ot.Data;
import cn.enaium.ot.utils.Utils;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
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
    public abstract String mirror(String text);

    @Shadow
    protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, boolean seeThrough, int underlineColor, int light);

    @Shadow
    @Final
    private TextHandler handler;

    @Shadow
    @Final
    public int fontHeight;

    @Shadow @Final private static Vec3f FORWARD_SHIFT;

    /**
     * @author Enaium
     */
    @Overwrite
    private int drawInternal(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean mirror) throws IOException {

        if (Utils.getConfig("saveStringList").equals("true")) {

            Data.saveStringList.add(text);

        }

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
