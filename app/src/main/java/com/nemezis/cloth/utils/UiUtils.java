package com.nemezis.cloth.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Dmitry Kostyrev on 12/10/15
 */
public class UiUtils {

    public static boolean isEmailValid(@NonNull String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
