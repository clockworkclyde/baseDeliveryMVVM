package com.github.clockworkclyde.basedeliverymvvm.util

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.clockworkclyde.basedeliverymvvm.R

fun RequestManager.loadDishImage(imageUrl: String, view: ImageView) {
    this.load(imageUrl)
        .transform(
            CenterCrop(),
            RoundedCorners(view.context.resources.getDimensionPixelOffset(R.dimen.radius_default_app))
        )
        .transition(withCrossFade())
        .into(view)
}

fun RequestManager.loadDishDetailsImage(imageUrl: String, view: ImageView) {
    val radius = view.context.resources.getDimensionPixelOffset(R.dimen.card_radius).toFloat()
    load(imageUrl)
        .transform(
            CenterCrop(),
            GranularRoundedCorners(radius, radius, 0f, 0f)
        )
        .transition(withCrossFade())
        .placeholder(R.drawable.bg_progress_menu_item)
        .into(view)
}



