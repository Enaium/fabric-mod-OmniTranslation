package cn.enaium.ot.mixin;

import cn.enaium.ot.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
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
	private FontStorage fontStorage;

	@Overwrite
	private int draw(String text, float x, float y, int color, Matrix4f matrix, boolean shadow) {
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

	@Overwrite
	public int getStringWidth(String text) {
		if (text == null) {
			return 0;
		} else {
			String string = Utils.getKey(text);
			float f = 0.0F;
			boolean bl = false;

			for(int i = 0; i < string.length(); ++i) {
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
