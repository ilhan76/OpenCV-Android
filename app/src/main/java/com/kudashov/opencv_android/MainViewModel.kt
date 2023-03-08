package com.kudashov.opencv_android

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun imageLoaded(bitmap: Bitmap?) {
        _stateLiveData.postValue(state?.copy(bitmap = bitmap))
    }

    fun processImage(bitmap: Bitmap) = launch(Dispatchers.IO) {
        val t1 = System.currentTimeMillis()
        val processedBitmap = imageProcessor.meanShift(bitmap)
        val t2 = System.currentTimeMillis()

        _stateLiveData.postValue(
            state?.copy(
                bitmap = processedBitmap,
                sdkTime = (t2 - t1).toDouble() / 1000
            )
        )
    }
}

// Set default value for any type of MutableLiveData
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }