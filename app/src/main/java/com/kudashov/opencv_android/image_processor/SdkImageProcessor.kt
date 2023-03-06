package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class SdkImageProcessor : ImageProcessor {

    override suspend fun blur(bitmap: Bitmap, sigma: Double): Bitmap {
        return withContext(Dispatchers.IO) {
            val imageSrc = Mat()

            Utils.bitmapToMat(bitmap, imageSrc)

            val destination = Mat()
            Imgproc.GaussianBlur(imageSrc, destination, Size(), sigma)

            val copy = Bitmap.createBitmap(
                destination.width(),
                destination.height(),
                Bitmap.Config.ARGB_8888
            )
            Utils.matToBitmap(destination, copy)
            copy
        }
    }
}