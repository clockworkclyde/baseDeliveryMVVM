package com.github.clockworkclyde.basedeliverymvvm.layers.ui.navigation

import android.view.View

interface OnBottomSheetCallback {
    fun onStateChanged(bottomSheet: View, newState: Int)
}