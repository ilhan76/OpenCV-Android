package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class SdkImageProcessor : ImageProcessor {

    override suspend fun blur(bitmap: Bitmap, sigma: Double): Bitmap {
        val imageSrc = Mat()

        Utils.bitmapToMat(bitmap, imageSrc)

        val destination = Mat()
        Imgproc.GaussianBlur(imageSrc, destination, Size(), sigma, sigma)

        val copy = Bitmap.createBitmap(
            destination.width(),
            destination.height(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(destination, copy)
        return copy
    }

    override suspend fun meanShift(bitmap: Bitmap): Bitmap {
        val imageSrc = Mat()
        val destination = Mat()

        Utils.bitmapToMat(bitmap, imageSrc)
        Imgproc.cvtColor(imageSrc, imageSrc, Imgproc.COLOR_RGBA2RGB)

        Imgproc.pyrMeanShiftFiltering(
            imageSrc,
            destination,
            30.0,
            50.0,
            3
        )

        val copy = Bitmap.createBitmap(
            destination.width(),
            destination.height(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(destination, copy)
        return copy
    }
}