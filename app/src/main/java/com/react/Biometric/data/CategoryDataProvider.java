package com.react.Biometric.data;

import com.react.Biometric.items.ICategoryItem;
import com.react.Biometric.items.basic.FaceCaptureDefaultItem;
import com.react.Biometric.items.basic.LivenessDefaultItem;
import com.react.Biometric.items.basic.MatchFacesRequestItem;
import com.react.Biometric.items.customization.BasicCustomItem;
import com.react.Biometric.items.customization.AdvancedCustomItem;
import com.react.Biometric.items.customization.ButtonsColorItem;
import com.react.Biometric.items.customization.FlashButtonItem;
import com.react.Biometric.items.customization.HideCloseButtonItem;
import com.react.Biometric.items.customization.HideFlashButtonItem;
import com.react.Biometric.items.customization.HideNotificationViewItem;
import com.react.Biometric.items.customization.LivenessProcessingCustomItem;
import com.react.Biometric.items.customization.NotificationViewItem;
import com.react.Biometric.items.customization.NotificationViewPositionItem;
import com.react.Biometric.items.customization.OverlayCustomItem;
import com.react.Biometric.items.customization.SwapButtonItem;
import com.react.Biometric.items.features.FaceCaptureCameraPositionItem;
import com.react.Biometric.items.features.FaceCaptureHintAnimationItem;
import com.react.Biometric.items.features.LivenessAttemptsCountItem;
import com.react.Biometric.items.features.LivenessCameraSwitchItem;
import com.react.Biometric.items.features.LivenessHintAnimationItem;
import com.react.Biometric.items.other.LocalizationItem;
import com.react.Biometric.items.other.NetworkInterceptorItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CategoryDataProvider {

    public List<ICategoryItem> getCategoryItems() {
        return new ArrayList<>(Arrays.asList(
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Basic";
                    }
                },
                new LivenessDefaultItem(),
                new FaceCaptureDefaultItem(),
                new MatchFacesRequestItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Feature Customization";
                    }
                },
                new LivenessCameraSwitchItem(),
                new LivenessAttemptsCountItem(),
                new LivenessHintAnimationItem(),
                new FaceCaptureCameraPositionItem(),
                new FaceCaptureHintAnimationItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "UI Customization";
                    }
                },
                new HideCloseButtonItem(),
                new HideFlashButtonItem(),
                new HideNotificationViewItem(),
                new NotificationViewItem(),
                new ButtonsColorItem(),
                new NotificationViewPositionItem(),
                new FlashButtonItem(),
                new SwapButtonItem(),
                new BasicCustomItem(),
                new AdvancedCustomItem(),
                new OverlayCustomItem(),
                new LivenessProcessingCustomItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Other";
                    }
                },
                new LocalizationItem(),
                new NetworkInterceptorItem()
        ));
    }

    abstract static class HeaderItem implements ICategoryItem {

        @Override
        public boolean isHeader() {
            return true;
        }
    }
}
