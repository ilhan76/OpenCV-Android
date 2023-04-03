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
            val sdkTimeStart = System.currentTimeMillis()
            val sdkResultBitmap = sdkImageProcessor.meanShift(bitmap)
            val sdkTimeEnd = System.currentTimeMillis()

            val ndkTimeStart = System.currentTimeMillis()
            val ndkResultBitmap = ndkImageProcessor.meanShift(bitmap)
            val ndkTimeEnd = System.currentTimeMillis()

            _stateLiveData.postValue(
                state?.copy(
                    resultSdkBitmap = sdkResultBitmap,
                    resultNdkBitmap = ndkResultBitmap,
                    sdkTime = (sdkTimeEnd - sdkTimeStart).toDouble() / 1000,
                    ndkTime = (ndkTimeEnd - ndkTimeStart).toDouble() / 1000
                )
            )
        }

    }
}