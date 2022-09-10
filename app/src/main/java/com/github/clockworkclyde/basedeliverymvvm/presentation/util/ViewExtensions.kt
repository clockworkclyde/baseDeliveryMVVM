package com.github.clockworkclyde.basedeliverymvvm.presentation.util

import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged

const val DEFAULT_THROTTLE_DURATION_MS = 300L

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

fun EditText.getTextWithoutDashesAndSpaces(): String {
    return text.toString().replace(" ", "").replace("-", "")
}

inline fun <V : View> V.onSingleClick(
    throttleDuration: Long = DEFAULT_THROTTLE_DURATION_MS,
    crossinline listener: () -> Unit
) {
    setOnClickListener(SafeClickListener(throttleDuration) { listener.invoke() })
}

class SafeClickListener(
    throttleDuration: Long,
    private val clickListener: View.OnClickListener
) : View.OnClickListener {
    private val doubleClickPreventer = DoubleClickPrevent(throttleDuration)

    override fun onClick(v: View?) {
        if (doubleClickPreventer.isPrevent()) return
        else clickListener.onClick(v)
    }
}

private class DoubleClickPrevent(private val throttleDuration: Long) {
    private var lastClickTiming = 0L

    fun isPrevent(): Boolean {
        val now = System.currentTimeMillis()
        val spentTime = now - lastClickTiming
        return if (spentTime in 1 until throttleDuration) {
            true
        } else {
            lastClickTiming = now
            false
        }
    }
}