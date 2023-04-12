package com.kudashov.opencv_android.screens.test

import android.graphics.Color
import com.github.mikephil.charting.data.*
import com.kudashov.opencv_android.base.IMAGE_COUNT

data class TestsState(
    val totalImageCount: Int = IMAGE_COUNT,
    val processedImageCount: Int = 0,
    val sdkTimeResults: Array<Double> = Array(totalImageCount) { 0.0 },
    val ndkTimeResults: Array<Double> = Array(totalImageCount) { 0.0 },
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestsState

        if (totalImageCount != other.totalImageCount) return false
        if (processedImageCount != other.processedImageCount) return false
        if (!sdkTimeResults.contentEquals(other.sdkTimeResults)) return false
        if (!ndkTimeResults.contentEquals(other.ndkTimeResults)) return false
        if (isProcessingFinished != other.isProcessingFinished) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalImageCount
        result = 31 * result + processedImageCount
        result = 31 * result + sdkTimeResults.contentHashCode()
        result = 31 * result + ndkTimeResults.contentHashCode()
        result = 31 * result + isProcessingFinished.hashCode()
        return result
    }
}