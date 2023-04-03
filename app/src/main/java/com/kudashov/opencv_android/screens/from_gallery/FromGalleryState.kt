package com.kudashov.opencv_android.screens.from_gallery

import android.graphics.Bitmap

data class FromGalleryState(
    val sourceBitmap: Bitmap? = null,
    val resultSdkBitmap: Bitmap? = null,
    val resultNdkBitmap: Bitmap? = null,
    val sdkTime: Double? = null,
    val ndkTime: Double? = null
)