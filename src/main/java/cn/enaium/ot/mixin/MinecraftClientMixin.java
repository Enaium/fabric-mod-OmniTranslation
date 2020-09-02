package cn.enaium.ot.mixin;

import cn.enaium.ot.Data;
import cn.enaium.ot.utils.FileUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "stop")
    private void stop(CallbackInfo callbackInfo) throws IOException {

        new File(MinecraftClient.getInstance().runDirectory.toString() + "/OmniTranslation").mkdir();

        File file = new File(MinecraftClient.getInstance().runDirectory.toString() + "/OmniTranslation/saveStringList.txt");


        if (!file.exists()) {
            file.createNewFile();
        }

        StringBuilder saveStringList = new StringBuilder();

        for (String s : Data.saveStringList) {
            saveStringList.append(s + "\n");
        }


        FileUtils.write(file, saveStringList.toString());
    }

}
