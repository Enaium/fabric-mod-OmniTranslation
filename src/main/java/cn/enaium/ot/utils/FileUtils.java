package cn.enaium.ot.utils;

import java.io.File;
import java.io.IOException;

/**
 * Project: OmniTranslation
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class FileUtils {

    public static String read(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
    }

    public static void write(File file, String s) throws IOException {
        org.apache.commons.io.FileUtils.writeStringToFile(file, s, "UTF-8");
    }

}
