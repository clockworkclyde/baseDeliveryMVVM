package com.github.clockworkclyde.basedeliverymvvm.presentation.util

import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged


inline fun SearchView.doOnQueryTextChanged(crossinline listener: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            return if (newText.isNullOrEmpty()) false
            else {
                listener(newText)
                true
            }
        }
    })
}

inline fun EditText.doOnQueryTextChanged(crossinline listener: (String) -> Unit) {
    doOnTextChanged { text, _, _, _ ->
        listener(text?.toString().orEmpty())
    }
}
