package com.kudashov.opencv_android.extensions

import androidx.lifecycle.MutableLiveData


// Set default value for any type of MutableLiveData
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }