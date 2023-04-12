package com.kudashov.opencv_android.screens.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kudashov.opencv_android.R
import com.kudashov.opencv_android.databinding.ActivityTestsBinding

class TestsActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityTestsBinding
    private val viewModel = TestsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()
        viewModel.startImageProcessing()
    }

    private fun bind() = viewModel.stateLiveData.observe(this) { state ->
        if (binding.loaderPb.max != state.totalImageCount) {
            binding.loaderPb.max = state.totalImageCount
        }
        binding.loaderTv.text = getString(
            R.string.image_processing_progress_text,
            state.processedImageCount,
            state.totalImageCount
        )
        binding.loaderPb.progress = state.processedImageCount
        binding.chart.data = state.getChartData()
        binding.chart.invalidate()
    }
}