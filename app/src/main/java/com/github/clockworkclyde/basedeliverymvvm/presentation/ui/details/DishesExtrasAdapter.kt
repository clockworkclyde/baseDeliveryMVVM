package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import androidx.core.view.isVisible
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemDishExtraBinding
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.order.DishExtra
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

enum class ExtraButtonAction {
    LESS, MORE, CLEAR
}

interface OnExtraClickListener {
    fun onSelectItemClick(item: DishExtra)
    fun onButtonClick(item: DishExtra, action: ExtraButtonAction)
}

class DishesExtrasAdapter(onExtraClickListener: OnExtraClickListener) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(dishExtraDelegate(onExtraClickListener))
    }

    companion object {
        private fun dishExtraDelegate(clickListener: OnExtraClickListener) =
            adapterDelegateViewBinding<DishExtra, ListItem, ItemDishExtraBinding>({ inflater, container ->
                ItemDishExtraBinding.inflate(inflater, container, false)
            }) {
                bind {
                    with(binding) {
                        val isSelected = item.isSelected
                        // counter layout
                        counterTextView.text = item.quantity.toString()
                        counterCardView.isVisible = isSelected
                        lessButton.setOnClickListener {
                            clickListener.onButtonClick(
                                item,
                                ExtraButtonAction.LESS
                            )
                        }
                        moreButton.setOnClickListener {
                            clickListener.onButtonClick(
                                item,
                                ExtraButtonAction.MORE
                            )
                        }
                        // button
                        extraButton.apply {
                            text = item.title
                            if (isSelected) {
                                background.setTint(getColor(R.color.mint))
                            } else {
                                background.setTint(getColor(com.mapbox.mapboxsdk.R.color.mapbox_gray))
                            }
                            setOnClickListener { clickListener.onSelectItemClick(item) }
                        }
                    }
                }
            }
    }
}