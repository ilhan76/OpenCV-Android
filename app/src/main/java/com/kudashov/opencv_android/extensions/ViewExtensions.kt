package com.kudashov.opencv_android.extensions

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

fun <T> View.goneIfNull(value: T) {
    isVisible = value != null
}

fun TextView.setValueOrGone(value: Double?) {
    if (value == null) {
        isVisible = false
    } else {
        isVisible = true
        text = value.toString()
    }
}

fun ImageView.setValueOrGone(value: Bitmap?) {
    if (value == null) {
        isVisible = false
    } else {
        isVisible = true
        setImageBitmap(value)
    }
}