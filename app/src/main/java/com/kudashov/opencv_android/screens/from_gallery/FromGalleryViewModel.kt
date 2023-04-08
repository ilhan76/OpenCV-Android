package com.kudashov.opencv_android.screens.from_gallery

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.opencv_android.extensions.default
import com.kudashov.opencv_android.image_processor.NdkImageProcessor
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

class FromGalleryViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val sdkImageProcessor = SdkImageProcessor()
    private val ndkImageProcessor = NdkImageProcessor()

    private val _stateLiveData = MutableLiveData<FromGalleryState>().default(FromGalleryState())
    val stateLiveData: LiveData<FromGalleryState> = _stateLiveData

    private val state get() = stateLiveData.value

    fun imageLoaded(bitmap: Bitmap?) = _stateLiveData.postValue(FromGalleryState(sourceBitmap = bitmap))

    fun processImage() = launch(Dispatchers.IO) {
        state?.sourceBitmap?.let { bitmap ->
            val sdkResultBitmap: Bitmap
            val ndkResultBitmap: Bitmap
            val sdkTimeResult = measureTimeMillis {
               sdkResultBitmap = sdkImageProcessor.meanShift(bitmap)
            }

            val ndkTimeResult = measureTimeMillis {
                ndkResultBitmap = ndkImageProcessor.meanShift(bitmap)
            }

            _stateLiveData.postValue(
                state?.copy(
                    resultSdkBitmap = sdkResultBitmap,
                    resultNdkBitmap = ndkResultBitmap,
                    sdkTime = sdkTimeResult.toDouble() / 1000,
                    ndkTime = ndkTimeResult.toDouble() / 1000
                )
            )
        }
    }
}