package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.github.clockworkclyde.basedeliverymvvm.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialogFragment : BottomSheetDialogFragment() {

    protected open var isExpandedOnStart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialog)
    }

    override fun onStart() {
        super.onStart()
        val bottomSheetBehavior = BottomSheetBehavior.from(requireView().parent as View)
        bottomSheetBehavior.skipCollapsed = true
        if (isExpandedOnStart) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}