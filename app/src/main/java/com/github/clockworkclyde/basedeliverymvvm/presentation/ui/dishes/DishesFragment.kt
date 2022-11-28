package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.databinding.FragmentDishesBinding
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseFragment
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.OrderCartViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.ListMediator
import com.github.clockworkclyde.basedeliverymvvm.util.launchWhenResumed
import com.github.clockworkclyde.basedeliverymvvm.util.setList
import com.github.clockworkclyde.models.ZERO
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.DishProgress
import com.github.clockworkclyde.models.ui.dishes.DishesCategoryItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DishesFragment : BaseFragment(R.layout.fragment_dishes), OnDishItemClickListener {

    override var bottomNavigationViewVisibility: Int = View.VISIBLE
    private var errorViewIsVisible = false

    private lateinit var binding: FragmentDishesBinding
    private val adapter by lazy { CategoryAdapter(this, ::onQuantityActionClick) }
    private val dishesViewModel: DishesViewModel by viewModels()
    private val orderViewModel: OrderCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dishesViewModel.errorData.addOnExceptionListener {
            errorViewIsVisible = true
            showError()
        }
        binding = FragmentDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.itemAnimator = null
            recyclerView.setHasFixedSize(true)

            val mediator = ListMediator(recyclerView, tabLayout)

            dishesViewModel.viewState
                .onEach { viewState ->
                    mediator.detach()
                    val categories: List<DishesCategoryItem> = when (viewState) {
                        is ViewState.Loading -> {
                            getProgressItems()
                        }
                        is ViewState.Success -> {
                            viewState.data
                        }
                        is ViewState.Empty -> {
                            getEmptyItems()
                        }
                        is ViewState.Error -> {
                            getErrorItems()
                        }
                    }
                    adapter.setList(categories)

                    if (viewState is ViewState.Success) {
                        tabLayout.setTabs(categories)
                        if (tabLayout.tabCount == categories.count()) {
                            mediator.updateWithAnchors(categories.indices.toList())
                            mediator.attach()
                            tabLayout.getTabAt(getTabPosition())?.select()
                        }
                    }
                }
                .launchWhenResumed(lifecycleScope)
        }
    }

    private fun getTabPosition(): Int {
        return (binding.recyclerView.layoutManager as? LinearLayoutManager)?.let { lm ->
            lm.findFirstVisibleItemPosition().takeIf { it != RecyclerView.NO_POSITION }
        } ?: ZERO
    }

    private fun TabLayout.setTabs(categories: List<DishesCategoryItem>) {
        removeAllTabs()
        isVisible = false
        for (category in categories) {
            addTab(newTab().setText(getString(category.categoryId ?: R.string.empty)))
        }
        if (tabCount > 1) isVisible = true
    }

    private fun showError() {
        binding.apply {
            errorBody.isVisible = true
            recyclerView.isVisible = false
            retryButton.setOnClickListener { retryAgain() }
        }
    }

    private fun retryAgain() {
        binding.apply {
            //     dishesViewModel.loadDishes()
            errorViewIsVisible = false
            errorBody.isVisible = false
            recyclerView.isVisible = true
        }
    }

    override fun onButtonClick(item: DishItem) {
        // TODO отправить пользователя выбрать доп в details
        orderViewModel.addToOrderCart(id = item.id, extras = emptyList())
    }

    override fun onItemClick(item: DishItem) =
        findNavController().navigate(
            DishesFragmentDirections.actionToDetailsFragment(item, arrayOf())
        )

    override fun onEditOrderDish(item: OrderDish) {
        findNavController().navigate(
            DishesFragmentDirections.actionToDetailsFragment(
                dishItem = item.dish,
                extrasList = item.extras.toTypedArray()
            )
        )
    }

    private fun onQuantityActionClick(item: OrderDish, action: QuantityButtonAction) {
        orderViewModel.updateItemByAction(
            id = item.dish.id,
            action = action,
            extras = item.extras,
            additionalTime = item.additionTime
        )
    }

    private fun navigateToSearchFragment() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        findNavController().navigate(R.id.action_dishesFragment_to_searchDishesFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchButton = menu.findItem(R.id.searchButton)

        searchButton.setOnMenuItemClickListener {
            navigateToSearchFragment()
            true
        }
    }

    private fun getEmptyItems(): List<DishesCategoryItem> {
        return emptyList()
    }

    private fun getProgressItems(): List<DishesCategoryItem> {
        val items = IntRange(1, 10).map { DishProgress }
        return listOf(DishesCategoryItem(null, items))
    }

    private fun getErrorItems(): List<DishesCategoryItem> {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        return emptyList()
    }
}
