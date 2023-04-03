package com.kudashov.opencv_android.base

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("opencvnativedemo")
    }
}