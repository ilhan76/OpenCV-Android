package com.kudashov.opencv_android.screens.test

import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kudashov.opencv_android.base.ITERATIONS_PER_IMAGE
import com.kudashov.opencv_android.base.Locations.DIC2K_DATASET
import com.kudashov.opencv_android.base.Locations.COCO_DATASET
import com.kudashov.opencv_android.extensions.default
import com.kudashov.opencv_android.image_processor.NdkImageProcessor
import com.kudashov.opencv_android.image_processor.SdkImageProcessor
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

sealed class DatasetType {
    object Coco : DatasetType()
    object Div2k : DatasetType()
}

class TestsViewModel : ViewModel() {

    private val sdkImageProcessor = SdkImageProcessor(viewModelScope)
    private val ndkImageProcessor = NdkImageProcessor(viewModelScope)

    private val _stateLiveData = MutableLiveData<TestsState>().default(TestsState())
    val stateLiveData: LiveData<TestsState> = _stateLiveData

    private val state get() = stateLiveData.value

    private val div2kFirebaseInteractor =
        FirebaseInteractor(Firebase.storage.getReference(DIC2K_DATASET))
    private val cocoFirebaseInteractor =
        FirebaseInteractor(Firebase.storage.getReference(COCO_DATASET))

    fun startImageProcessing(datasetType: DatasetType) =
        viewModelScope.launch(Dispatchers.Default) {
            var pageToken: String? = null
            do {
                val imagesRequest = when (datasetType) {
                    DatasetType.Coco -> cocoFirebaseInteractor
                    DatasetType.Div2k -> div2kFirebaseInteractor
                }.getListPaginated(pageToken)

                imagesRequest.byteImages.forEach { processImage(it) }
                pageToken = imagesRequest.token
            } while (pageToken != null)
        }

    private suspend fun processImage(byteArray: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val sdkResultTime = measureTimeMillis {
            repeat(ITERATIONS_PER_IMAGE) { sdkImageProcessor.meanShiftAsync(bitmap).await() }
        }.toDouble() / 1000 / ITERATIONS_PER_IMAGE

        val ndkResultTime = measureTimeMillis {
            repeat(ITERATIONS_PER_IMAGE) { ndkImageProcessor.meanShiftAsync(bitmap).await() }
        }.toDouble() / 1000 / ITERATIONS_PER_IMAGE

        withContext(Dispatchers.Main) {
            state?.let { it ->
                _stateLiveData.value = it.copy(
                    sdkTimeResults = it.sdkTimeResults
                        .toMutableList()
                        .apply { add(sdkResultTime) },
                    ndkTimeResults = it.ndkTimeResults
                        .toMutableList()
                        .apply { add(ndkResultTime) },
                    processedImageCount = it.processedImageCount + 1
                )
            }
        }
    }
}