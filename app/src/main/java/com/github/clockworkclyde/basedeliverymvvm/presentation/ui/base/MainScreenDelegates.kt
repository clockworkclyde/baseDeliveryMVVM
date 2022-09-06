package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import android.app.Activity
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemCategoryBinding
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuBinding
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemMenuProgressBinding
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.menu.DishesCategoryItem
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.models.ui.menu.DishProgress
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main.CategoryAdapter
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.onSingleClick
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

object MainScreenDelegates {

    enum class ClickAction {
        OpenDetails, AddToCart
    }

    fun categoryAdapterDelegate(onItemClickListener: (DishItem, ClickAction) -> Unit) =
        adapterDelegateViewBinding<DishesCategoryItem, ListItem, ItemCategoryBinding>({ inflater, container ->
            ItemCategoryBinding.inflate(inflater, container, false)
        }) {
            val adapter = CategoryAdapter(onItemClickListener)
            binding.recyclerView.adapter = adapter

            bind {
                adapter.items = item.items
            }
        }

    fun menuItemsProgressAdapterDelegate() =
        adapterDelegateViewBinding<DishProgress, ListItem, ItemMenuProgressBinding>(
            { inflater, container ->
                ItemMenuProgressBinding.inflate(inflater, container, false)
            }
        ) {
            val anim = AnimationUtils.loadAnimation(context, R.anim.anim_loading_shim)
            bind {
                binding.root.startAnimation(anim)
            }
        }

    fun menuItemsAdapterDelegate(
        onItemClickListener: (DishItem, ClickAction) -> Unit
    ) =
        adapterDelegateViewBinding<DishItem, ListItem, ItemMenuBinding>(
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
}
