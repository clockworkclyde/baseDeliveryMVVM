package com.github.clockworkclyde.basedeliverymvvm.util

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
}

class AndroidResourceProvider @Inject constructor(private val context: Context) : ResourceProvider {
    override fun getString(id: Int): String = context.getString(id)
}