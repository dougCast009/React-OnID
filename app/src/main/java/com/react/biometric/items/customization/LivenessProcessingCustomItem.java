package com.react.biometric.items.customization;

import android.content.Context;

import com.react.biometric.items.CategoryItem;
import com.react.biometric.items.customization.fragment.LivenessProcessingCustomFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 29.04.22.
 * Copyright (c) 2022 Regula. All rights reserved.
 */

public class LivenessProcessingCustomItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      LivenessConfiguration configuration = new LivenessConfiguration.Builder()
              .registerProcessingFragment(LivenessProcessingCustomFragment.class)
              .build();
      FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
   }

   @Override
   public String getDescription() {
      return "Customize liveness processing and retry screens";
   }

   @Override
   public String getTitle() {
      return "Custom liveness process";
   }
}
