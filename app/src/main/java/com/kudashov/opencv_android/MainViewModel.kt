package com.kudashov.opencv_android

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.opencv_android.extensions.default
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val imageProcessor = SdkImageProcessor()

    private val _stateLiveData = MutableLiveData<MainState>().default(MainState())
    val stateLiveData: LiveData<MainState> = _stateLiveData

    private val state get() = stateLiveData.value

    fun imageLoaded(bitmap: Bitmap?) = _stateLiveData.postValue(MainState(sourceBitmap = bitmap))

    fun processImage() = launch(Dispatchers.IO) {
        state?.sourceBitmap?.let { bitmap ->
            val sdkTimeStart = System.currentTimeMillis()
            val sdkResultBitmap = imageProcessor.meanShift(bitmap)
            val sdkTimeEnd = System.currentTimeMillis()

            val ndkResultBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

            val ndkTimeStart = System.currentTimeMillis()
            meanShift(bitmap, ndkResultBitmap)
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

    private external fun meanShift(bitmapIn: Bitmap, bitmapOut: Bitmap)

    private external fun blur(bitmapIn: Bitmap, bitmapOut: Bitmap)
}
