package com.kudashov.opencv_android

import android.graphics.Bitmap

data class MainState(
    val bitmap: Bitmap? = null,
    val sdkTime: Double? = null,
    val ndkTime: Double? = null
)