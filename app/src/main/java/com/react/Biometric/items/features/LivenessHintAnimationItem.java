package com.react.Biometric.items.features;

import android.content.Context;

import com.react.Biometric.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessHintAnimationItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setShowHelpTextAnimation(false)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getTitle() {
        return "Disable Liveness NotificationTextView animation";
    }

    @Override
    public String getDescription() {
        return "Disables blinking NotificationTextView animation for Liveness.";
    }
}
