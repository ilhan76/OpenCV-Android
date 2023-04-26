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
        viewModel.startImageProcessing(DatasetType.Div2k)
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

        binding.averageTimeNdkTv.text = getString(
            R.string.average_ndk_time_text,
            state.averageNdkTime.toString()
        )
        binding.averageTimeSdkTv.text = getString(
            R.string.average_sdk_time_text,
            state.averageSdkTime.toString()
        )
        binding.performanceGainTv.text = getString(R.string.performance_gain_text, state.performanceGain.toString())
    }
}