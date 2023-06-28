package com.react.biometric.items.customization.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.react.biometric.R;
import com.regula.facesdk.fragment.FaceUiFragment;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class AdvancedCustomUiFragment extends FaceUiFragment {

    private ImageButton mSwapCameraBtn;
    private ImageButton mFlashLightBtn;
    private ImageButton mCloseBtn;

    @Override
    public View getSwapCameraButton(@NonNull View v) {
        mSwapCameraBtn = v.findViewById(R.id.swapCameraButton);
        return mSwapCameraBtn;
    }

    @Override
    public View getFlashLightButton(@NonNull View v) {
        mFlashLightBtn = v.findViewById(R.id.lightButton);
        return mFlashLightBtn;
    }

    @Override
    public TextView getNotificationView(@NonNull View v) {
        return v.findViewById(R.id.notificationTextView);
    }

    @Override
    public View getCloseButton(View v) {
        mCloseBtn = v.findViewById(R.id.exitButton);
        return mCloseBtn;
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.custom_toolbar_appearance;
    }

    @Override
    public void onLightButtonClicked(boolean isLightOn) {
        super.onLightButtonClicked(isLightOn);
        mFlashLightBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),isLightOn ? R.drawable.flash_light_on : R.drawable.flash_light_off));
    }

    @Override
    public void onScreenLightChanged(boolean isScreenLightOn) {
        int buttonsColor = getButtonsColor(isScreenLightOn);

        mCloseBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
        mFlashLightBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
        mSwapCameraBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
    }

    protected int getButtonsColor(boolean isLightOn) {
        return isLightOn ? Color.BLACK : Color.WHITE;
    }
}
