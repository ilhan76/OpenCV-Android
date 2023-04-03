package com.kudashov.opencv_android.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kudashov.opencv_android.databinding.ActivityMainBinding
import com.kudashov.opencv_android.screens.from_gallery.FromGalleryActivityView
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        OpenCVLoader.initDebug()
    }

    private fun initListeners() = with(binding) {
        openFromGalleryScreenBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, FromGalleryActivityView::class.java))
        }
        openTestsScreenBtn.setOnClickListener {
            // todo
        }
    }
}