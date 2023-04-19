package com.kudashov.opencv_android.screens.test

import com.google.firebase.storage.StorageReference
import com.kudashov.opencv_android.base.STEP
import kotlinx.coroutines.tasks.await

data class ImagesRequest(
    val token: String?,
    val byteImages: List<ByteArray>
)

class FirebaseInteractor(
    private val reference: StorageReference
) {

    suspend fun getListPaginated(token: String? = null): ImagesRequest {
        val listResult = reference.list(STEP, token.orEmpty())
            .await()

        val byteImages = listResult
            .items
            .map { it.getBytes(Long.MAX_VALUE).await() }

        return ImagesRequest(
            token = listResult.pageToken,
            byteImages = byteImages
        )
    }

}
