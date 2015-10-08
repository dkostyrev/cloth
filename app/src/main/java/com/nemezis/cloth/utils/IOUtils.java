package com.nemezis.cloth.utils;

import android.support.annotation.NonNull;

import java.io.Closeable;

/**
 * Created by Dmitry Kostyrev on 08/10/15
 */
public class IOUtils {

    public static void closeSilently(@NonNull Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            // Ignore
        }
    }
}
