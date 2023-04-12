package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class NdkImageProcessor(
    private val coroutineScope: CoroutineScope
) : ImageProcessor {

    override suspend fun blurAsync(bitmap: Bitmap, sigma: Int) = coroutineScope.async<Bitmap>(Dispatchers.Default) {
        val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        blur(bitmap, resultBitmap, sigma)
        resultBitmap
    }

    override suspend fun meanShiftAsync(bitmap: Bitmap) = coroutineScope.async<Bitmap>(Dispatchers.Default) {
        val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        meanShift(bitmap, resultBitmap)
        resultBitmap
    }

    private external fun meanShift(bitmapIn: Bitmap, bitmapOut: Bitmap)

    private external fun blur(bitmapIn: Bitmap, bitmapOut: Bitmap, sigma: Int)
}