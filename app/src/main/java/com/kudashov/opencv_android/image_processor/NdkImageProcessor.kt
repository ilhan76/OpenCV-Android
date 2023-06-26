package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class NdkImageProcessor(
    private val coroutineScope: CoroutineScope
) : ImageProcessor {

    override fun blurAsync(bitmap: Bitmap, sigma: Int) =
        coroutineScope.async<Bitmap>(Dispatchers.Default) {
            val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            blur(bitmap, resultBitmap, sigma)
            resultBitmap
        }

    override fun meanShiftAsync(bitmap: Bitmap) =
        coroutineScope.async(Dispatchers.Default) {
            meanShift(bitmap)
        }

    private external fun meanShift(bitmapIn: Bitmap) : Bitmap

    private external fun blur(bitmapIn: Bitmap, bitmapOut: Bitmap, sigma: Int)
}