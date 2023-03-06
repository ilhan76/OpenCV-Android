package com.kudashov.opencv_android

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.coroutineScope

class MainViewModel : ViewModel() {

    private val imageProcessor = SdkImageProcessor()

    private val _imageLiveData = MutableLiveData<Bitmap>()
    val imageLiveData: LiveData<Bitmap> = _imageLiveData

    suspend fun blurImage(bitmap: Bitmap, sigma: Double) = coroutineScope {
        _imageLiveData.postValue(imageProcessor.blur(bitmap, sigma))
    }
}