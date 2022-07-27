package com.github.clockworkclyde.basedeliverymvvm.ui.navigation

import android.view.View

interface OnBottomSheetCallback {
    fun onStateChanged(bottomSheet: View, newState: Int)
}