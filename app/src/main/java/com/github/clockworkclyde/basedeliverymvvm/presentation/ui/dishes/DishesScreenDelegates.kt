package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes

import android.app.Activity
import android.os.Parcelable
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.*
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.loadDishImage
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.DishProgress
import com.github.clockworkclyde.models.ui.dishes.DishesCategoryItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import timber.log.Timber

interface OnDishItemClickListener {
    fun onItemClick(item: DishItem)
    fun onButtonClick(item: DishItem)
    fun onEditOrderDish(item: OrderDish)
}

object MainScreenDelegates {

    fun categoryAdapterDelegate(
        onItemClickListener: OnDishItemClickListener,
        onQuantityActionClick: (OrderDish, QuantityButtonAction) -> Unit
    ) =
        adapterDelegateViewBinding<DishesCategoryItem, ListItem, ItemCategoryBinding>({ inflater, container ->
            ItemCategoryBinding.inflate(inflater, container, false)
        }) {
            val adapter = DishesAdapter(onItemClickListener, onQuantityActionClick)
            binding.recyclerView.apply {
                if (getAdapter() == null) {
                    this.adapter = adapter
                }
            }
            bind {
                adapter.items = item.items
            }
        }

    fun dishesProgressAdapterDelegate() =
        adapterDelegateViewBinding<DishProgress, ListItem, ItemDishProgressBinding>(
            { inflater, container ->
                ItemDishProgressBinding.inflate(inflater, container, false)
            }
        ) {
            val anim = AnimationUtils.loadAnimation(context, R.anim.anim_loading_shim)
            bind {
                binding.root.startAnimation(anim)
            }
        }

    fun dishesAdapterDelegate(
        onItemClickListener: OnDishItemClickListener
    ) =
        adapterDelegateViewBinding<DishItem, ListItem, ItemDishBinding>(
            { inflater, container ->
                ItemDishBinding.inflate(inflater, container, false)
            }
        ) {
            bind {
                binding.apply {
                    Glide.with(root)
                        .loadDishImage(
                            item.image,
                            imageView
                        )
                    titleTextView.text = item.title
                    priceTextView.text = "${item.price} ₽."
                    servingSizeTextView.text = item.servingSize

                    root.onSingleClick { onItemClickListener.onItemClick(item) }
                    btnAddItemToOrderCart.onSingleClick { onItemClickListener.onButtonClick(item) }
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }

    fun selectedDishesAdapterDelegate(
        onItemClickListener: OnDishItemClickListener,
        onQuantityActionClick: (OrderDish, QuantityButtonAction) -> Unit
    ) =
        adapterDelegateViewBinding<OrderDish, ListItem, ItemDishSelectedBinding>({ inflater, container ->
            ItemDishSelectedBinding.inflate(inflater, container, false)
        }) {
            bind {
                val dish = item.dish
                binding.apply {
                    Glide.with(root)
                        .loadDishImage(
                            dish.image,
                            imageView
                        )
                    titleTextView.text = dish.title
                    servingSizeTextView.text = dish.servingSize
                    root.onSingleClick { onItemClickListener.onEditOrderDish(item) }
                    // init quantity buttons
                    counterTextView.text = item.quantity.toString()
                    lessButton.setOnClickListener {
                        onQuantityActionClick.invoke(item, QuantityButtonAction.LESS)
                    }
                    moreButton.setOnClickListener {
                        onQuantityActionClick.invoke(item, QuantityButtonAction.MORE)
                    }
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }
}
