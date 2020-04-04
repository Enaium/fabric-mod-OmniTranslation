package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
@Mixin(TextRenderer.class)
public class TextRendererMixin {

	@Overwrite
	private int draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow) throws IOException {
		if (text == null) {
			return 0;
		} else {
			String string = Utils.getKey(text);
			VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
			int i = MinecraftClient.getInstance().textRenderer.draw(string, x, y, color, shadow, matrix, immediate, false, 0, 15728880);
			immediate.draw();
			return i;
		}
	}
}
