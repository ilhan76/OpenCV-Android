package com.kudashov.opencv_android

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.kudashov.opencv_android.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob()

    private lateinit var binding: ActivityMainBinding

    private var imageBitmap: Bitmap? = null

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            try {
                val mat = Mat()
                Utils.bitmapToMat(imageBitmap, mat)
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
                Utils.matToBitmap(mat, imageBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            Glide.with(this)
                .load(imageBitmap)
                .into(binding.pictureIv)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        OpenCVLoader.initDebug()
    }

    private fun initListeners() = with(binding) {
        openGalleryBtn.setOnClickListener { resultLauncher.launch(IMAGE_TYPE) }
    }
}