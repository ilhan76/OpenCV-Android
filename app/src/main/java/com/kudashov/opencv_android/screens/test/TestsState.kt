package com.kudashov.opencv_android.screens.test

data class TestsState(
    val totalImageCount: Int = 2,
    val processedImageCount: Int = 0,
    val sdkTimeResults: List<Double> = emptyList(),
    val ndkTimeResults: List<Double> = emptyList(),
) {
    val isProcessingFinished: Boolean = totalImageCount == processedImageCount
}