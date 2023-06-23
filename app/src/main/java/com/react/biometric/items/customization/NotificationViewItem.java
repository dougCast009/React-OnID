package com.react.biometric.items.customization;

import android.content.Context;

import com.react.biometric.items.CategoryItem;
import com.react.biometric.items.customization.fragment.NotificationViewFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class NotificationViewItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .registerUiFragmentClass(NotificationViewFragment.class)
                .setCameraSwitchEnabled(true)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, faceCaptureResponse -> { });
    }

    @Override
    public String getDescription() {
        return "Custom notification background drawable based on Default UI";
    }

    @Override
    public String getTitle() {
        return "Custom notification background";
    }
}
