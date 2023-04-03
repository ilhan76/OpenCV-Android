package com.kudashov.opencv_android.base

import android.app.Application
import org.opencv.android.OpenCVLoader

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("opencvnativedemo")
        OpenCVLoader.initDebug()
    }
}