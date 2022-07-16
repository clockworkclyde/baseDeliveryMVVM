package com.github.clockworkclyde.basedeliverymvvm.layers.ui.screens.main

import android.app.Activity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuBinding
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuProgressBinding
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuItemProgressModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.base.ListItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainScreenDelegates {

    fun mainScreenAdapterDelegate() = //buttonClickListener: (MenuItemUiModel) -> Unit
        adapterDelegateViewBinding<MenuItemUiModel, ListItem, ItemMenuBinding>(
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
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }

    fun mainScreenProgressAdapterDelegate() =
        adapterDelegateViewBinding<MenuItemProgressModel, ListItem, ItemMenuProgressBinding>(
            { inflater, container ->
                ItemMenuProgressBinding.inflate(inflater, container, false)
            }
        ) {
            bind {}
        }


}