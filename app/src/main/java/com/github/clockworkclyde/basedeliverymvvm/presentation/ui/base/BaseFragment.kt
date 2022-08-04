package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.MainActivity

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.setBottomNavigationViewVisibility(bottomNavigationViewVisibility)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.setBottomNavigationViewVisibility(bottomNavigationViewVisibility)
    }
}