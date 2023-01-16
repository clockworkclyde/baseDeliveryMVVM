package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentDetailsBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseDialogFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.OrderCartViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.loadDishDetailsImage
import com.github.clockworkclyde.basedeliverymvvm.util.onSingleClick
import com.github.clockworkclyde.basedeliverymvvm.util.setList
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.extra.DishExtra
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailsFragment : BaseDialogFragment(), OnExtraClickListener {

    override var isExpandedOnStart: Boolean = true

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var button: MaterialButton
    private val args: DetailsFragmentArgs by navArgs()
    private val item by lazy { args.dishItem }
    private val glide by lazy { Glide.with(this) }
    private val adapter by lazy { ExtraCategoriesAdapter(this) }

    private val extrasViewModel: DetailsViewModel by viewModels()
    private val orderViewModel: OrderCartViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            val density = requireContext().resources.displayMetrics.density
            val coordinator =
                findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
            val container = findViewById<FrameLayout>(com.google.android.material.R.id.container)
            val buttonLayout = layoutInflater.inflate(R.layout.layout_details_button, null)

            buttonLayout.findViewById<MaterialButton>(R.id.button)?.let { button = it }
            buttonLayout.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            }
            container?.addView(buttonLayout)
            buttonLayout.post {
                (coordinator?.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    buttonLayout.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    this.bottomMargin = (buttonLayout.measuredHeight - 8 * density).toInt()
                    container?.requestLayout()
                }
            }
        }
        setDishExtras()
    }

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
        applyDishLayout()
        applyQuantityButtons()
        setDishQuantity()
        setSelectedExtras()
    }

    private fun setDishExtras() {
        extrasViewModel.extras
            .onEach { extras ->
                // adapter.clearList()
                adapter.setList(extras)
                extras.onEach { cat ->
                    cat.extras
                        .filter { it.isSelected }
                        .also { setAddToOrderClick(it) }
                        .sumOf { it.price * it.quantity }
                        .let { updateTotalPrice(it) }
                }
            }.launchIn(lifecycleScope)
    }

    private fun setSelectedExtras() {
        val extras = args.extrasList.toList()
        extrasViewModel.initDishExtras(item.extras, extras)
    }

    private fun setDishQuantity() {
        extrasViewModel.dishQuantity.observe(viewLifecycleOwner) {
            binding.dishQuantityTextView.text = it.toString()
        }
    }

    private fun setAddToOrderClick(extras: List<DishExtra>) {
        button.apply {
            onSingleClick {
                extrasViewModel.dishQuantity.observe(viewLifecycleOwner) {
                    addDishToOrderCart(item, extras, it)
                }
            }
        }
    }

    private fun updateTotalPrice(value: Int = 0) {
        binding.dishTotalPriceTextView.apply {
            text = "${value + item.price} ₽."
        }
    }

    private fun applyDishLayout() {
        with(binding) {
            glide.loadDishDetailsImage(item.image, imageView)
            titleTextView.text = item.title
            servingSizeTextView.text = item.servingSize
            extrasRecyclerView.adapter = adapter
            updateTotalPrice()
        }
    }

    private fun applyQuantityButtons() {
        binding.apply {
            addDishQuantityButton.setOnClickListener {
                extrasViewModel.editDishQuantity(QuantityButtonAction.MORE)
            }
            reduceDishQuantityButton.setOnClickListener {
                extrasViewModel.editDishQuantity(QuantityButtonAction.LESS)
            }
        }
    }

    private fun addDishToOrderCart(item: DishItem, extras: List<DishExtra>, quantity: Int) {
        if (orderViewModel.addToOrderCart(item.id, extras, quantity)) {
            findNavController().popBackStack()
        }
    }

    override fun onSelectItemClick(item: DishExtra) {
        extrasViewModel.updateSelection(item)
    }

    override fun onButtonClick(item: DishExtra, action: ExtraButtonAction) {
        extrasViewModel.updateSelection(item, action)
    }
}