package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentDetailsBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseDialogFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.getScreenSize

class DetailsFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val glide by lazy { Glide.with(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val screenSize = getScreenSize(requireContext())
        val item = args.menuItem
        with(binding) {
            glide.load(item.image)
                .override(screenSize.x, (screenSize.y * 9f / 16).toInt())
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelOffset(R.dimen.card_radius))
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
            imageView.apply { scaleType = ImageView.ScaleType.CENTER_CROP }

            titleTextView.text = item.title
            button.text = item.price.toString()
        }
    }
}