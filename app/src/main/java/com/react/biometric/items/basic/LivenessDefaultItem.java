package com.react.biometric.items.basic;

import android.content.Context;

import com.react.biometric.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessDefaultItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().startLiveness(context, livenessResponse -> { });
    }

    @Override
    public String getTitle() {
        return "Liveness";
    }

    @Override
    public String getDescription() {
        return "Detects if a person on camera is alive.";
    }
}