package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import kotlinx.coroutines.Deferred

interface ImageProcessor {

    fun blurAsync(bitmap: Bitmap, sigma: Int): Deferred<Bitmap>

    fun meanShiftAsync(bitmap: Bitmap): Deferred<Bitmap>
}