package com.kudashov.opencv_android

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.kudashov.opencv_android.base.IMAGE_TYPE
import com.kudashov.opencv_android.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.opencv.android.OpenCVLoader
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private lateinit var binding: ActivityMainBinding

    private val viewModel = MainViewModel()

    private var imageBitmap: Bitmap? = null

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            viewModel.imageLoaded(imageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        bind()
        OpenCVLoader.initDebug()
    }

    private fun initListeners() = with(binding) {
        openGalleryBtn.setOnClickListener { resultLauncher.launch(IMAGE_TYPE) }
        processBtn.setOnClickListener {
            imageBitmap?.let { viewModel.processImage(it) }
        }
    }

    private fun bind() = viewModel.stateLiveData.observe(this) { state ->
        state.bitmap?.let {
            binding.pictureIv.setImageBitmap(it)
        }
        state.sdkTime?.let {
            binding.sdkTimeTv.text = it.toString()
        }
    }
}