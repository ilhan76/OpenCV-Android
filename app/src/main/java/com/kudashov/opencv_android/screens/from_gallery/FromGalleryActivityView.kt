package com.kudashov.opencv_android.screens.from_gallery

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kudashov.opencv_android.base.IMAGE_TYPE
import com.kudashov.opencv_android.databinding.ActivityFromGalleryBinding
import com.kudashov.opencv_android.extensions.goneIfNull
import com.kudashov.opencv_android.extensions.setValueOrGone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class FromGalleryActivityView  : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private lateinit var binding: ActivityFromGalleryBinding

    private val viewModel = FromGalleryViewModel()

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.imageLoaded(MediaStore.Images.Media.getBitmap(contentResolver, uri))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        bind()
    }

    private fun initListeners() = with(binding) {
        openGalleryBtn.setOnClickListener { resultLauncher.launch(IMAGE_TYPE) }
        processBtn.setOnClickListener { viewModel.processImage() }
    }

    private fun bind() = viewModel.stateLiveData.observe(this) { state ->
        with(state) {
            val shouldShowOriginalImage =
                sourceBitmap != null && resultNdkBitmap == null && resultSdkBitmap == null
            binding.sourceImageTitleTv.isVisible = shouldShowOriginalImage
            binding.pictureSourceIv.setValueOrGone(sourceBitmap)
            binding.pictureSourceIv.isVisible = shouldShowOriginalImage

            binding.sdkProcessedImageTitleTv.goneIfNull(resultSdkBitmap)
            binding.pictureSdkResultIv.setValueOrGone(resultSdkBitmap)

            binding.ndkProcessedImageTitleTv.goneIfNull(resultNdkBitmap)
            binding.pictureNdkResultIv.setValueOrGone(resultNdkBitmap)

            binding.sdkTimeTitleTv.goneIfNull(sdkTime)
            binding.sdkTimeTv.setValueOrGone(sdkTime)
            binding.ndkTimeTitleTv.goneIfNull(sdkTime)
            binding.ndkTimeTv.setValueOrGone(ndkTime)
        }
    }
}