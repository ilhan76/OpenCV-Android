package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap

class NdkImageProcessor: ImageProcessor {

    override suspend fun blur(bitmap: Bitmap, sigma: Int): Bitmap {
        val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        blur(bitmap, resultBitmap, sigma)
        return resultBitmap
    }

    override suspend fun meanShift(bitmap: Bitmap): Bitmap {
        val resultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        meanShift(bitmap, resultBitmap)
        return resultBitmap
    }

    private external fun meanShift(bitmapIn: Bitmap, bitmapOut: Bitmap)

    private external fun blur(bitmapIn: Bitmap, bitmapOut: Bitmap, sigma: Int)
}