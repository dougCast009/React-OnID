package com.react.biometric.data;

import com.react.biometric.items.ICategoryItem;
import com.react.biometric.items.basic.FaceCaptureDefaultItem;
import com.react.biometric.items.basic.LivenessDefaultItem;
import com.react.biometric.items.basic.MatchFacesRequestItem;
import com.react.biometric.items.customization.BasicCustomItem;
import com.react.biometric.items.customization.AdvancedCustomItem;
import com.react.biometric.items.customization.ButtonsColorItem;
import com.react.biometric.items.customization.FlashButtonItem;
import com.react.biometric.items.customization.HideCloseButtonItem;
import com.react.biometric.items.customization.HideFlashButtonItem;
import com.react.biometric.items.customization.HideNotificationViewItem;
import com.react.biometric.items.customization.LivenessProcessingCustomItem;
import com.react.biometric.items.customization.NotificationViewItem;
import com.react.biometric.items.customization.NotificationViewPositionItem;
import com.react.biometric.items.customization.OverlayCustomItem;
import com.react.biometric.items.customization.SwapButtonItem;
import com.react.biometric.items.features.FaceCaptureCameraPositionItem;
import com.react.biometric.items.features.FaceCaptureHintAnimationItem;
import com.react.biometric.items.features.LivenessAttemptsCountItem;
import com.react.biometric.items.features.LivenessCameraSwitchItem;
import com.react.biometric.items.features.LivenessHintAnimationItem;
import com.react.biometric.items.other.LocalizationItem;
import com.react.biometric.items.other.NetworkInterceptorItem;

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
