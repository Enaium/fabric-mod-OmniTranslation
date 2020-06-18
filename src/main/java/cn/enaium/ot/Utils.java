package cn.enaium.ot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class Utils {

    public static String getKey(String key) {
        try {
            InputStream inputStream = Utils.class.getResourceAsStream("/assets/ot/lang/" + getConfig("language") + ".json");
            String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return new Gson().fromJson(string, JsonObject.class).get(key).getAsString();
        } catch (Exception e) {
            return key;
        }
    }

    public static boolean has(String key) {
        try {
            InputStream inputStream = Utils.class.getResourceAsStream("/assets/ot/lang/" + getConfig("language") + ".json");
            String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            new Gson().fromJson(string, JsonObject.class).get(key).getAsString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getConfig(String key) {
        try {
            InputStream inputStream = Utils.class.getResourceAsStream("/assets/ot/config.json");
            String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return new Gson().fromJson(string, JsonObject.class).get(key).getAsString();
        } catch (Exception e) {
            return key;
        }
    }

}
