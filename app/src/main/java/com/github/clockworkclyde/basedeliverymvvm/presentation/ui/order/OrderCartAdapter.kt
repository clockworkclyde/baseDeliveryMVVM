package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemOrderCartBinding
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.order.DishExtraEntity
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

enum class QuantityButtonAction {
    LESS, MORE
}

interface OnOrderItemClickListener {
    fun onButtonClick(id: Long, action: QuantityButtonAction, extras: List<DishExtraEntity>, additionTime: Long)
    fun onItemClick(item: DishItem, extras: List<DishExtraEntity>, quantity: Int)
}

class OrderCartAdapter(onOrderItemClickListener: OnOrderItemClickListener) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(orderAdapterDelegate(onOrderItemClickListener = onOrderItemClickListener))
    }

    companion object {
        fun orderAdapterDelegate(onOrderItemClickListener: OnOrderItemClickListener) =
            adapterDelegateViewBinding<OrderDish, ListItem, ItemOrderCartBinding>(
                { inflater, container ->
                    ItemOrderCartBinding.inflate(inflater, container, false)
                }
            ) {
                bind {
                    val resources = binding.root.resources

                    binding.apply {
                        // bind dish's extras added to order
                        item.extras.map {
                            it.title + " " + it.quantity + " pcs. - " + it.price + " ₽. "
                        }.let {
                            val extrasAdapter =
                                ArrayAdapter<String>(context, R.layout.order_dish_extra, it)
                            extrasListTextView.adapter = extrasAdapter
                        }
                        // bind dish's data
                        val dish = item.dish
                        titleTextView.text = dish.title
                        servingSizeTextView.text = dish.servingSize
                        priceTextView.text =
                            "${dish.price + item.extras.sumOf { it.price * it.quantity }} ₽."
                        Glide.with(root)
                            .load(dish.image)
                            .override(
                                resources.getDimensionPixelOffset(R.dimen.menu_item_width),
                                resources.getDimensionPixelOffset(R.dimen.menu_item_height)
                            )
                            .transform(
                                CenterCrop(),
                                RoundedCorners(resources.getDimensionPixelOffset(R.dimen.card_radius))
                            )
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageView)

                        root.onSingleClick {
                            onOrderItemClickListener.onItemClick(
                                item.dish,
                                item.extras,
                                item.quantity
                            )
                        }

                        // init quantity buttons
                        counterTextView.text = item.quantity.toString()
                        lessButton.setOnClickListener {
                            onOrderItemClickListener.onButtonClick(
                                dish.id,
                                QuantityButtonAction.LESS,
                                item.extras,
                                item.additionTime
                            )
                        }
                        moreButton.setOnClickListener {
                            onOrderItemClickListener.onButtonClick(
                                dish.id,
                                QuantityButtonAction.MORE,
                                item.extras,
                                item.additionTime
                            )
                        }
                    }
                }
            }
    }
}