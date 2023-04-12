package com.kudashov.opencv_android.image_processor

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class SdkImageProcessor(
    private val coroutineScope: CoroutineScope
) : ImageProcessor {

    override suspend fun blurAsync(bitmap: Bitmap, sigma: Int) = coroutineScope.async<Bitmap>(
        Dispatchers.Default) {
        val imageSrc = Mat()

        Utils.bitmapToMat(bitmap, imageSrc)

        val destination = Mat()
        Imgproc.GaussianBlur(imageSrc, destination, Size(), sigma.toDouble(), sigma.toDouble())

        val copy = Bitmap.createBitmap(
            destination.width(),
            destination.height(),
            Bitmap.Config.ARGB_8888
        )
        Utils.matToBitmap(destination, copy)
        copy
    }

    override suspend fun meanShiftAsync(bitmap: Bitmap) = coroutineScope.async<Bitmap>(Dispatchers.Default) {
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
        copy
    }
}