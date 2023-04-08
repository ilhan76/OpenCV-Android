package com.kudashov.opencv_android.screens.test

import android.graphics.Color
import com.github.mikephil.charting.data.*
import com.kudashov.opencv_android.base.IMAGE_COUNT

data class TestsState(
    val totalImageCount: Int = IMAGE_COUNT,
    val processedImageCount: Int = 0,
    val sdkTimeResults: List<Double> = emptyList(),
    val ndkTimeResults: List<Double> = emptyList(),
) {
    val isProcessingFinished: Boolean = totalImageCount == processedImageCount

    fun getChartData(): LineData {
        val ndkEntryList = mutableListOf<Entry>()
        ndkTimeResults.forEachIndexed { i, data ->
            ndkEntryList.add(Entry(i.toFloat(), data.toFloat()))
        }
        val ndkDataSet = LineDataSet(ndkEntryList, "NDK")
        ndkDataSet.color = Color.RED

        val sdkEntryList = mutableListOf<Entry>()
        sdkTimeResults.forEachIndexed { i, data ->
            sdkEntryList.add(Entry(i.toFloat(), data.toFloat()))
        }
        val sdkDataSet = LineDataSet(sdkEntryList, "SDK")
        sdkDataSet.color = Color.GREEN

        val data = LineData()

        data.addDataSet(sdkDataSet)
        data.addDataSet(ndkDataSet)
        return data
    }
}