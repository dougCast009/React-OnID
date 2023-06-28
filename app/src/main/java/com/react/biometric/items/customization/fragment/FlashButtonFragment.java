package com.react.biometric.items.customization.fragment;

import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.react.biometric.R;
import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 7.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FlashButtonFragment extends FaceDefaultUiFragment {

    View mFlashLightBtn;

    @Override
    public View getFlashLightButton(@NonNull View view) {
        mFlashLightBtn = super.getFlashLightButton(view);
        return mFlashLightBtn;
    }

    @Override
    public void updateFlashLightButton(boolean isLightOn) {
        if (mFlashLightBtn == null)
            return;

        if (mFlashLightBtn instanceof ImageButton)
            ((ImageButton) mFlashLightBtn).setImageDrawable(ContextCompat.getDrawable(getContext(),isLightOn ? R.drawable.flash_light_on : R.drawable.flash_light_off));
    }
}
