package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap

interface ImageProcessor {

    suspend fun blur(bitmap: Bitmap, sigma: Int): Bitmap

    suspend fun meanShift(bitmap: Bitmap): Bitmap
}