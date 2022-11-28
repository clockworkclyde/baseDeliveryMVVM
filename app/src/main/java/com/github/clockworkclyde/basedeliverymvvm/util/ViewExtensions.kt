package com.github.clockworkclyde.basedeliverymvvm.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import com.github.clockworkclyde.models.ui.base.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

const val DEFAULT_THROTTLE_DURATION_MS = 300L


fun AsyncListDifferDelegationAdapter<ListItem>.clearList() {
    items = listOf()
}

fun AsyncListDifferDelegationAdapter<ListItem>.setList(list: List<ListItem>) {
    items = list
}

fun <T> Flow<T>.launchWhenResumed(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenResumed { collect()
    }
}

fun View.getFocusAndShowSoftInput() {
    if (requestFocus()) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

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

fun Button.setTextById(id: Int) {
    text = this.context.resources.getString(id)
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