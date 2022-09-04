package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected open var bottomNavigationViewVisibility = View.VISIBLE

    fun <T : Throwable> Flow<T>.addOnExceptionListener(block: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            collect { block.invoke() }
        }
    }

//    fun <T> Flow<T>.handleErrorData(block: () -> Unit): Flow<T> {
//        return flow {
//            try {
//                collect { value ->
//                    emit(value)
//                }
//            } catch (e: Exception) {
//                block.invoke()
////                throw e
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.setBottomNavigationViewVisibility(
            bottomNavigationViewVisibility
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.setBottomNavigationViewVisibility(
            bottomNavigationViewVisibility
        )
    }
}