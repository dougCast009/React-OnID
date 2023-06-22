package com.react.Biometric.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface imageUpdate {
    void updateImageView(final ImageView imgPreview, final Bitmap previewBitmap, String message, final boolean flagComplete, int captureError);

}
