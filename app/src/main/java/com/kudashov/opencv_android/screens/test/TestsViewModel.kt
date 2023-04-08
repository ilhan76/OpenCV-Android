package com.kudashov.opencv_android.screens.test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kudashov.opencv_android.base.IMAGE_COUNT
import com.kudashov.opencv_android.extensions.default
import com.kudashov.opencv_android.image_processor.NdkImageProcessor
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class TestsViewModel : ViewModel() {

    private val tag: String = "TestsViewModel"

    private val sdkImageProcessor = SdkImageProcessor()
    private val ndkImageProcessor = NdkImageProcessor()

    private val _stateLiveData = MutableLiveData<TestsState>().default(TestsState())
    val stateLiveData: LiveData<TestsState> = _stateLiveData

    private val state get() = stateLiveData.value

    private val ref = Firebase.storage.getReference("images/")
//    private val ref = Firebase.storage.getReference("high_quality_images/")

    fun startImageProcessing() = GlobalScope.launch(Dispatchers.IO) {
        listAllPaginated()
    }

    private suspend fun listAllPaginated() =
        ref.list(IMAGE_COUNT)
            .addOnSuccessListener { listResult ->
            listResult.items.forEach { storageReference ->
                storageReference
                    .getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener { biteArray ->
                        GlobalScope.launch(Dispatchers.IO) {
                            processImage(BitmapFactory.decodeByteArray(biteArray, 0, biteArray.size))
                        }
                    }.addOnFailureListener(::onGettingBytesFailed)
            }
        }.addOnFailureListener(::onGettingImageFailed)

/*    private fun listAllPaginated(pageToken: String? = null) =
        if (pageToken != null) {
            ref.list(10, pageToken)
        } else {
            ref.list(10)
        }.addOnSuccessListener { listResult ->
            listResult.items.forEach { storageReference ->
                storageReference
                    .getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener { biteArray ->
                        processImage(BitmapFactory.decodeByteArray(biteArray, 0, biteArray.size))
                        listResult.pageToken?.let {
                            listAllPaginated(it)
                        }
                    }.addOnFailureListener(::onGettingBytesFailed)
            }
        }.addOnFailureListener(::onGettingImageFailed)*/

    private suspend fun processImage(bitmap: Bitmap) {
        val sdkResultTime = measureTimeMillis {
            sdkImageProcessor.meanShift(bitmap)
        }.toDouble() / 1000

        val ndkResultTime = measureTimeMillis {
            ndkImageProcessor.meanShift(bitmap)
        }.toDouble() / 1000

        withContext(Dispatchers.Main) {
            state?.let { it ->
                _stateLiveData.postValue(
                    it.copy(
                        sdkTimeResults = listOf(
                            it.sdkTimeResults,
                            listOf(sdkResultTime)
                        ).flatten(),
                        ndkTimeResults = listOf(
                            it.ndkTimeResults,
                            listOf(ndkResultTime)
                        ).flatten(),
                        processedImageCount = it.processedImageCount + 1
                    )
                )
            }
        }
    }

    private fun onGettingImageFailed(exception: Exception) =
        Log.d(tag, "Ошибка получения изображений - ${exception.localizedMessage}")

    private fun onGettingBytesFailed(exception: Exception) =
        Log.d(tag, "Ошибка получения байт изображения - ${exception.localizedMessage}")
}