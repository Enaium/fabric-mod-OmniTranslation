package cn.enaium.ot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class Utils {
    public static String getKey(String key) {
        try {
            InputStream inputStream = Utils.class.getResourceAsStream("/assets/ot/zh_cn.json");
            String string = IOUtils.toString(inputStream, "utf-8");
            return new Gson().fromJson(string, JsonObject.class).get(key).getAsString();
        } catch (Exception e) {
            return key;
        }
    }
}
