package com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.util

import androidx.recyclerview.widget.DiffUtil
import com.github.clockworkclyde.basedeliverymvvm.layers.data.base.ListItem

open class BaseDiffUtilCallback: DiffUtil.ItemCallback<ListItem>() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.itemId == newItem.itemId

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.equals(newItem)
}