package com.kudashov.opencv_android

import android.graphics.Bitmap

data class MainState(
    val sourceBitmap: Bitmap? = null,
    val resultSdkBitmap: Bitmap? = null,
    val resultNdkBitmap: Bitmap? = null,
    val sdkTime: Double? = null,
    val ndkTime: Double? = null
)