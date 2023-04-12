package com.kudashov.opencv_android.screens.from_gallery

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kudashov.opencv_android.extensions.default
import com.kudashov.opencv_android.image_processor.NdkImageProcessor
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class FromGalleryViewModel : ViewModel() {

    private val sdkImageProcessor = SdkImageProcessor(viewModelScope)
    private val ndkImageProcessor = NdkImageProcessor(viewModelScope)

    private val _stateLiveData = MutableLiveData<FromGalleryState>().default(FromGalleryState())
    val stateLiveData: LiveData<FromGalleryState> = _stateLiveData

    private val state get() = stateLiveData.value

    fun imageLoaded(bitmap: Bitmap?) = _stateLiveData.postValue(FromGalleryState(sourceBitmap = bitmap))

    fun processImage() = viewModelScope.launch(Dispatchers.IO) {
        state?.sourceBitmap?.let { bitmap ->
            val sdkResultBitmap: Bitmap
            val ndkResultBitmap: Bitmap
            val sdkTimeResult = measureTimeMillis {
               sdkResultBitmap = sdkImageProcessor.meanShiftAsync(bitmap).await()
            }

            val ndkTimeResult = measureTimeMillis {
                ndkResultBitmap = ndkImageProcessor.meanShiftAsync(bitmap).await()
            }

            withContext(Dispatchers.Main) {
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
}