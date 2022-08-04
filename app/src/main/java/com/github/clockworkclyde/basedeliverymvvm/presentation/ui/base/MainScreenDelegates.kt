package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.app.Activity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemCartBinding
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuBinding
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuProgressBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart.OrderProductItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemProgress
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.onSingleClick
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainScreenDelegates {

    enum class ClickAction {
        OpenDetails, AddToCart
    }

    fun mainScreenAdapterDelegate(
        onItemClickListener: (MenuItem, ClickAction) -> Unit
    ) =
        adapterDelegateViewBinding<MenuItem, ListItem, ItemMenuBinding>(
            { inflater, container ->
                ItemMenuBinding.inflate(inflater, container, false)
            }
        ) {
            bind {
                val resources = binding.root.resources
                binding.apply {
                    Glide.with(root) // todo base-util
                        .load(item.image)
                        .override(
                            resources.getDimensionPixelOffset(R.dimen.menu_item_width),
                            resources.getDimensionPixelOffset(R.dimen.menu_item_height)
                        )
                        .transform(
                            CenterCrop(),
                            RoundedCorners(resources.getDimensionPixelOffset(R.dimen.card_radius))
                        )
                        .transition(withCrossFade())
                        .into(imageView)

                    titleTextView.text = item.title
                    btnAddItemToOrderCart.text = "${item.price} p."
                    servingSizeTextView.text = item.servingSize

                    root.onSingleClick { onItemClickListener.invoke(item, ClickAction.OpenDetails) }
                    btnAddItemToOrderCart.onSingleClick {
                        onItemClickListener.invoke(
                            item,
                            ClickAction.AddToCart
                        )
                    }
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }

    fun mainScreenProgressAdapterDelegate() =
        adapterDelegateViewBinding<MenuItemProgress, ListItem, ItemMenuProgressBinding>(
            { inflater, container ->
                ItemMenuProgressBinding.inflate(inflater, container, false)
            }
        ) {
            bind {}
        }

    fun orderAdapterDelegate(onButtonClickListener: (Long, Int) -> Unit) =
        adapterDelegateViewBinding<OrderProductItem, ListItem, ItemCartBinding>(
            { inflater, container ->
                ItemCartBinding.inflate(inflater, container, false)
            }
        ) {
            bind {
                val resources = binding.root.resources

                binding.apply {
                    titleTextView.text = item.title
                    servingSizeTextView.text = item.servingSize
                    priceTextView.text = "${item.price} p."
                    Glide.with(root)
                        .load(item.image)
                        .override(
                            resources.getDimensionPixelOffset(R.dimen.menu_item_width),
                            resources.getDimensionPixelOffset(R.dimen.menu_item_height)
                        )
                        .transform(
                            CenterCrop(),
                            RoundedCorners(resources.getDimensionPixelOffset(R.dimen.card_radius))
                        )
                        .transition(withCrossFade())
                        .into(imageView)

                    val quantity = item.quantity
                    counterTextView.text = quantity.toString()
                    lessButton.setOnClickListener {
                        onButtonClickListener.invoke(
                            item.id,
                            quantity - 1
                        )
                    }
                    moreButton.setOnClickListener {
                        onButtonClickListener.invoke(
                            item.id,
                            quantity + 1
                        )
                    }
                }
            }
        }

}