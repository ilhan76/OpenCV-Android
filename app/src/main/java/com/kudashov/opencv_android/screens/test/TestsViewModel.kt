package com.kudashov.opencv_android.screens.test

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.component3
import com.kudashov.opencv_android.extensions.default
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TestsViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val tag: String = "TestsViewModel"

    private val _stateLiveData = MutableLiveData<TestsState>().default(TestsState())
    val stateLiveData: LiveData<TestsState> = _stateLiveData

    private val ref = Firebase.storage.getReference("images/")

    suspend fun startImageProcessing() = coroutineScope {
        launch {
            withContext(Dispatchers.IO) {
                listAllPaginated()
            }
        }
    }

    private fun listAllPaginated(pageToken: String? = null) = if (pageToken != null) {
        ref.list(1, pageToken)
    } else {
        ref.list(1)
    }.addOnSuccessListener { (items, _, pageToken) ->
        onImagesLoaded(items, pageToken)
    }.addOnFailureListener(::onGettingImageFailed)

    private fun onImagesLoaded(
        items: List<StorageReference>,
        pageToken: String?
    ) {
        //todo - Обработка изображений
        pageToken?.let { listAllPaginated(it) }
    }

    private fun onGettingImageFailed(exception: Exception) {
        Log.d(tag, "Ошибка получения изображений - ${exception.localizedMessage}")
    }
}