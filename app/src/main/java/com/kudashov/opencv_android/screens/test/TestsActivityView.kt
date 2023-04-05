package com.kudashov.opencv_android.screens.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kudashov.opencv_android.databinding.ActivityTestsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TestsActivityView : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private lateinit var binding: ActivityTestsBinding
    private val viewModel = TestsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launch { viewModel.startImageProcessing() }
    }
}