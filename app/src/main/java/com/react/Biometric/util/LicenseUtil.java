package com.react.Biometric.util;

import android.content.Context;

import com.react.Biometric.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sergey Yakimchik on 10.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LicenseUtil {

    private LicenseUtil() {
        throw new IllegalStateException("Utility class");
    }
    public static byte[] getLicense(Context context) {
        if (context == null)
            return null;

        InputStream licInput = context.getResources().openRawResource(R.raw.regula);
        int available;
        try {
            available = licInput.available();
        } catch (IOException e) {
            return null;
        }
        byte[] license = new byte[available];
        try {
            licInput.read(license);
        } catch (IOException e) {
            return null;
        }

        return license;
    }
}
