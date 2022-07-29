package com.github.clockworkclyde.basedeliverymvvm.presentation.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun getScreenSize(context: Context): Point {
    val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}