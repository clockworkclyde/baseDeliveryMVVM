package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.cart

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.ItemCartBinding
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.cart.OrderProductItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class OrderCartAdapter(onButtonClickListener: (Long, Int) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(orderAdapterDelegate(onButtonClickListener = onButtonClickListener))
    }

    companion object {
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
                            .transition(DrawableTransitionOptions.withCrossFade())
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
}