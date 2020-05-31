package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.class_5348;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
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
public class TextRendererMixin {

    @Shadow
    @Final
    private TextHandler handler;

    /**
     * @author Enaium
     */
    @Overwrite
    private int draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow, boolean mirror) {
        if (text == null) {
            return 0;
        } else {
            System.out.println(text);
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
    public int getWidth(String text) {
        return MathHelper.ceil(this.handler.getWidth(Utils.getKey(text)));
    }
}
