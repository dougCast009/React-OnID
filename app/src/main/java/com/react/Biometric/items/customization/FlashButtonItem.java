package com.react.Biometric.items.customization;

import android.content.Context;

import com.react.Biometric.items.CategoryItem;
import com.react.Biometric.items.customization.fragment.FlashButtonFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 7.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FlashButtonItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .registerUiFragmentClass(FlashButtonFragment.class)
                .setCameraSwitchEnabled(true)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getDescription() {
        return "Change flash button using default UI";
    }

    @Override
    public String getTitle() {
        return "Custom flash button";
    }
}
